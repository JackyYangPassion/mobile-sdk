package io.inappchat.inappchat.tenant.interactor;

import android.annotation.SuppressLint;
import com.google.firebase.iid.FirebaseInstanceId;
import android.text.TextUtils;
import android.util.Log;

import io.inappchat.inappchat.UserConfiguration;
import io.inappchat.inappchat.core.base.UseCase;
import io.inappchat.inappchat.core.type.AccountDetails;
import io.inappchat.inappchat.core.type.NotificationSettingsType;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.e2e.E2EMapper;
import io.inappchat.inappchat.e2e.ECDHUtils;
import io.inappchat.inappchat.user.mapper.UserMapper;
import io.inappchat.inappchat.utils.Constants;
import io.inappchat.inappchat.InAppChat;
import io.inappchat.inappchat.cache.database.dao.UserDao;
import io.inappchat.inappchat.cache.database.entity.EKeyTable;
import io.inappchat.inappchat.cache.database.entity.Tenant;
import io.inappchat.inappchat.cache.database.entity.TenantConfig;
import io.inappchat.inappchat.cache.database.entity.User;
import io.inappchat.inappchat.cache.preference.PreferenceManager;
import io.inappchat.inappchat.remote.model.request.ChangePassword;
import io.inappchat.inappchat.remote.model.request.ForgotPassword;
import io.inappchat.inappchat.remote.model.request.Login;
import io.inappchat.inappchat.remote.model.request.Logout;
import io.inappchat.inappchat.remote.model.request.UpdateUserRequest;
import io.inappchat.inappchat.remote.model.response.ChatUserResponse;
import io.inappchat.inappchat.remote.model.response.E2EKey;
import io.inappchat.inappchat.remote.model.response.TenantDetailResponse;
import io.inappchat.inappchat.remote.util.HeaderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;

