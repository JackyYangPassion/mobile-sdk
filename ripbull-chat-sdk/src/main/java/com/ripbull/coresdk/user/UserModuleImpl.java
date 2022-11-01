package com.ripbull.coresdk.user;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.paging.DataSource;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;
import com.ripbull.ertc.cache.database.entity.User;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.core.type.AvailabilityStatus;
import com.ripbull.coresdk.core.type.ChatType;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.module.BaseModule;
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord;
import com.ripbull.coresdk.user.mapper.UserRecord;
import com.ripbull.coresdk.user.repository.UserRepository;
import com.ripbull.coresdk.user.repository.UserRepositoryImpl;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

/** @author meeth */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class UserModuleImpl extends BaseModule implements UserModule {

  public static UserModule newInstance(DataManager dataManager, EventHandler eventHandler) {
    return new UserModuleImpl(dataManager,eventHandler);
  }

  private final UserRepository userRepository;

  private UserModuleImpl(DataManager dataManager, EventHandler eventHandler) {
    super(dataManager);
    userRepository = UserRepositoryImpl.newInstance(dataManager,eventHandler);
  }

  private PagedList.Config config() {
    return new PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(20).build();
  }

  private <K, V> Flowable<PagedList<V>> getPagedList(
      DataSource.Factory<K, V> dataSource, PagedList.BoundaryCallback<V> boundaryCallback) {
    return new RxPagedListBuilder<>(dataSource, config())
        .setBoundaryCallback(boundaryCallback)
        .buildFlowable(BackpressureStrategy.LATEST);
  }

  @SuppressWarnings("unused")
  // @Override
  public Flowable<PagedList<User>> getPagedUsers() {
    return withTenant(
        tenantId -> {
          // Perform operation on valid tenant
          // DataSource.Factory<Integer, User> usersDataSource =
          // db().userDao().getUsersFactory(tenantId);
          return getPagedList(null, new UserBoundaryCallback(data()));
        },
        Flowable::empty);
  }

  @SuppressWarnings("unused")
  // @Override
  public Flowable<PagedList<User>> getPagedUsers(String userName) {
    return withTenant(
        tenantId -> {
          // Perform operation on valid tenant
          // DataSource.Factory<Integer, User> usersDataSource =
          //    db().userDao().getUsersWithQueryFactory(tenantId, userName);
          return getPagedList(null, new UserBoundaryCallback(data()));
        },
        Flowable::empty);
  }

  @Override
  public Flowable<List<UserRecord>> getChatUsers() {
    return userRepository.getChatUsers(getTenantId());
  }

  @Override
  public Single<List<UserRecord>> getMentionedUsers() {
    return userRepository.getMentionedUsers(getTenantId());
  }

  @Override
  public Single<List<UserRecord>> getReactionedUsers(List<String> reactionUnicodes,String msgId , String threadId, ChatType chatType) {
    return userRepository.getReactionedUsers(reactionUnicodes,msgId,threadId,chatType);
  }

  @Override
  public Single<Boolean> fetchMoreUsers() {
    return withTenant(
        tenantId -> {
          // Perform operation on valid tenant
          return Single.fromCallable(
              () -> {
                final User lastUser = userRepository.getLastUserInSync(tenantId);
                final String lastUserId = lastUser != null ? lastUser.getId() : null;
                List<User> userList = userRepository.getUsersInSync(tenantId, lastUserId);
                userRepository.saveUsersInSync(userList);
                return !userList.isEmpty();
              });
        },
        () -> Single.just(Boolean.FALSE));
  }

  @Override
  public Flowable<List<UserRecord>> getNewUsers(String addUpdateOrDelete) {
    return userRepository.getNewUsers(getTenantId(), addUpdateOrDelete);
  }


  @Override
  public Flowable<UserRecord> getUserById(@NonNull String id) {
    return withTenant(tenantId -> userRepository.getUserById(tenantId, id), Flowable::empty);
  }

  @Override
  public Single<Result> logout() {
    return userRepository.logout();
  }

  @Override
  public Single<Result> updateProfile(@NonNull String profileStatus, @NonNull String mediaPath,
      @NonNull String mediaType) {
    return userRepository.updateProfile(getTenantId(),getUserId(),profileStatus,mediaPath,mediaType);
  }

  @Override
  public Single<UserRecord> getProfile() {
    return userRepository.getProfile(getAppUserId());
  }

  @Override
  public Flowable<UserRecord> getLoggedInUser() {
    return userRepository.getLoggedInUser();
  }

  @Override
  public String getUserAvailabilityStatus() {
    return db().userDao().getUserStatus(getTenantId(), getAppUserId());
  }

  @Override
  public Single setUserAvailability(AvailabilityStatus availabilityStatus) {
    return userRepository.setUserAvailability(getTenantId(), availabilityStatus);
  }

  @Override
  public Observable<UserRecord> subscribeToUserMetaData() {
    return userRepository.subscribeToUserMetaData();
  }

  @Override
  public Single<Result> removeProfilePic() {
    return userRepository.removeProfilePic(getTenantId(),getUserId());
  }

  @Override
  public Single<Result> deactivate() {
    return userRepository.deactivate();
  }

  @Override
  public Observable<UserMetaDataRecord> metaDataOn(String appUserId) {
    return userRepository.metaDataOn(appUserId);
  }

  @Override
  public Single<Result> fetchLatestUserStatus() {
    return userRepository.fetchLatestUserStatus();
  }

  @Override
  public Observable<Result> subscribeToLogout() {
    return userRepository.subscribeToLogout();
  }
}
