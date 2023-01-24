package io.inappchat.inappchat.user

import androidx.annotation.RestrictTo
import androidx.paging.*
import androidx.paging.rxjava3.flowable
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.AvailabilityStatus
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.module.BaseModule
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.user.repository.UserRepository
import io.inappchat.inappchat.user.repository.UserRepositoryImpl
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.Callable

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class UserModuleImpl private constructor(dataManager: DataManager, eventHandler: EventHandler) :
    BaseModule(dataManager), UserModule {
    private val userRepository: UserRepository

    init {
        userRepository = UserRepositoryImpl.newInstance(dataManager, eventHandler)
    }

    private fun config() =
        PagingConfig(20, 20, false)

    private fun <K : Any, V : Any> getPagedList(
        factory: () -> PagingSource<K, V>
    ) =
        Pager(
            config = config(),
            pagingSourceFactory = factory
        ).flowable

    val pagedUsers
        get() = this.withTenant<Flowable<PagingData<User>>>(
            { tenantId -> getPagedList { UserPagingSource(data(), userRepository) } },
            { Flowable.empty() })

    override val chatUsers: Flowable<List<UserRecord>>
        get() =
            userRepository.getChatUsers(tenantId)

    override val mentionedUsers: Single<List<UserRecord>>
        get() = userRepository.getMentionedUsers(tenantId)

    override val loggedInUser: Flowable<UserRecord>
        get() = userRepository.getLoggedInUser()

    override val profile: Single<UserRecord>
        get() = userRepository.getProfile(appUserId!!)

    override val userAvailabilityStatus: String
        get() = db().userDao().getUserStatus(tenantId, appUserId)

    override fun getReactionedUsers(
        reactionUnicodes: List<String>,
        msgId: String,
        threadId: String,
        chatType: ChatType
    ): Single<List<UserRecord>> {
        return userRepository.getReactionedUsers(reactionUnicodes, msgId, threadId, chatType)
    }

    override fun fetchMoreUsers(): Single<Boolean> {
        return withTenant(
            { tenantId ->
                Single.fromCallable(
                    Callable {
                        val lastUser: User = userRepository.getLastUserInSync(tenantId)
                        val lastUserId = if (lastUser != null) lastUser.id else null
                        val userList: List<User> =
                            userRepository.getUsersInSync(tenantId, lastUserId)
                        userRepository.saveUsersInSync(userList)
                        !userList.isEmpty()
                    })
            },
            { Single.just(false) })
    }

    override fun getNewUsers(addUpdateOrDelete: String): Flowable<List<UserRecord>> {
        return userRepository.getNewUsers(tenantId, addUpdateOrDelete)
    }

    override fun getUserById(id: String): Flowable<UserRecord> {
        return withTenant<Flowable<UserRecord>>({ tenantId ->
            userRepository.getUserById(
                tenantId,
                id
            )
        }, { Flowable.empty() })
    }

    override fun logout(): Single<Result> {
        return userRepository.logout()
    }

    override fun updateProfile(
        profileStatus: String, mediaPath: String,
        mediaType: String
    ): Single<Result> {
        return userRepository.updateProfile(
            tenantId,
            userId!!,
            profileStatus,
            mediaPath,
            mediaType
        )
    }

    override fun setUserAvailability(availabilityStatus: AvailabilityStatus): Single<*> {
        return userRepository.setUserAvailability(tenantId, availabilityStatus)
    }

    override fun subscribeToUserMetaData(): Observable<UserRecord> {
        return userRepository.subscribeToUserMetaData()
    }

    override fun removeProfilePic(): Single<Result> {
        return userRepository.removeProfilePic(tenantId, userId!!)
    }

    override fun deactivate(): Single<Result> {
        return userRepository.deactivate()
    }

    override fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord> {
        return userRepository.metaDataOn(appUserId)
    }

    override fun fetchLatestUserStatus(): Single<Result> {
        return userRepository.fetchLatestUserStatus()
    }

    override fun subscribeToLogout(): Observable<Result> {
        return userRepository.subscribeToLogout()
    }

    companion object {
        @JvmStatic
        fun newInstance(dataManager: DataManager, eventHandler: EventHandler): UserModule {
            return UserModuleImpl(dataManager, eventHandler)
        }
    }
}