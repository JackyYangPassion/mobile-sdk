package com.ripbull.coresdk.user;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import com.ripbull.coresdk.R;
import com.ripbull.coresdk.core.ChatSDKException;
import com.ripbull.coresdk.core.type.AvailabilityStatus;
import com.ripbull.coresdk.core.type.ChatType;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.eRTCSDK;
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord;
import com.ripbull.coresdk.user.mapper.UserRecord;
import com.ripbull.coresdk.utils.Constants;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

/** @author meeth */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class UserModuleStub implements UserModule {

  private Context appContext;

  public static UserModule newInstance() {
    return new UserModuleStub(eRTCSDK.getAppContext());
  }

  private UserModuleStub(Context context) {
    this.appContext = context;
  }

  @Override
  public Flowable<List<UserRecord>> getChatUsers() {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Single<List<UserRecord>> getMentionedUsers() {
    return Single.error(
            new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
                    appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Single<List<UserRecord>> getReactionedUsers(List<String> reactionUnicodes, String msgId , String threadId, ChatType chatType) {
    return Single.error(
            new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
                    appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Flowable<UserRecord> getUserById(@NonNull String id) {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Single<Boolean> fetchMoreUsers() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Flowable<List<UserRecord>> getNewUsers(String addUpdateOrDelete) {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Single<Result> logout() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.LOGOUT)));
  }

  @Override
  public Single<Result> updateProfile(@NonNull String profileStatus, @NonNull String mediaPath,
      @NonNull String mediaType) {
    if (mediaPath == null || mediaPath.isEmpty()) {
      return Single.error(
          new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
              appContext.getString(R.string.alert_message, Constants.Features.USER_PROFILE)));
    }
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_PROFILE_IMAGE)));
  }

  @Override
  public Single<UserRecord> getProfile() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Flowable<UserRecord> getLoggedInUser() {
    return Flowable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public String getUserAvailabilityStatus() {
    return (appContext.getString(R.string.alert_message, Constants.Features.AVAILABILITY_STATUS));
  }

  @Override
  public Single setUserAvailability(AvailabilityStatus availabilityStatus) {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.AVAILABILITY_STATUS)));
  }

  @Override
  public Observable<UserRecord> subscribeToUserMetaData() {
    return Observable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_MODULE)));
  }

  @Override
  public Single<Result> removeProfilePic() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.USER_PROFILE_IMAGE)));
  }

  @Override
  public Single<Result> deactivate() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.LOGOUT)));
  }

  @Override
  public Observable<UserMetaDataRecord> metaDataOn(String appUserId) {
    return Observable.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.MUTE_NOTIFICATIONS)));
  }

  @Override
  public Single<Result> fetchLatestUserStatus() {
    return Single.error(
        new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
            appContext.getString(R.string.alert_message, Constants.Features.AVAILABILITY_STATUS)));
  }

  @Override
  public Observable<Result> subscribeToLogout() {
    return null;
  }
}