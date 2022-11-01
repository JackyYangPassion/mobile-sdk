package com.ripbull.coresdk.user.repository

import androidx.annotation.RestrictTo
import com.ripbull.coresdk.core.type.AvailabilityStatus
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.user.mapper.UserMetaDataRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.entity.User
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface UserRepository {
  //fun getLocalUsers(tenantId: String): Flowable<MutableList<UserRecord>>?
  //fun getRemoteUsers(tenantId: String): Single<MutableList<UserRecord>>?
  //fun getUpdatedUsers(tenantId: String, addUpdateOrDelete: String): Single<MutableList<UserRecord>>?

  fun getNewUsers(
    tenantId: String,
    addUpdateOrDelete: String
  ): Flowable<List<UserRecord>>?
  fun getChatUsers(tenantId: String): Flowable<List<UserRecord>>?
  fun getChatUsersRemote(tenantId: String): Single<List<UserRecord>>
  fun getMentionedUsers(tenantId: String): Single<List<UserRecord>>
  fun getReactionedUsers(
    reactionUnicodes: List<String>,
    msgId: String,
    threadId: String,
    chatType: ChatType
  ): Single<List<UserRecord>>

  fun getUsersInSync(
    tenantId: String,
    lastUserId: String?
  ): List<User?>?

  fun getLastUser(tenantId: String): Single<UserRecord>?
  fun getLastUserInSync(tenantId: String): User?
  fun saveUsersInSync(userList: MutableList<User>): Boolean?
  fun getUserById(
    tenantId: String,
    userId: String
  ): Flowable<UserRecord>?

  fun logout(): Single<Result?>?
  fun updateProfile(
    tenantId: String,
    userId: String,
    profileStatus: String,
    mediaPath: String,
    mediaType: String
  ): Single<Result?>?

  fun getProfile(appUserId: String): Single<UserRecord?>?

  fun getLoggedInUser(): Flowable<UserRecord?>?

  //fun getUserInfo(tenantId: String, appUserIds: List<String?>?): Single<List<UserRecord?>?>?

  fun setUserAvailability(
    tenantId: String,
    availabilityStatus: AvailabilityStatus
  ): Single<Result?>?

  fun subscribeToUserMetaData(): Observable<UserRecord>?

  fun removeProfilePic(
    tenantId: String,
    userId: String
  ): Single<Result?>?

  fun deactivate(): Single<Result?>?

  fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord?>?

  fun fetchLatestUserStatus(): Single<Result?>

  fun subscribeToLogout(): Observable<Result>
}