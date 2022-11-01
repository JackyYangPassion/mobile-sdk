package com.ripbull.coresdk.user.repository;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.ripbull.ertc.cache.database.dao.TenantDao;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.core.type.AvailabilityStatus;
import com.ripbull.coresdk.core.type.ChatType;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.user.UserModule;
import com.ripbull.coresdk.user.UserModuleImpl;
import com.ripbull.coresdk.user.UserModuleStub;
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord;
import com.ripbull.coresdk.user.mapper.UserRecord;
import com.ripbull.coresdk.utils.Constants;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class UserModuleHookImpl implements UserModuleHook, UserModule {

    private final UserModule userModule;
    private final UserModule stub;
    private DataManager dataManager;

    public static UserModuleHook newInstance(DataManager dataManager, EventHandler eventHandler) {
        UserModule userModule = UserModuleImpl.newInstance(dataManager, eventHandler);
        UserModule stub = UserModuleStub.newInstance();
        return new UserModuleHookImpl(userModule, stub, dataManager);
    }

    private UserModuleHookImpl(@NonNull UserModule userModule, @NonNull UserModule stub,
                               DataManager dataManager) {
        this.userModule = userModule;
        this.stub = stub;
        this.dataManager = dataManager;
    }

    private TenantDao getTenantDao() {
        return dataManager.db().tenantDao();
    }

    private String getTenantId() {
        return dataManager.preference().getTenantId();
    }

    private Single<Boolean> isFeatureEnabled(String feature) {
        return getTenantDao().getTenantConfigValue(getTenantId(), feature)
                .flatMap((Function<String, SingleSource<Boolean>>) flag -> Single.just(flag.equals("1")));
    }

    private Single<Boolean> isSubscriptionAvailable() {
        return isFeatureEnabled(Constants.TenantConfig.USER_PROFILE);
    }

    private Single<Boolean> canUpdateProfile() {
        return Single.zip(isFeatureEnabled(Constants.TenantConfig.USER_PROFILE),
                isFeatureEnabled(Constants.TenantConfig.UserProfile.Image.EDITABLE),
                (aBoolean, aBoolean2) -> aBoolean && aBoolean2);
    }

    private Single<Boolean> canUpdateUserStatus() {
        return isFeatureEnabled(Constants.TenantConfig.USER_PROFILE);
    }

    @Override
    public UserModule provideModule() {
        return this;
    }

    @Override
    public Flowable<List<UserRecord>> getChatUsers() {
        return isSubscriptionAvailable().toFlowable()
                .flatMap((Function<Boolean, Flowable<List<UserRecord>>>) aBoolean -> aBoolean
                        ? userModule.getChatUsers() : stub.getChatUsers());
    }

    @Override
    public Single<List<UserRecord>> getMentionedUsers() {
        return isSubscriptionAvailable()
                .flatMap(aBoolean -> aBoolean ? userModule.getMentionedUsers() : stub.getMentionedUsers());
    }

    @Override
    public Single<List<UserRecord>> getReactionedUsers(List<String> reactionUnicodes, String msgId, String threadId, ChatType chatType) {
        return isSubscriptionAvailable()
                .flatMap(aBoolean -> aBoolean ? userModule.getReactionedUsers(reactionUnicodes, msgId, threadId,chatType) : stub.getReactionedUsers(reactionUnicodes, msgId, threadId,chatType));
    }

    @Override
    public Flowable<UserRecord> getUserById(@NonNull String id) {
        return isSubscriptionAvailable().toFlowable()
                .flatMap(
                        (Function<Boolean, Flowable<UserRecord>>) aBoolean -> aBoolean ? userModule.getUserById(
                                id) : stub.getUserById(id));
    }

    @Override
    public Single<Boolean> fetchMoreUsers() {
        return isSubscriptionAvailable().flatMap(
                (Function<Boolean, SingleSource<Boolean>>) aBoolean -> aBoolean
                        ? userModule.fetchMoreUsers() : stub.fetchMoreUsers());
    }

    @Override
    public Flowable<List<UserRecord>> getNewUsers(String addUpdateOrDelete) {
        return isSubscriptionAvailable().toFlowable()
                .flatMap((Function<Boolean, Publisher<List<UserRecord>>>) aBoolean -> aBoolean
                        ? userModule.getNewUsers(addUpdateOrDelete) : stub.getNewUsers(addUpdateOrDelete));
    }

    @Override
    public Single<Result> logout() {
        return isSubscriptionAvailable().flatMap(
                aBoolean -> aBoolean ? userModule.logout() : stub.logout());
    }

    @Override
    public Single<Result> updateProfile(@NonNull String profileStatus, @NonNull String mediaPath,
                                        @NonNull String mediaType) {
        if (mediaPath == null || mediaPath.isEmpty()) {
            return canUpdateUserStatus().flatMap(
                    (Function<Boolean, SingleSource<Result>>) aBoolean -> aBoolean ? userModule.updateProfile(
                            profileStatus, mediaPath, mediaType)
                            : stub.updateProfile(profileStatus, mediaPath, mediaType));
        }
        return canUpdateProfile().flatMap(
                (Function<Boolean, SingleSource<Result>>) aBoolean -> aBoolean ? userModule.updateProfile(
                        profileStatus, mediaPath, mediaType)
                        : stub.updateProfile(profileStatus, mediaPath, mediaType));
    }

    @Override
    public Single<UserRecord> getProfile() {
        return isSubscriptionAvailable().flatMap(
                aBoolean -> aBoolean ? userModule.getProfile() : stub.getProfile());
    }

    @Override
    public Flowable<UserRecord> getLoggedInUser() {
        return isSubscriptionAvailable().toFlowable().flatMap(
            aBoolean -> aBoolean ? userModule.getLoggedInUser() : stub.getLoggedInUser());
    }

    @Override
    public String getUserAvailabilityStatus() {
        return userModule.getUserAvailabilityStatus();
    }

    @Override
    public Single setUserAvailability(AvailabilityStatus availabilityStatus) {
        return canGetAvailabilityStatus().flatMap(
                (Function<Boolean, SingleSource<?>>) aBoolean -> aBoolean ? userModule.setUserAvailability(
                        availabilityStatus) : stub.setUserAvailability(availabilityStatus));
    }

    @Override
    public Observable<UserRecord> subscribeToUserMetaData() {
        return userModule.subscribeToUserMetaData();
    }

    private Single<Boolean> canGetAvailabilityStatus() {
        return isFeatureEnabled(Constants.TenantConfig.UserProfile.AVAILABLE_STATUS);
    }

    @Override
    public Single<Result> removeProfilePic() {
        return canUpdateProfile().flatMap((Function<Boolean, SingleSource<Result>>) aBoolean -> aBoolean
                ? userModule.removeProfilePic() : stub.removeProfilePic());
    }

    @Override
    public Single<Result> deactivate() {
        return isSubscriptionAvailable().flatMap(
                aBoolean -> aBoolean ? userModule.deactivate() : stub.deactivate());
    }

    @Override
    public Observable<UserMetaDataRecord> metaDataOn(String appUserId) {
        return userModule.metaDataOn(appUserId);
    }

    @Override
    public Single<Result> fetchLatestUserStatus() {
        return canGetAvailabilityStatus().flatMap(
            (Function<Boolean, SingleSource<Result>>) aBoolean -> aBoolean
                ? userModule.fetchLatestUserStatus() : stub.fetchLatestUserStatus());
    }

    @Override
    public Observable<Result> subscribeToLogout() {
        return userModule.subscribeToLogout();
    }
}
