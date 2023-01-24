package io.inappchat.inappchat.user.repository

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.core.type.AvailabilityStatus
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.cache.database.entity.User
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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
    ): List<User>?

    fun getLastUser(tenantId: String): Single<UserRecord>?
    fun getLastUserInSync(tenantId: String): User?
    fun saveUsersInSync(userList: MutableList<User>): Boolean?
    fun getUserById(
        tenantId: String,
        userId: String
    ): Flowable<UserRecord>?

    fun logout(): Single<Result>?
    fun updateProfile(
        tenantId: String,
        userId: String,
        profileStatus: String,
        mediaPath: String,
        mediaType: String
    ): Single<Result>?

    fun getProfile(appUserId: String): Single<UserRecord>?

    fun getLoggedInUser(): Flowable<UserRecord>?

    //fun getUserInfo(tenantId: String, appUserIds: List<String>?): Single<List<UserRecord>>?

    fun setUserAvailability(
        tenantId: String,
        availabilityStatus: AvailabilityStatus
    ): Single<Result>?

    fun subscribeToUserMetaData(): Observable<UserRecord>?

    fun removeProfilePic(
        tenantId: String,
        userId: String
    ): Single<Result>?

    fun deactivate(): Single<Result>?

    fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord>?

    fun fetchLatestUserStatus(): Single<Result>

    fun subscribeToLogout(): Observable<Result>
}