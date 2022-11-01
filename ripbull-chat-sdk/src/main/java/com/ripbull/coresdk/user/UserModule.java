package com.ripbull.coresdk.user;

import androidx.annotation.NonNull;
import com.ripbull.coresdk.core.type.AvailabilityStatus;
import com.ripbull.coresdk.core.type.ChatType;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord;
import com.ripbull.coresdk.user.mapper.UserRecord;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

/** @author meeth */
public interface UserModule {

  Flowable<List<UserRecord>> getChatUsers();

  Single<List<UserRecord>> getMentionedUsers();

  Single<List<UserRecord>> getReactionedUsers(List<String> reactionUnicodes, String msgId , String threadId, ChatType chatType);

  Flowable<UserRecord> getUserById(@NonNull String id);

  Single<Boolean> fetchMoreUsers();

  Flowable<List<UserRecord>> getNewUsers(String addUpdateOrDelete);

  Single<Result> logout();

  Single<Result> updateProfile( @NonNull String profileStatus, @NonNull String mediaPath, @NonNull String mediaType);

  Single<UserRecord> getProfile();

  Flowable<UserRecord> getLoggedInUser();

  String getUserAvailabilityStatus();

  Single setUserAvailability(AvailabilityStatus availabilityStatus);

  Observable<UserRecord> subscribeToUserMetaData();

  Single<Result> removeProfilePic();

  Single<Result> deactivate();

  Observable<UserMetaDataRecord> metaDataOn(String appUserId);

  Single<Result> fetchLatestUserStatus();

  Observable<Result> subscribeToLogout();
}