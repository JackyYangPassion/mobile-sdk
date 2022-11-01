package com.ripbull.coresdk.user.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import com.ripbull.ertc.cache.database.entity.User;
import com.ripbull.ertc.remote.model.response.UserResponse;

/** @author meeth */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class UserMapper {

  public static User from(
      @NonNull UserResponse userResponse, @NonNull String tenantId, @NonNull String loginType, String userMedia) {
    User user = new User(
        userResponse.getAppUserId(),
        tenantId,
        userResponse.getName(),
        userResponse.getAppState(),
        loginType,
        userResponse.getProfilePic(),
        userResponse.getProfilePicThumb(),
        userResponse.getProfileStatus(),
        userResponse.getLoginTimeStamp(),
        userResponse.getUserId());

    if (userResponse.getProfilePic() != null
        && userResponse.getProfilePic().length() > 0
        && !userResponse.getProfilePic().contains(userMedia)) {
      user.setProfilePic(userMedia + userResponse.getProfilePic());
      user.setProfileThumb(userMedia + userResponse.getProfilePicThumb());
    }
    return user;
  }

  public static User update(
          @NonNull User user, @NonNull UserResponse userResponse, @NonNull String loginType, String userMedia, boolean isRemoveProfilePic) {
    user.setName(userResponse.getName());
    user.setLoginType(loginType);
    if (!isRemoveProfilePic) user.setProfileStatus(userResponse.getProfileStatus());
    user.setLoginTimestamp(userResponse.getLoginTimeStamp());
    user.setUserChatId(userResponse.getUserId());

    if (userResponse.getProfilePic() != null
            && userResponse.getProfilePic().length() > 0
            && !userResponse.getProfilePic().contains(userMedia)) {
      user.setProfilePic(userMedia + userResponse.getProfilePic());
      user.setProfileThumb(userMedia + userResponse.getProfilePicThumb());
    } else {
      user.setProfilePic(null);
      user.setProfileThumb(null);
    }
    return user;
  }

  public static User transform(UserRecord userRecord) {
    User user =
        new User(
            userRecord.getId(),
            userRecord.getTenantId(),
            userRecord.getName(),
            userRecord.getAppState(),
            userRecord.getLoginType(),
            userRecord.getProfilePic(),
            userRecord.getProfileThumb(),
            userRecord.getProfileStatus(),
            userRecord.getLoginTimestamp(),
            userRecord.getUserChatId(),
            userRecord.getAvailabilityStatus(),
            userRecord.getBlockedStatus(),
            userRecord.getNotificationSettings());
    user.setErtcId(userRecord.getErtcId());
    user.setRole(userRecord.getRole());
    return user;
  }

  public static UserRecord transform(UserResponse userResponse, String userMedia) {
    UserRecord userRecord = new UserRecord();
    userRecord.setId(userResponse.getAppUserId());
    userRecord.setName(userResponse.getName());
    if (userResponse.getAppState() == null || userResponse.getAppState().isEmpty()) {
      userRecord.setAppState("active");
    } else {
      userRecord.setAppState(userResponse.getAppState());
    }

    if (userResponse.getProfilePic() != null
        && userResponse.getProfilePic().length() > 0
        && !userResponse.getProfilePic().contains(userMedia)) {
      userRecord.setProfilePic(userMedia + userResponse.getProfilePic());
      userRecord.setProfileThumb(userMedia + userResponse.getProfilePicThumb());
    }
    userRecord.setProfileStatus(userResponse.getProfileStatus());
    userRecord.setLoginTimestamp(userResponse.getLoginTimeStamp());
    userRecord.setUserChatId(userResponse.getERTCUserId());
    return userRecord;
  }

  public static UserRecord transform(User user) {

    return new UserRecord(
        user.getId(),
        user.getTenantId(),
        user.getName(),
        user.getAppState(),
        user.getLoginType(),
        user.getProfilePic(),
        user.getProfileThumb(),
        user.getProfileStatus(),
        user.getLoginTimestamp(),
        user.getUserChatId(),
        user.getRole(),
        user.getErtcId(),
        user.getAvailabilityStatus(),
        user.getBlockedStatus(),
        user.getNotificationSettings());
  }

  public static UserRecord transform(User user, String userMedia) {
    UserRecord userRecord = new UserRecord(
        user.getId(),
        user.getTenantId(),
        user.getName(),
        user.getAppState(),
        user.getLoginType(),
        user.getProfilePic(),
        user.getProfileThumb(),
        user.getProfileStatus(),
        user.getLoginTimestamp(),
        user.getUserChatId(),
        user.getRole(),
        user.getErtcId(),
        user.getAvailabilityStatus(),
        user.getBlockedStatus());

    if (user.getProfilePic() != null
        && user.getProfilePic().length() > 0
        && !user.getProfilePic().contains(userMedia)) {
      userRecord.setProfilePic(userMedia + user.getProfilePic());
      userRecord.setProfileThumb(userMedia + user.getProfileThumb());
    }

    return userRecord;
  }
}
