package io.inappchat.inappchat.user

import io.inappchat.inappchat.core.type.AvailabilityStatus
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/** @author meeth
 */
interface UserModule {
    val chatUsers: Flowable<List<UserRecord>>
    val mentionedUsers: Single<List<UserRecord>>
    fun getReactionedUsers(
        reactionUnicodes: List<String>,
        msgId: String,
        threadId: String,
        chatType: ChatType
    ): Single<List<UserRecord>>

    fun getUserById(id: String): Flowable<UserRecord>
    fun fetchMoreUsers(): Single<Boolean>
    fun getNewUsers(addUpdateOrDelete: String): Flowable<List<UserRecord>>
    fun logout(): Single<Result>
    fun updateProfile(profileStatus: String, mediaPath: String, mediaType: String): Single<Result>
    val profile: Single<UserRecord>
    val loggedInUser: Flowable<UserRecord>
    val userAvailabilityStatus: String
    fun setUserAvailability(availabilityStatus: AvailabilityStatus): Single<*>
    fun subscribeToUserMetaData(): Observable<UserRecord>
    fun removeProfilePic(): Single<Result>
    fun deactivate(): Single<Result>
    fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord>
    fun fetchLatestUserStatus(): Single<Result>
    fun subscribeToLogout(): Observable<Result>
}