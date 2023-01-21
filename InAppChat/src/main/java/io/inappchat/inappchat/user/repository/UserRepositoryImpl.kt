package io.inappchat.inappchat.user.repository

import androidx.annotation.RestrictTo
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import io.inappchat.inappchat.cache.database.dao.UserDao
import io.inappchat.inappchat.cache.database.entity.ChatReactionWithUsers
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.remote.model.common.Response
import io.inappchat.inappchat.remote.model.request.Logout
import io.inappchat.inappchat.remote.model.request.UpdateUserRequest
import io.inappchat.inappchat.remote.model.request.UserInfoRequest
import io.inappchat.inappchat.remote.model.response.UserListResponse
import io.inappchat.inappchat.remote.model.response.UserResponse
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.AccountDetails
import io.inappchat.inappchat.core.type.AvailabilityStatus
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.EventType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.eRTCSDK
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.utils.Constants
import io.inappchat.inappchat.utils.Utils
import io.inappchat.inappchat.utils.transform
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.mqtt.model.UserMetaData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.create
import io.reactivex.rxjava3.core.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class UserRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val eventHandler: EventHandler,
  private val preference: PreferenceManager = dataManager.preference(),
  private val userDao: UserDao = dataManager.db().userDao(),
  private val gson: Gson = Gson()
) :
  BaseRepository(dataManager), UserRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler
    ): UserRepository = UserRepositoryImpl(dataManager, eventHandler)
  }

  private fun getLocalUsers(tenantId: String): Flowable<MutableList<UserRecord>>? {
    return userDao.getUsers(tenantId, appUserId)
      .flatMap { users: List<User> ->
        val transform =
          users.transform { user: User ->
            UserMapper.transform(
              User(
                user.id, tenantId, user.name, user.appState,
                AccountDetails.Type.EMAIL.value, user.profilePic,
                user.profileThumb, user.profileStatus, user.loginTimestamp,
                user.userChatId, user.availabilityStatus, user.blockedStatus
              )
            )
          }
        Flowable.just(transform)
      }
  }

  private fun getRemoteUsers(tenantId: String): Single<List<UserRecord>> {

    return dataManager.network().api().getUsers(userId, tenantId, null).flatMap { (userList) ->
      val list = mutableListOf<String>()
      if (userList.isNullOrEmpty()) {
        return@flatMap Single.just(mutableListOf<UserRecord>())
      }
      for (user in userList) {
        if (user.appUserId != preference.appUserId) {
          list.add(user.appUserId)
        }
      }
      //list.add(preference.appUserId!!)
      val userListRecord =
        userList.transform { response ->
          UserMapper.transform(response, preference.userServer)
        }

      if (list.isEmpty()) {
        Single.just(mutableListOf<UserRecord>())
      } else {
        //getUserAdditionalInfo(tenantId,list,userListRecord);

        val flatMap = dataManager.network()
          .api()
          .getUserInfo(tenantId, preference.chatUserId!!, UserInfoRequest(list))
          .flatMap { (userList) ->
            val finalUserListRecord = mutableListOf<UserRecord>()
            for (userRecord in userListRecord) {
              if (userList != null) {
                for (userResponse in userList) {
                  if (userRecord.id == userResponse.appUserId) {
                    val finalUserRecord = userRecord.copy(
                      ertcId = userResponse.eRTCUserId,
                      userChatId = userResponse.eRTCUserId,
                      blockedStatus = userResponse.statusDetails?.blockedStatus,
                      availabilityStatus = userResponse.statusDetails?.availabilityStatus,
                      tenantId = tenantId
                    )
                    finalUserListRecord.add(finalUserRecord)
                    break
                  }

                }
              }
            }

            // save e2e keys when e2e feature is enabled
            if (userList != null && isE2EFeatureEnabled()) {
              for (userResponse in userList) {
                if (!userResponse.e2eEncryptionKeys.isNullOrEmpty()) {
                  for (e2eKey in userResponse.e2eEncryptionKeys!!) {
                    if (e2eKey.deviceId != preference.deviceId) {
                      //update Remaining keys
                      val updatedRow: Int = dataManager.db().ekeyDao().updateKey(
                        e2eKey.eRTCUserId, e2eKey.publicKey, e2eKey.keyId, e2eKey.deviceId, System.currentTimeMillis()
                      )
                      if (updatedRow == 0) {
                        val eKeyTableUpdated = EKeyTable(
                          keyId = e2eKey.keyId,
                          deviceId = e2eKey.deviceId,
                          publicKey = e2eKey.publicKey,
                          privateKey = "",
                          ertcUserId = e2eKey.eRTCUserId,
                          tenantId = tenantId
                        )
                        dataManager.db().ekeyDao().save(eKeyTableUpdated)
                      }
                    }
                  }
                }
              }
            }
            Single.just(finalUserListRecord)
          }
        flatMap
      }

    }

  }

  private fun getUpdatedUsers(
    tenantId: String,
    addUpdateOrDelete: String
  ): Single<MutableList<UserRecord>> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    var lastCallTime = dataManager.preference().lastCallTimeUser
    if (lastCallTime == null) {
      lastCallTime = "-1"
    }
    return dataManager.network()
      .api()
      .getUpdatedUsers(userId, tenantId, addUpdateOrDelete, lastCallTime)
      .flatMap { (userList) ->
        dataManager.preference().lastCallTimeUser = "" + System.currentTimeMillis() / 1000

        val list = mutableListOf<String>()
        for (user in userList) {
          list.add(user.appUserId)
        }
        val userListRecord =
          userList.transform { response ->
            UserMapper.transform(response, preference.userServer)
          }

        if (list.isEmpty()) {
          Single.just(mutableListOf<UserRecord>())
        } else {
          val flatMap = dataManager.network()
            .api()
            .getUserInfo(tenantId, preference.chatUserId!!, UserInfoRequest(list))
            .flatMap { (userList) ->
              val finalUserListRecord = mutableListOf<UserRecord>()
              for (userRecord in userListRecord) {
                if (userList != null) {
                  for (userResponse in userList) {
                    if (userRecord.id == userResponse.appUserId) {
                      val finalUserRecord = userRecord.copy(
                        ertcId = userResponse.eRTCUserId,
                        userChatId = userResponse.eRTCUserId,
                        blockedStatus = userResponse.statusDetails?.blockedStatus,
                        availabilityStatus = userResponse.statusDetails?.availabilityStatus,
                        tenantId = tenantId
                      )
                      finalUserListRecord.add(finalUserRecord)
                      break
                    }

                  }
                }
              }

              // save e2e keys when e2e feature is enabled
              if (userList != null && isE2EFeatureEnabled()) {
                for (userResponse in userList) {
                  if (!userResponse.e2eEncryptionKeys.isNullOrEmpty()) {
                    for (e2eKey in userResponse.e2eEncryptionKeys!!) {
                      if (e2eKey.deviceId != preference.deviceId) {
                        //update Remaining keys
                        val updatedRow: Int = dataManager.db().ekeyDao().updateKey(
                          e2eKey.eRTCUserId, e2eKey.publicKey, e2eKey.keyId, e2eKey.deviceId, System.currentTimeMillis()
                        )
                        if (updatedRow == 0) {
                          val eKeyTableUpdated = EKeyTable(
                            keyId = e2eKey.keyId,
                            deviceId = e2eKey.deviceId,
                            publicKey = e2eKey.publicKey,
                            privateKey = "",
                            ertcUserId = e2eKey.eRTCUserId,
                            tenantId = tenantId
                          )
                          dataManager.db().ekeyDao().save(eKeyTableUpdated)
                        }
                      }
                    }
                  }
                }
              }
              Single.just(finalUserListRecord)
            }
          flatMap
        }
      }
  }

  override fun getNewUsers(
    tenantId: String,
    addUpdateOrDelete: String
  ): Flowable<List<UserRecord>>? {
    val localSingle =
      getLocalUsers(tenantId)
    val remoteSingle =
      getUpdatedUsers(tenantId, addUpdateOrDelete).map { users: List<UserRecord> ->
        when {
          "addUpdated".equals(addUpdateOrDelete, ignoreCase = true) -> {
            saveUsersInSync(users.transform { userRecord: UserRecord? ->
              UserMapper.transform(userRecord)
            })
            for ((id) in users) {
              id?.let {
                dataManager.db().threadDao().updateUserUpdatedAt(it, System.currentTimeMillis())
              }
            }
          }
          "inactive" == addUpdateOrDelete                           -> {
            saveUsersInSync(users.transform { userRecord: UserRecord? ->
              UserMapper.transform(userRecord)
            })
            for ((id) in users) {
              id?.let {
                dataManager.db().threadDao().updateUserUpdatedAt(it, System.currentTimeMillis())
              }
            }
          }
          else                                                      -> {
            for ((id) in users) {
              id?.let {
                userDao.deleteUser(it)
                dataManager.db().threadDao().getThreadIdByUserId(it)?.let { threadId ->
                  dataManager.db().singleChatDao().deleteByThreadId(threadId)
                }
                dataManager.db().threadDao().deleteThreadByUserId(it)
                dataManager.db().threadUserLinkDao().deleteThreadByUserId(it)
              }
            }
          }
        }
        users
      }.toFlowable().subscribeOn(Schedulers.io())
    return Flowable.merge(
      localSingle,
      remoteSingle
    )
  }

  override fun getChatUsers(tenantId: String): Flowable<List<UserRecord>>? {
    return getLocalUsers(tenantId)?.subscribeOn(Schedulers.computation())?.flatMap {
      if (it.size <= 0) {
        getRemoteUsers(tenantId).flatMapPublisher { users: List<UserRecord> ->
          saveUsersInSync(users.transform { userRecord: UserRecord? ->
            UserMapper.transform(
              userRecord
            )
          })
          Flowable.just(users)
        }
      } else {
        Flowable.just(it)
      }
    }
  }

  override fun getChatUsersRemote(tenantId: String): Single<List<UserRecord>> {
    return getRemoteUsers(tenantId).flatMap { users ->
      saveUsersInSync(users.transform { userRecord: UserRecord? ->
        UserMapper.transform(
          userRecord
        )
      })
      Single.just(users)
    }
  }

  override fun getMentionedUsers(tenantId: String): Single<List<UserRecord>> {
    return userDao.getUsersBySingle(tenantId, appUserId)
      .flatMap { users: List<User> ->
        val transform =
          users.transform { user: User ->
            UserMapper.transform(
              User(
                user.id, tenantId, user.name
              )
            )
          }
        Single.just(transform)
      }
  }

  override fun getReactionedUsers(reactionUnicodes: List<String>, msgId : String, threadId : String, chatType: ChatType): Single<List<UserRecord>> {
    // query array of user chat id and query user from array of ids
    val chatReactionDao = dataManager.db().chatReactionDao()
    val chatReactionWithUsersSingle =
      if (chatType == ChatType.SINGLE_CHAT_THREAD || chatType == ChatType.GROUP_CHAT_THREAD) {
        chatReactionDao.getReactionedUsersOnThread(reactionUnicodes.toTypedArray(), msgId, threadId)
      } else {
        chatReactionDao.getReactionedUsersOnChat(reactionUnicodes.toTypedArray(), msgId, threadId)
      }
    return chatReactionWithUsersSingle.flatMap { chatReactionWithUsers: List<ChatReactionWithUsers> ->
      val arrayListOf = arrayListOf<UserRecord>()
      chatReactionWithUsers.forEach { chatReactionWithUser ->
        chatReactionWithUser.users?.transform { user: User ->
          UserMapper.transform(
            User(
              user.id, tenantId, user.name
            )
          )
        }?.let { arrayListOf.addAll(it) }
      }
      Single.just(arrayListOf)
    }
  }

  override fun getUsersInSync(
    tenantId: String,
    lastUserId: String?
  ): List<User> {
    val result: List<User> =
      ArrayList()
    val call: Call<UserListResponse?> =
      dataManager.network().api().getUsersInSync(tenantId, lastUserId)
    try {
      val response = call.execute()
      if (response != null && response.isSuccessful) {
        if (response.body() != null) {
          val body = response.body()
          val userList = body!!.userList
          val loginType = AccountDetails.Type.EMAIL.value
          return userList.transform { userResponse: UserResponse? ->
            UserMapper.from(
                userResponse!!, tenantId, loginType,
                dataManager.preference().userServer
            )
          }
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return result
  }

  override fun getLastUser(tenantId: String): Single<UserRecord> {
    return userDao.getLastUser(tenantId)
      .flatMap { user: User? ->
        Single.just(
          UserMapper.transform(user)
        )
      }
  }

  override fun getLastUserInSync(tenantId: String): User? {
    return userDao.getLastUserInSync(tenantId)
  }

  override fun saveUsersInSync(userList: MutableList<User>): Boolean? {
    userDao.insertWithReplace(userList)
    return true
  }

  override fun getUserById(
    tenantId: String,
    userId: String
  ): Flowable<UserRecord> {
    return userDao.getUserByIdFlowable(tenantId, userId)
      .flatMap { user: User? ->
        Flowable.just(
          UserMapper.transform(user)
        )
      }
  }

  override fun logout(): Single<Result?>? {
    val tenantId =
      Objects.requireNonNull(dataManager.preference().tenantId)
    val userId =
      Objects.requireNonNull(dataManager.preference().userId)
    val appUserId =
      Objects.requireNonNull(dataManager.preference().appUserId)
    val deviceId =
      Objects.requireNonNull(dataManager.preference().deviceId)
    val chatUserId =
      Objects.requireNonNull(dataManager.preference().chatUserId)
    val request = Logout(appUserId!!, deviceId!!)
    return Single.zip(
      dataManager.network().api().chatLogout(tenantId!!, chatUserId!!, request),
      dataManager.network().api().userLogout(tenantId, userId!!, request),
      BiFunction { _: Response?, _: Response? ->
        dataManager.db().tenantDao().deleteAll()
        dataManager.db().threadDao().deleteAll()
        dataManager.db().singleChatDao().deleteAll()
        dataManager.db().groupDao().deleteAll()
        dataManager.db().ekeyDao().deleteAll()
        userDao.deleteAll()
        dataManager.mqtt().removeConnectionAndSubscription()
        FirebaseInstanceId.getInstance().deleteInstanceId()
        eRTCSDK.fcm().clearNotification()
        dataManager.preference().clearData()
        dataManager.db().downloadMediaDao().clear()
        Result(true, "User logged out successfully", "")
      }
    )
  }

  override fun updateProfile(
    tenantId: String, userId: String,
    profileStatus: String, mediaPath: String, mediaType: String
  ): Single<Result?>? {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .updateProfile(
        tenantId,
        userId,
        mediaPath,
        mediaType,
        Objects.requireNonNull(data().preference().loginType)!!,
        profileStatus,
        Utils.getMimeType(mediaPath, mediaType)
      )
      .flatMap { response: UserResponse? ->
        val userEntity =
          userDao.getUserByIdInSync(tenantId, preference.appUserId)

        val updatedUserEntity = UserMapper.update(
          userEntity, response!!, dataManager.preference().loginType!!,
          dataManager.preference().userServer, false
        )
        dataManager.db().userDao().insertWithReplace(updatedUserEntity)

        Single.just(
          Result(
            true,
            "Profile updated!",
            ""
          )
        )
      }
  }

  override fun getProfile(appUserId: String): Single<UserRecord?>? {
    return dataManager.network()
      .api()
      .getProfile(appUserId)
      .flatMap { userResponse: UserResponse? ->
        Single.just(
          UserMapper.transform(userResponse, dataManager.preference().userServer)
        )
      }
  }

  override fun getLoggedInUser(): Flowable<UserRecord?>? {
    return userDao.getUserByIdFlowable(tenantId, appUserId)
      .flatMap { user: User? ->
        Flowable.just(
          UserMapper.transform(user)
        )
      }
  }

  override fun setUserAvailability(
    tenantId: String, availabilityStatus: AvailabilityStatus
  ): Single<Result?>? {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .updateUserInfo(
        tenantId, preference.chatUserId!!,
        UpdateUserRequest(availabilityStatus = availabilityStatus.status)
      )
      .flatMap {
        val userDao = userDao
        val userStatus =
          userDao.getUserByIdInSync(tenantId, preference.appUserId)
        userStatus.availabilityStatus = availabilityStatus.status
        userDao.update(userStatus)
        Single.just(
          Result(
            true,
            "Success",
            ""
          )
        )
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  override fun subscribeToUserMetaData(): Observable<UserRecord>? {
    return eventHandler.source().filter(NetworkEvent.filterType(EventType.USER_META_DATA_UPDATED))
      .flatMap { networkEvent ->
      create<UserRecord> {
        if (networkEvent.message() != null) {
          val (_, appUserId, availabilityStatus) = gson.fromJson(
            networkEvent.message().message,
            UserMetaData::class.java
          )
          val user =
            userDao.getUserByIdInSync(tenantId, appUserId)
          user.availabilityStatus = availabilityStatus
          it.onNext(UserMapper.transform(user))
        } else if (networkEvent.userRecord() != null) {
          it.onNext(networkEvent.userRecord())
        } else {
          val user =
            userDao.getUserByIdInSync(tenantId, appUserId)
          user.blockedStatus = networkEvent.userRecord().blockedStatus
          it.onNext(UserMapper.transform(user))
        }
      }.subscribeOn(Schedulers.single())
    }
  }

  override fun removeProfilePic(
    tenantId: String, userId: String
  ): Single<Result?>? {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
        .api()
        .removeProfilePic(
          tenantId,
          userId
        )
        .flatMap { response: UserResponse? ->
          val userEntity =
            userDao.getUserByIdInSync(tenantId, preference.appUserId)

          val updatedUserEntity = UserMapper.update(
            userEntity, response!!, dataManager.preference().loginType!!,
            dataManager.preference().userServer, true
          )
          dataManager.db().userDao().update(updatedUserEntity)

          Single.just(
            Result(
              true,
              "Profile picture removed!",
              ""
            )
          )
        }
  }

  override fun deactivate(): Single<Result?>? {
    dataManager.db().tenantDao().deleteAll()
    dataManager.db().threadDao().deleteAll()
    dataManager.db().singleChatDao().deleteAll()
    dataManager.db().groupDao().deleteAll()
    dataManager.db().ekeyDao().deleteAll()
    userDao.deleteAll()
    dataManager.mqtt().removeConnectionAndSubscription()
    FirebaseInstanceId.getInstance().deleteInstanceId()
    eRTCSDK.fcm().clearNotification()
    dataManager.preference().clearData()
    dataManager.db().downloadMediaDao().clear()
    return Single.just(Result(true, "User are forcefully logged out", ""))
  }

  override fun metaDataOn(appUserId: String): Observable<UserMetaDataRecord?>? {
    return eventHandler.source()
      .filter(NetworkEvent.filterType(EventType.USER_META_DATA_UPDATED))
      .filter{ it.userMetaDataRecord() != null }
      .filter{ appUserId == it.userMetaDataRecord().appUserId }
      .flatMap { networkEvent ->
        Observable.create<UserMetaDataRecord> {
          it.onNext(networkEvent.userMetaDataRecord())
        }
      }.subscribeOn(Schedulers.single())
  }

  override fun fetchLatestUserStatus(): Single<Result?> {
    return userDao.getUserIds(appUserId)
      .flatMap { users: List<String> ->

        if (users.isEmpty()) {
          Single.just(mutableListOf<UserRecord>())
        } else {
          val response = dataManager.network().api()
            .getUserInfo(tenantId, preference.chatUserId!!, UserInfoRequest(users, arrayListOf("availabilityStatus"))).blockingGet()

          response?.let { response ->
            if (response.userList != null) {
              for (user in response.userList!!) {
                userDao.updateAvailabilityStatusById(user.appUserId, user.statusDetails?.availabilityStatus)
              }
            }
          }
        }
        Single.just(Result(true, "User are forcefully logged out", ""))
      }
  }

  override fun subscribeToLogout(): Observable<Result> {
    return eventHandler.source().filter(NetworkEvent.filterType(EventType.LOGOUT))
      .flatMap { networkEvent ->
        Observable.just(networkEvent.Result())
      }
  }

  private fun isE2EFeatureEnabled(): Boolean {
    return (dataManager.db().tenantDao().getFeatureEnabled(dataManager.preference().tenantId, Constants.TenantConfig.E2E_CHAT) == "1")
  }

  private fun noInternetConnection(): Boolean {
    return try {
      // Connect to Google DNS to check for connection
      val timeoutMs = 1500
      val socket = Socket()
      val socketAddress = InetSocketAddress("8.8.8.8", 53)

      socket.connect(socketAddress, timeoutMs)
      socket.close()

      false
    } catch (ex: IOException) {
      true
    }
  }
}