package com.ripbull.coresdk.fcm;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;
import com.ripbull.coresdk.chat.mapper.MessageRecord;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.core.type.NetworkEvent;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.eRTCSDK;
import com.ripbull.coresdk.utils.Logger;
import com.ripbull.ertc.mqtt.utils.Constants;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FcmModuleImpl implements FcmModule {

  private Context appContext;
  private DataManager dataManager;
  private EventHandler eventHandler;

  private static final String TAG = ERTCFirebaseMessagingService.class.getSimpleName();

    public static FcmModuleImpl newInstance(EventHandler eventHandler, DataManager dataManager) {
    return new FcmModuleImpl(eRTCSDK.getAppContext(), eventHandler, dataManager);
  }

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  private FcmModuleImpl(Context context, EventHandler eventHandler, DataManager dataManager) {
    this.appContext = context;
    this.dataManager = dataManager;
    this.eventHandler= eventHandler;

    /*compositeDisposable.add(eventHandler
        .source()
        .filter(NetworkEvent.filterType(EventType.MESSAGE_ADDED))
        .filter(networkEvent -> !AvailabilityStatus.DND.getStatus()
            .equals(eRTCSDK.user().getUserAvailabilityStatus()))
        .filter(networkEvent -> !networkEvent.messageRecord().getSenderId()
            .equals(this.dataManager.preference().getAppUserId()))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::messageAdded, Throwable::printStackTrace));*/
  }

  private void messageAdded(NetworkEvent networkEvent){
    MessageRecord messageRecord = networkEvent.messageRecord();
    handleDataMessage(messageRecord);
  }


  @Override
  public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    Logger.d(Logger.FCM, "From: " + remoteMessage.getFrom());

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Logger.d(Logger.FCM, "Notification Body: " + remoteMessage.getNotification().getBody());
    }

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Logger.d(Logger.FCM, "Data Payload: " + remoteMessage.getData().toString());

      try {
        String eventType = remoteMessage.getData().get("eventType");
        if(!TextUtils.isEmpty(eventType)) {
          switch (eventType) {
            case Constants.USER_DB_UPDATED:
              if (Objects.requireNonNull(remoteMessage.getData().get("message")).contains("addUpdated")) {
                compositeDisposable.add(eRTCSDK.user()
                    .getNewUsers("addUpdated")
                    .subscribeOn(Schedulers.io())
                    .subscribe(userRecords -> {
                      // No handler
                    }, Throwable::printStackTrace));
              } else if (Objects.requireNonNull(remoteMessage.getData().get("message")).contains("deleted")) {
                eRTCSDK.event().messageFromFcm(eventType,
                    new FcmMessageImpl(
                        eventType,
                        remoteMessage.getData().get("message"),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("body")
                    )
                );
              } else if (Objects.requireNonNull(remoteMessage.getData().get("message")).contains("inactive")) {
                compositeDisposable.add(eRTCSDK.user()
                    .getNewUsers("inactive")
                    .subscribeOn(Schedulers.io())
                    .subscribe(userRecords -> {
                      // No handler
                    }, Throwable::printStackTrace));
              }
              break;
            case Constants.TENANT_CONFIG_MODIFIED:
            case Constants.LOGOUT:
              eRTCSDK.event().messageFromFcm(
                  eventType,
                  new FcmMessageImpl(eventType, "", "", "")
              );
              break;
            case Constants.CHAT_EVENT:
            case Constants.MSG_READ_STATUS_TOPIC:
            case Constants.GROUP_UPDATED:
            case Constants.CHAT_REACTION_EVENT:
            case Constants.CHAT_UPDATE_EVENT:
              String message = remoteMessage.getData().get("message");
              String title = remoteMessage.getData().get("title");
              String body = remoteMessage.getData().get("body");
              eRTCSDK.event().messageFromFcm(eventType + ":",
                  new FcmMessageImpl(eventType + ":", message, title, body));
              break;
            case Constants.USER_SELF_UPDATE:
              eRTCSDK.event().messageFromFcm(eventType + ":",
                  new FcmMessageImpl(
                      eventType + ":",
                      remoteMessage.getData().get("message"),
                      remoteMessage.getData().get("title"),
                      remoteMessage.getData().get("body")
                  )
              );
              break;
            case Constants.CHAT_HISTORY_CLEARED:
            case Constants.CHAT_SETTING_UPDATED:
            case Constants.ANNOUNCEMENT:
              eRTCSDK.event().messageFromFcm(eventType,
                  new FcmMessageImpl(
                      eventType,
                      remoteMessage.getData().get("message"),
                      remoteMessage.getData().get("title"),
                      remoteMessage.getData().get("body")
                  )
              );
              break;
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void clearNotification() {
    try {
      NotificationPreferenceManager preferenceManager = new NotificationPreferenceManager(appContext);
      preferenceManager.clearNotifications();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void handleDataMessage(MessageRecord messageRecord) {

    Logger.d(Logger.FCM, "handleDataMessage ***** ");

    //Logger.d(TAG, "Availability status : "+ eRTCSDK.user().getUserAvailabilityStatus());

    try {
      NotificationRecord notificationRecord = messageRecord.getNotificationRecord();
      if(notificationRecord != null &&
          !TextUtils.isEmpty(notificationRecord.getTitle()) &&
          !TextUtils.isEmpty(notificationRecord.getBody())){
        String title = Objects.requireNonNull(messageRecord.getNotificationRecord()).getTitle();
        String threadId = messageRecord.getThreadId();
        String body = NotificationUtils.Companion.uriDecoder(Objects.requireNonNull(
            Objects.requireNonNull(messageRecord.getNotificationRecord()).getBody()));

        body = NotificationUtils.Companion.parseRegex(body);
        // for encrypted text decrypting
        /*if (jsonMessage.has("encryptedChat")
            && jsonMessage.has("msgType")
            && MessageType.TEXT.getType().equals(jsonMessage.getString("msgType"))) {

          JSONObject jsonSender = jsonMessage.getJSONObject("senderKeyDetails");
          String publicKey = jsonSender.getString("publicKey");

          JSONObject jsonEChat = jsonMessage.getJSONObject("encryptedChat");
          String eMsg = jsonEChat.getString("message");
          String ertcUserId = jsonEChat.getString("eRTCUserId");
          String deviceId = jsonEChat.getString("deviceId");
          String keyId = jsonEChat.getString("keyId");

          EKeyTable myKeyTable = DatabaseManagerImpl.instance(eRTCSDK.getAppContext())
              .ekeyDao()
              .getPrivateKeyByKeyId(ertcUserId, deviceId, tenantId, keyId);
          String privateKey = myKeyTable.getPrivateKey();

          body = ECDHUtils.decrypt(eMsg, publicKey, privateKey);
        }*/
        // JSONObject messageObject = new JSONObject(message);

        NotificationUtils.Companion.showNotificationMessage(title, body, threadId, eRTCSDK.getAppContext());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