/** Created by DK on 05/12/18. */
public class AuthenticationRepositoryImpl extends UseCase<DataManager>
    implements AuthenticationRepository {

  public AuthenticationRepositoryImpl(DataManager dataManager) {
    super(dataManager);
  }

  @Override
  public Single<Result> connect(AccountDetails details) {
    PreferenceManager preference = data().preference();
    return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
      data().mqtt().setMqttServer(preference.getMqttServer());
      data().preference().setAppUserId(details.getAppUserId());
      data().network().api(data().networkConfig());

      data().preference().setFcmToken(details.getAuthConfig().getFcmToken());
      data().preference().setAppUserId(preference.getAppUserId());

      User user =
          new User(details.getAppUserId(), tenantId(), "Third party user", "active", "", "", "",
              details.getType().getValue(), System.currentTimeMillis(),
              UUID.randomUUID().toString());
      data().db().userDao().insertWithReplace(user);
      emitter.onSuccess(Boolean.TRUE);
    }).flatMap(aBoolean -> {

      final String publicKey, privateKey;
      if (isE2EFeatureEnabled(tenantId())) {
        String[] keyPair = ECDHUtils.generateKeyPair();
        publicKey = keyPair[0];
        privateKey = keyPair[1];
      } else {
        publicKey = null;
        privateKey = "";
      }

      Single<ChatUserResponse> chatUserId = data().network()
          .api()
          .getChatUser(Objects.requireNonNull(preference.getTenantId()),
              new UpdateUserRequest(Objects.requireNonNull(preference.getAppUserId()),
                  preference.getFcmToken(), Objects.requireNonNull(preference.getDeviceId()),
                  Constants.TenantConfig.DEVICE_TYPE, publicKey, null,
                  details.getAuthConfig().getPayload()));
      return chatUserId.flatMap(response -> {
        UserDao userDao = data().db().userDao();
        String appUserId = preference.getAppUserId();
        User userByIdInSync = userDao.getUserByIdInSync(preference.getTenantId(), appUserId);
        userByIdInSync.setUserChatId(response.getERTCUserId());
        userByIdInSync.setAvailabilityStatus(response.getAvailabilityStatus());
        if (response.getNotificationSettings() != null) {
          userByIdInSync.setNotificationSettings(response.getNotificationSettings().getAllowFrom());
        } else {
          userByIdInSync.setNotificationSettings(NotificationSettingsType.ALL.getMute());
        }
        userDao.insertWithReplace(userByIdInSync);
        preference.setChatUserId(response.getERTCUserId());
        preference.setName(response.getName());
        if (response.getToken() != null) {
          preference.setChatToken(response.getToken().getAccessToken());
          preference.setChatRefreshToken(response.getToken().getRefreshToken());
        } else {
          preference.setChatToken(HeaderUtils.SEPARATOR);
          preference.setChatRefreshToken(HeaderUtils.SEPARATOR);
        }
        data().network().api(data().networkConfig());
        List<E2EKey> e2eKeys = response.getE2eKeys();
        if (!TextUtils.isEmpty(publicKey) && e2eKeys != null && !e2eKeys.isEmpty()) {
          EKeyTable eKeyTable = new EKeyTable();
          eKeyTable.setDeviceId(preference.getDeviceId());
          eKeyTable.setPublicKey(Objects.requireNonNull(publicKey));
          eKeyTable.setPrivateKey(privateKey);
          eKeyTable.setErtcUserId(response.getERTCUserId());
          eKeyTable.setTenantId(tenantId());

          data().db().ekeyDao().save(eKeyTable);
          for (E2EKey e2EKey : e2eKeys) {
            if (e2EKey != null) {
              if (e2EKey.getDeviceId().equals(preference.getDeviceId())) {
                data().db()
                    .ekeyDao()
                    .setKeyId(preference.getChatUserId(), e2EKey.getPublicKey(), e2EKey.getKeyId(),
                        eKeyTable.getTime(), preference.getDeviceId());
              } else {
                EKeyTable eKeyTable1 = E2EMapper.getEKey(e2EKey, tenantId());
                data().db().ekeyDao().save(eKeyTable1);
              }
            }
          }
        }
        // data().mqtt().createConnection(tenantId(), response.getERTCUserId());
        return Single.just(new Result(true, "Connected successfully", ""));
      });
    });
  }

  @Override
  public Single<Result> validateNamespace(String url) {
    return data().network().api().validateUrl(url).map(tenant -> {
      data().db()
          .tenantDao()
          .save(new Tenant(tenant.getId(), tenant.getName(),
              Objects.requireNonNull(data().preference().getApiKey()), tenant.getNameSpace()));
      TenantDetailResponse.Config config = tenant.getConfig();
      ArrayList<TenantConfig> configArrayList = getConfigList(tenant, config);

      data().db().tenantDao().save(configArrayList);
      data().preference().setTenantId(tenant.getId());
      data().preference().setLoginType(config.getLoginType());
      data().preference().setChatApiKey(config.getServerDetails().getChatServer().getApiKey());

      data().preference().setMqttApiKey(config.getServerDetails().getMqttServer().getApiKey());
      data().preference().setChatServer(config.getServerDetails().getChatServer().getUrl() + "v1/");

      if (config.getServerDetails().getUserServer() != null) {
        data().preference().setUserServer(config.getServerDetails().getUserServer().getUrl() + "v1/");
        data().preference().setUserApiKey(config.getServerDetails().getUserServer().getApiKey());
      }

      data().preference().setMqttServer(config.getServerDetails().getMqttServer().getUrl());
      data().network().api(data().networkConfig());
      data().preference().setProjectType(tenant.getProjectType());
      return new Result(true, "", "");
    }).compose(applySchedulers());
  }

  @SuppressLint("CheckResult")
  @Override
  public Single<Result> login(AccountDetails details) {
    PreferenceManager preference = data().preference();
    data().mqtt().setMqttServer(preference.getMqttServer());
    return data().network()
        .api()
        .login(tenantId(),
            new Login(details.getType().getValue(), details.getUsername(), details.getPassword(),
                preference.getFcmToken(), Objects.requireNonNull(preference.getDeviceId()),
                Constants.TenantConfig.DEVICE_TYPE))
        .flatMap(userResponse -> {
          data().preference().setLastCallTimeUser("" + System.currentTimeMillis() / 1000);
          data().preference().setUserId(userResponse.getUserId());
          data().preference().setAppUserId(userResponse.getAppUserId());
          if (userResponse.getToken() != null) {
            data().preference().setUserToken(userResponse.getToken().getAccessToken());
            data().preference().setUserRefreshToken(userResponse.getToken().getRefreshToken());
          } else {
            data().preference().setUserToken(HeaderUtils.SEPARATOR);
            data().preference().setUserRefreshToken(HeaderUtils.SEPARATOR);
          }
          data().network().api(data().networkConfig());
          data().db()
              .userDao()
              .insertWithReplace(
                  UserMapper.from(userResponse, tenantId(), AccountDetails.Type.EMAIL.getValue(),
                      data().preference().getUserServer()));
          final String publicKey;
          final String privateKey;
          if (isE2EFeatureEnabled(tenantId())) {
            String[] keyPair = ECDHUtils.generateKeyPair();
            publicKey = keyPair[0];
            privateKey = keyPair[1];
          } else {
            publicKey = null;
            privateKey = "";
          }
          Single<ChatUserResponse> chatUserId = data().network()
              .api()
              .getChatUser(Objects.requireNonNull(preference.getTenantId()),
                  new UpdateUserRequest(Objects.requireNonNull(preference.getAppUserId()),
                      preference.getFcmToken(), Objects.requireNonNull(preference.getDeviceId()),
                      Constants.TenantConfig.DEVICE_TYPE, publicKey));

          return chatUserId.flatMap(response -> {
            UserDao userDao = data().db().userDao();
            String appUserId = preference.getAppUserId();
            User userByIdInSync = userDao.getUserByIdInSync(preference.getTenantId(), appUserId);
            userByIdInSync.setUserChatId(response.getERTCUserId());
            userByIdInSync.setAvailabilityStatus(response.getAvailabilityStatus());
            if (response.getNotificationSettings() != null) {
              userByIdInSync.setNotificationSettings(
                  response.getNotificationSettings().getAllowFrom());
            } else {
              userByIdInSync.setNotificationSettings(NotificationSettingsType.ALL.getMute());
            }
            userDao.insertWithReplace(userByIdInSync);
            preference.setChatUserId(response.getERTCUserId());
            preference.setName(response.getName());
            if (response.getToken() != null) {
              preference.setChatToken(response.getToken().getAccessToken());
              preference.setChatRefreshToken(response.getToken().getRefreshToken());
            } else {
              preference.setChatToken(HeaderUtils.SEPARATOR);
              preference.setChatRefreshToken(HeaderUtils.SEPARATOR);
            }
            data().network().api(data().networkConfig());
            List<E2EKey> e2eKeys = response.getE2eKeys();
            if (!TextUtils.isEmpty(publicKey) && e2eKeys != null && !e2eKeys.isEmpty()) {
              EKeyTable eKeyTable = new EKeyTable();
              eKeyTable.setDeviceId(preference.getDeviceId());
              eKeyTable.setPublicKey(Objects.requireNonNull(publicKey));
              eKeyTable.setPrivateKey(privateKey);
              eKeyTable.setErtcUserId(response.getERTCUserId());
              eKeyTable.setTenantId(tenantId());

              data().db().ekeyDao().save(eKeyTable);
              for (E2EKey e2EKey : e2eKeys) {
                if (e2EKey != null) {
                  Log.i("VIKK", "yaay: " + e2EKey.getDeviceId());
                  if (e2EKey.getDeviceId().equals(preference.getDeviceId())) {
                    Log.i("VIKK", "yaay");
                    data().db()
                        .ekeyDao()
                        .setKeyId(preference.getChatUserId(), e2EKey.getPublicKey(),
                            e2EKey.getKeyId(), eKeyTable.getTime(), preference.getDeviceId());
                  } else {
                    Log.i("VIKK", "yaay else ");
                    EKeyTable eKeyTable1 = E2EMapper.getEKey(e2EKey, tenantId());
                    data().db().ekeyDao().save(eKeyTable1);
                  }
                }
              }
            }
            // data().mqtt().createConnection(tenantId(), response.getERTCUserId());
            return Single.just(new Result(true, "", ""));
          });
        })
        .compose(applySchedulers());
  }

  @Override
  public Boolean isTenantValidated() {
    return !TextUtils.isEmpty(tenantId());
  }

  @Override
  public Boolean isUserAuthenticated() {
    return !TextUtils.isEmpty(tenantId()) && !TextUtils.isEmpty(userId());
  }

  @Override
  public Single logout() {
    return null;
  }

  private String tenantId() {
    return data().preference().getTenantId();
  }

  private String userId() {
    return data().preference().getUserId();
  }

  private String appUserId() {
    return data().preference().getAppUserId();
  }

  @Override
  public String getLoginType() {
    return data().preference().getLoginType();
  }

  @Override
  public Single<Result> forgotPassword(AccountDetails details) {
    return data().network()
        .api()
        .forgotPassword(tenantId(),
            new ForgotPassword(details.getType().getValue(), details.getUsername()))
        .map(response -> new Result(response.getSuccess(), response.getMessage(),
            response.getErrorCode()))
        .compose(applySchedulers());
  }

  @Override
  public Single<Result> changePassword(AccountDetails details) {
    return data().network()
        .api()
        .changePassword(tenantId(),
            new ChangePassword(details.getType().getValue(), appUserId(), details.getPassword(),
                details.getNewPassword()), userId())
        .map(response -> new Result(response.getSuccess(), response.getMessage(),
            response.getErrorCode()))
        .compose(applySchedulers());
  }

  @Override
  public Single<Result> logoutOtherDevices() {
    return data().network()
        .api()
        .otherDeviceChatLogout(tenantId(), data().preference().getChatUserId(),
            new Logout(appUserId(), data().preference().getDeviceId()))
        .map(response -> {
          data().db().tenantDao().deleteAll();
          data().db().threadDao().deleteAll();
          data().db().singleChatDao().deleteAll();
          data().db().groupDao().deleteAll();
          data().db().ekeyDao().deleteAll();
          data().db().userDao().deleteAll();
          data().mqtt().removeConnectionAndSubscription();
          FirebaseInstanceId.getInstance().deleteInstanceId();
          InAppChat.fcm().clearNotification();
          data().preference().clearData();
          data().db().downloadMediaDao().clear();
          return new Result(response.getSuccess(), response.getMessage(), response.getErrorCode());
        })
        .compose(applySchedulers());
  }

  @Override
  public Single<Result> updateFeatureFlag(String nameSpace) {
    return data().network().api().validateUrl(nameSpace).map(tenant -> {
      TenantDetailResponse.Config config = tenant.getConfig();
      ArrayList<TenantConfig> configArrayList = getConfigList(tenant, config);

      data().db().tenantDao().save(configArrayList);
      data().preference().setProjectType(tenant.getProjectType());
      return new Result(true, "", "");
    }).compose(applySchedulers());
  }

  @Override
  public String projectType() {
    return data().preference().getProjectType();
  }

  @Override
  public Single<Result> configureUser(UserConfiguration userConfiguration) {
    PreferenceManager preference = data().preference();
    return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
      data().mqtt().setMqttServer(preference.getMqttServer());
      data().preference().setLastCallTimeUser("" + System.currentTimeMillis() / 1000);
      data().preference().setUserId(userConfiguration.getUserId());
      data().preference().setAppUserId(userConfiguration.getAppUserId());
      data().network().api(data().networkConfig());
      if (userConfiguration.getAccessToken() != null) {
        data().preference().setUserToken(userConfiguration.getAccessToken());
        data().preference().setChatToken(userConfiguration.getAccessToken());
        data().preference().setUserRefreshToken(userConfiguration.getRefreshToken());
      } else {
        data().preference().setUserToken(HeaderUtils.SEPARATOR);
        data().preference().setUserRefreshToken(HeaderUtils.SEPARATOR);
      }
      User user =
              new User(userConfiguration.getAppUserId(), tenantId(), userConfiguration.getName(),
                      userConfiguration.getAppState(), userConfiguration.getLoginType(),
                      userConfiguration.getProfilePic(), userConfiguration.getProfilePicThumb(),
                      userConfiguration.getProfileStatus(), System.currentTimeMillis(),
                      userConfiguration.getUserId());
      data().db().userDao().insertWithReplace(user);
      emitter.onSuccess(Boolean.TRUE);
    }).map(aBoolean -> {
      data().network().api(data().networkConfig());
      return aBoolean;
    }).flatMap(aBoolean -> data().network().api().activeAuth0User(tenantId(), userConfiguration.getAppUserId())
            .flatMap(result -> {
              final String publicKey, privateKey;
              if (isE2EFeatureEnabled(tenantId())) {
                String[] keyPair = ECDHUtils.generateKeyPair();
                publicKey = keyPair[0];
                privateKey = keyPair[1];
              } else {
                publicKey = null;
                privateKey = "";
              }

              Single<ChatUserResponse> chatUserId = data().network()
                      .api()
                      .getChatUser(Objects.requireNonNull(preference.getTenantId()),
                              new UpdateUserRequest(Objects.requireNonNull(preference.getAppUserId()),
                                      preference.getFcmToken(), Objects.requireNonNull(preference.getDeviceId()),
                                      Constants.TenantConfig.DEVICE_TYPE, publicKey, null,
                                      userConfiguration.getPayload()));
              return chatUserId.flatMap(response -> {
                UserDao userDao = data().db().userDao();
                String appUserId = preference.getAppUserId();
                User userByIdInSync = userDao.getUserByIdInSync(preference.getTenantId(), appUserId);
                userByIdInSync.setUserChatId(response.getERTCUserId());
                userByIdInSync.setAvailabilityStatus(response.getAvailabilityStatus());
                if (response.getNotificationSettings() != null) {
                  userByIdInSync.setNotificationSettings(response.getNotificationSettings().getAllowFrom());
                } else {
                  userByIdInSync.setNotificationSettings(NotificationSettingsType.ALL.getMute());
                }
                userDao.insertWithReplace(userByIdInSync);
                preference.setChatUserId(response.getERTCUserId());
                preference.setName(response.getName());
                data().network().api(data().networkConfig());
                List<E2EKey> e2eKeys = response.getE2eKeys();
                if (!TextUtils.isEmpty(publicKey) && e2eKeys != null && !e2eKeys.isEmpty()) {
                  EKeyTable eKeyTable = new EKeyTable();
                  eKeyTable.setDeviceId(preference.getDeviceId());
                  eKeyTable.setPublicKey(Objects.requireNonNull(publicKey));
                  eKeyTable.setPrivateKey(privateKey);
                  eKeyTable.setErtcUserId(response.getERTCUserId());
                  eKeyTable.setTenantId(tenantId());

                  data().db().ekeyDao().save(eKeyTable);
                  for (E2EKey e2EKey : e2eKeys) {
                    if (e2EKey != null) {
                      if (e2EKey.getDeviceId().equals(preference.getDeviceId())) {
                        data().db()
                                .ekeyDao()
                                .setKeyId(preference.getChatUserId(), e2EKey.getPublicKey(), e2EKey.getKeyId(),
                                        eKeyTable.getTime(), preference.getDeviceId());
                      } else {
                        EKeyTable eKeyTable1 = E2EMapper.getEKey(e2EKey, tenantId());
                        data().db().ekeyDao().save(eKeyTable1);
                      }
                    }
                  }
                }
                // data().mqtt().createConnection(tenantId(), response.getERTCUserId());
                return Single.just(new Result(true, "User configured successfully", ""));
              });
            }));
  }

  private ArrayList<TenantConfig> getConfigList(TenantDetailResponse tenant,
      TenantDetailResponse.Config config) {
    ArrayList<TenantConfig> configArrayList = new ArrayList<>();

    TenantDetailResponse.Features features = config.getFeatures();

    configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.CHAT_ENABLED,
        Objects.requireNonNull(features.getChat()).getEnabled() ? "1" : "0"));

    configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_ENABLED,
        Objects.requireNonNull(features.getGroupChat()).getEnabled() ? "1" : "0"));

    configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.TYPING_STATUS,
        Objects.requireNonNull(features.getTypingStatus()).getEnabled() ? "1" : "0"));

    TenantDetailResponse.Features.ReadReceipt readReceipt = features.getReadReceipt();

    if (readReceipt != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.READ_RECEIPTS,
          Objects.requireNonNull(features.getReadReceipt()).getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.ReadReceipts.SENT,
          readReceipt.getSendAlert().getEnabled() ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ReadReceipts.DELIVERED,
              readReceipt.getDeliveredAlert().getEnabled() ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.ReadReceipts.READ,
          readReceipt.getReadAlert().getEnabled() ? "1" : "0"));
    }

    if (features.getUserProfile() != null) {
      TenantDetailResponse.Features.UserProfile userProfile = features.getUserProfile();
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.USER_PROFILE,
          userProfile.getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.NAME,
          userProfile.getUserName().getEnabled() != null && userProfile.getUserName().getEnabled()
              ? "1" : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.Name.EDITABLE,
              userProfile.getUserName().getEditable() != null && userProfile.getUserName()
                  .getEditable() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.IMAGE,
          userProfile.getImage().getEnabled() != null && userProfile.getImage().getEnabled() ? "1"
              : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.Image.EDITABLE,
              userProfile.getImage().getEditable() != null && userProfile.getImage().getEditable()
                  ? "1" : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.FAV_CONTACTS,
              userProfile.getFavoriteContacts().getEnabled() ? "1" : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.UserProfile.AVAILABLE_STATUS,
              userProfile.getAvailabilityStatus().getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(),
          Constants.TenantConfig.UserProfile.AvailableStatus.ALLOW_OVERRIDING,
          userProfile.getAvailabilityStatus().getAllowOverriding() ? "1" : "0"));
    }

    if (features.getBlockUser() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.BLOCK_USER,
          features.getBlockUser().getEnabled() ? "1" : "0"));
    }

    if (features.getStarredChat() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.STAR_MESSAGE,
          features.getStarredChat().getEnabled() ? "1" : "0"));
    }

    if (features.getNotificationSettings() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.NOTIFICATION,
          features.getNotificationSettings().getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(),
          Constants.TenantConfig.SingeChat.Notification.MUTE_SETTINGS,
          features.getNotificationSettings().getChatMuteSetting() != null && Objects.requireNonNull(
              features.getNotificationSettings().getChatMuteSetting()).getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(),
          Constants.TenantConfig.GroupChat.Notification.MUTE_SETTINGS,
          features.getNotificationSettings().getGroupChatMuteSetting() != null
              && features.getNotificationSettings().getGroupChatMuteSetting().getEnabled() ? "1"
              : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(),
          Constants.TenantConfig.UserProfile.Notification.MUTE_SETTINGS,
          features.getNotificationSettings().getGlobalMuteSetting() != null
              && features.getNotificationSettings().getGlobalMuteSetting().getEnabled() ? "1"
              : "0"));
    }
    if (features.getSearchFilter() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.SEARCH_FILTER,
          features.getSearchFilter().getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.ChatAttachment chatAttachment = features.getChatAttachment();
    if (chatAttachment != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.ATTACHMENT,
          chatAttachment.getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_AUDIO,
          (chatAttachment.getAudioEnabled().getEnabled() && chatAttachment.getAudioEnabled()
              .getIndividualChatAudioSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_VIDEO,
          (chatAttachment.getVideoEnabled().getEnabled() && chatAttachment.getVideoEnabled()
              .getIndividualChatVideoSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_LOCATION, (
              chatAttachment.getLocationEnabled().getEnabled()
                  && chatAttachment.getLocationEnabled()
                  .getIndividualChatLocationSharing()
                  .getEnabled()) ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_CONTACT,
              (chatAttachment.getContactEnabled().getEnabled() && chatAttachment.getContactEnabled()
                  .getIndividualChatContactSharing()
                  .getEnabled()) ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_DOCUMENT, (
              chatAttachment.getDocumentEnabled().getEnabled()
                  && chatAttachment.getDocumentEnabled()
                  .getIndividualChatDocumentSharing()
                  .getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_IMAGE,
          (chatAttachment.getImageEnabled().getEnabled() && chatAttachment.getImageEnabled()
              .getIndividualChatImageSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.SINGLE_CHAT_GIPHY,
          (chatAttachment.getGifyEnabled().getEnabled() && chatAttachment.getGifyEnabled()
              .getIndividualChatGifySharing()
              .getEnabled()) ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_AUDIO,
          (chatAttachment.getAudioEnabled().getEnabled() && chatAttachment.getAudioEnabled()
              .getGroupChatAudioSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_VIDEO,
          (chatAttachment.getVideoEnabled().getEnabled() && chatAttachment.getVideoEnabled()
              .getGroupChatVideoSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_LOCATION, (
              chatAttachment.getLocationEnabled().getEnabled()
                  && chatAttachment.getLocationEnabled().getGroupChatLocationSharing().getEnabled())
              ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_CONTACT,
              (chatAttachment.getContactEnabled().getEnabled() && chatAttachment.getContactEnabled()
                  .getGroupChatContactSharing()
                  .getEnabled()) ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_DOCUMENT, (
              chatAttachment.getDocumentEnabled().getEnabled()
                  && chatAttachment.getDocumentEnabled().getGroupChatDocumentSharing().getEnabled())
              ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_IMAGE,
          (chatAttachment.getImageEnabled().getEnabled() && chatAttachment.getImageEnabled()
              .getGroupChatImageSharing()
              .getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GROUP_CHAT_GIPHY,
          (chatAttachment.getGifyEnabled().getEnabled() && chatAttachment.getGifyEnabled()
              .getGroupChatGifySharing()
              .getEnabled()) ? "1" : "0"));
    }

    TenantDetailResponse.Features.ForwardChat forwardChat = features.getForwardChat();
    if (forwardChat != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.FORWARD_CHAT,
          forwardChat.getEnabled() ? "1" : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.SINGLE_CHAT_TEXT,
              forwardChat.getTextEnabled().getIndividualChatTextForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.SINGLE_CHAT_MEDIA,
              forwardChat.getMediaEnabled().getIndividualChatMediaForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.SINGLE_CHAT_LOCATION,
              forwardChat.getLocationEnabled().getIndividualChatLocationForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.SINGLE_CHAT_CONTACT,
              forwardChat.getContactEnabled().getIndividualChatContactForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.SINGLE_CHAT_GIPHY,
              forwardChat.getGifyEnabled().getIndividualChatGifyForward().getEnabled() ? "1"
                  : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.GROUP_CHAT_TEXT,
              forwardChat.getTextEnabled().getGroupChatTextForward().getEnabled() ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.GROUP_CHAT_MEDIA,
              forwardChat.getMediaEnabled().getGroupChatMediaForward().getEnabled() ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.GROUP_CHAT_LOCATION,
              forwardChat.getLocationEnabled().getGroupChatLocationForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.GROUP_CHAT_CONTACT,
              forwardChat.getContactEnabled().getGroupChatContactForward().getEnabled() ? "1"
                  : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.ForwardChat.GROUP_CHAT_GIPHY,
              forwardChat.getGifyEnabled().getGroupChatGifyForward().getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.EditChat editChat = features.getEditChat();
    if (editChat != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.EDIT_CHAT,
          editChat.getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.E2EEncryption e2eEncryption = features.getE2EEncryption();
    if (e2eEncryption != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT,
          e2eEncryption.getEnabled() ? "1" : "0"));

      //if(e2eEncryption.getEnabled()) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT_TEXT,
          (e2eEncryption.getText() != null && e2eEncryption.getText().getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT_MEDIA,
          (e2eEncryption.getMedia() != null && e2eEncryption.getMedia().getEnabled()) ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT_LOCATION,
          (e2eEncryption.getLocation() != null && e2eEncryption.getLocation().getEnabled()) ? "1"
              : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT_CONTACT,
          (e2eEncryption.getContact() != null && e2eEncryption.getContact().getEnabled()) ? "1"
              : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.E2E_CHAT_GIFY,
          (e2eEncryption.getGify() != null && e2eEncryption.getGify().getEnabled()) ? "1" : "0"));
      //}
    }

    TenantDetailResponse.Features.DeleteChat deleteChat = features.getDeleteChat();
    if (deleteChat != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.DELETE_CHAT,
          deleteChat.getEnabled() ? "1" : "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.DeleteChat.DELETE_FOR_SELF,
              deleteChat.getDeleteForSelf().getEnabled() ? "1" : "0"));
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.DeleteChat.DELETE_FOR_EVERYONE,
              deleteChat.getDeleteForEveryone().getEnabled() ? "1" : "0"));
    } else {
      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.DELETE_CHAT, "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.DeleteChat.DELETE_FOR_SELF, "0"));

      configArrayList.add(
          new TenantConfig(tenant.getId(), Constants.TenantConfig.DeleteChat.DELETE_FOR_EVERYONE,
              "0"));
    }

    configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.CHAT_REACTIONS,
        Objects.requireNonNull(features.getChatReactions()).getEnabled() ? "1" : "0"));

    configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.REPLY_THREAD,
        Objects.requireNonNull(features.getReplyThread()).getEnabled() ? "1" : "0"));

    if (features.getFollowChat() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.FOLLOW_CHAT,
          features.getFollowChat().getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.UnifiedSearch unifiedSearch = features.getUnifiedSearch();
    if (unifiedSearch != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.CHANNEL_SEARCH,
          features.getUnifiedSearch().getEnabled() ? "1" : "0"));
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.GLOBAL_SEARCH,
          features.getUnifiedSearch().getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.UserMentions userMentions = features.getUserMentions();
    if (userMentions != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.USER_MENTIONS,
          features.getUserMentions().getEnabled() ? "1" : "0"));
    }

    if (features.getAnnouncements() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.ANNOUNCEMENT,
          features.getAnnouncements().getEnabled() ? "1" : "0"));
    }

    if (features.getCopyChat() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.COPY,
          features.getCopyChat().getEnabled() ? "1" : "0"));
    }

    if (features.getLocalSearch() != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.LOCAL_SEARCH,
          features.getLocalSearch().getEnabled() ? "1" : "0"));
    }

    TenantDetailResponse.Features.Moderation moderation = features.getModeration();
    if (moderation != null) {
      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.MODERATION,
          moderation.getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.DOMAIN_FILTER,
          moderation.getDomainFilter().getEnabled() ? "1" : "0"));

      configArrayList.add(new TenantConfig(tenant.getId(), Constants.TenantConfig.PROFANITY_FILTER,
          moderation.getProfanityFilter().getEnabled() ? "1" : "0"));
    }

    return configArrayList;
  }

  private boolean isE2EFeatureEnabled(String tenantId) {
    String flag = data().db()
        .tenantDao()
        .getTenantConfigValue(tenantId, Constants.TenantConfig.E2E_CHAT)
        .blockingGet();
    return "1".equals(flag);
  }
}
