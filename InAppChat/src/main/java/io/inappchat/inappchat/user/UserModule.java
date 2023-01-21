package io.inappchat.inappchat.user;

import androidx.annotation.NonNull;
import io.inappchat.inappchat.core.type.AvailabilityStatus;
import io.inappchat.inappchat.core.type.ChatType;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord;
import io.inappchat.inappchat.user.mapper.UserRecord;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
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