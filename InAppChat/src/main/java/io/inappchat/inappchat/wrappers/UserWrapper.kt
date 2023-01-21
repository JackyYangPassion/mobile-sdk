package io.inappchat.inappchat.wrappers

import com.google.gson.Gson
import io.inappchat.inappchat.core.type.AccountDetails
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.remote.model.response.UserSelfUpdateResponse
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserMetaDataRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.user.model.UserDBUpdate
import io.inappchat.inappchat.utils.Constants
import io.inappchat.inappchat.utils.transform
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.cache.database.entity.User
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage
import io.inappchat.inappchat.mqtt.model.UserMetaData
import io.inappchat.inappchat.remote.model.request.UserInfoRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.schedulers.Schedulers

/**
 * Created by DK on 12/06/20.
 */
class UserWrapper (private val dataManager: DataManager){
  private val gson: Gson = Gson()
  private val preferenceManager: PreferenceManager = dataManager.preference()

  fun userMetaDataUpdated(eventItem: UserSelfUpdateResponse.EventItem): Observable<UserMetaDataRecord> {
    return Observable.create { e: ObservableEmitter<UserMetaDataRecord> ->

      val userMetaDataRecord = UserMetaDataRecord(
        preferenceManager.appUserId,
        eventItem.eventData.notificationSettings?.allowFrom
      )

      // save Notification Settings into db
      dataManager.db().userDao().updateNotificationSettings(
        preferenceManager.chatUserId,
        eventItem.eventData.notificationSettings?.allowFrom
      )

      e.onNext(userMetaDataRecord)
    }.subscribeOn(Schedulers.io())
  }

  fun userMetaUpdated(receivedMessage: ReceivedMessage): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->
      val userMetaData: UserMetaData =
        gson.fromJson(receivedMessage.message, UserMetaData::class.java)

      val user = preferenceManager.tenantId?.let { it1 ->
        var userEntity = dataManager.db().userDao()
          .getUserByIdInSync(it1, userMetaData.appUserId)
        if(userEntity == null){
          userEntity = userMetaData.appUserId?.let {
            User(
              id = it,
              tenantId = preferenceManager.tenantId,
              name = "Third party user"
            )
          }
        }
        userEntity
      }
      user?.availabilityStatus = userMetaData.availabilityStatus

      dataManager.db().userDao().insertWithReplace(user)

      e.onNext(UserMapper.transform(user))
    }.subscribeOn(Schedulers.io())
  }

  fun userSelfAvailabilityUpdated(eventItem: UserSelfUpdateResponse.EventItem): Observable<UserMetaDataRecord> {
    return Observable.create { e: ObservableEmitter<UserMetaDataRecord> ->

      val userMetaDataRecord = UserMetaDataRecord(
        preferenceManager.appUserId,
        availabilityStatus = eventItem.eventData.availabilityStatus
      )

      // save Availability Status into db
      dataManager.db().userDao().updateAvailabilityStatus(
        preferenceManager.chatUserId,
        eventItem.eventData.availabilityStatus
      )

      e.onNext(userMetaDataRecord)
    }.subscribeOn(Schedulers.io())
  }

  fun userBlockStatusUpdated(eventItem: UserSelfUpdateResponse.EventItem): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->

      val user = preferenceManager.tenantId?.let { it1 ->
        var userEntity = dataManager.db().userDao()
          .getUserByIdInSync(it1, eventItem.eventData.targetUser?.appUserId)
        if(userEntity == null) {
          userEntity = eventItem.eventData.targetUser?.appUserId?.let {
            User(
              id = it,
              tenantId = preferenceManager.tenantId,
              name = "Third party user",
              userChatId = eventItem.eventData.targetUser?.eRTCUserId
            )
          }
        }
        userEntity
      }
      user?.blockedStatus = eventItem.eventData.blockedStatus

      dataManager.db().userDao().insertWithReplace(user)

      e.onNext(UserMapper.transform(user))
    }.subscribeOn(Schedulers.io())
  }

  fun userAddUpdated(userDBUpdate: UserDBUpdate): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->
      var lastCallTime = preferenceManager.lastCallTimeUser
      if (lastCallTime == null) {
        lastCallTime = "-1"
      }

      preferenceManager.userId?.let { userId ->
        preferenceManager.tenantId?.let { tenantId ->
          dataManager.network()
            .api()
            .getUpdatedUsers(userId, tenantId, userDBUpdate.event, lastCallTime)
            .map { (userList) ->
              preferenceManager.lastCallTimeUser = "" + System.currentTimeMillis() / 1000
              val list = mutableListOf<String>()
              for (user in userList) {
                list.add(user.appUserId)
              }
              val userListRecord =
                userList.transform { response ->
                  UserMapper.transform(response, preferenceManager.userServer)
                }

              if (list.isEmpty()) {
                //If list is empty
              } else {
                dataManager.network()
                  .api()
                  .getUserInfo(tenantId, preferenceManager.chatUserId!!, UserInfoRequest(list))
                  .map { (userList) ->
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
                            if (e2eKey.deviceId != preferenceManager.deviceId) {
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
                    saveUsersInSync(finalUserListRecord.transform { userRecord: UserRecord? ->
                      UserMapper.transform(userRecord)
                    })
                  }.subscribeOn(Schedulers.io()).subscribe()
              }
            }.subscribeOn(Schedulers.io()).subscribe()
        }
      }
    }.subscribeOn(Schedulers.io())
  }

  fun userDeleted(userDBUpdate: UserDBUpdate): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->
      for (appUserId in userDBUpdate.appUserIds) {
        dataManager.db().userDao().deleteUser(appUserId)
        dataManager.db().threadDao().getThreadIdByUserId(appUserId)?.let { threadId ->
          dataManager.db().singleChatDao().deleteByThreadId(threadId)
        }
        dataManager.db().threadDao().deleteThreadByUserId(appUserId)
        dataManager.db().threadUserLinkDao().deleteThreadByUserId(appUserId)
      }
    }.subscribeOn(Schedulers.io())
  }

  fun userInactivated(userDBUpdate: UserDBUpdate): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->
      var lastCallTime = preferenceManager.lastCallTimeUser
      if (lastCallTime == null) {
        lastCallTime = "-1"
      }

      preferenceManager.userId?.let { userId ->
        preferenceManager.tenantId?.let { tenantId ->
          dataManager.network()
            .api()
            .getUpdatedUsers(userId, tenantId, userDBUpdate.event, lastCallTime)
            .map { (userList) ->
              val loginType = AccountDetails.Type.EMAIL.value
              val userList =
                userList.transform { response ->
                  UserMapper.from(response, tenantId, loginType, dataManager.preference().userServer)
                }
              val isUpdated = saveUsersInSync(userList)
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
        }
      }
    }.subscribeOn(Schedulers.io())
  }

  fun userBlockStatusUpdatedBySelf(user: User): Observable<UserRecord> {
    return Observable.create { e: ObservableEmitter<UserRecord> ->
      e.onNext(UserMapper.transform(user))
    }.subscribeOn(Schedulers.io())
  }

  private fun saveUsersInSync(userList: MutableList<User>): Boolean {
    dataManager.db().userDao().insertWithReplace(userList)
    return true
  }

  private fun isE2EFeatureEnabled(): Boolean {
    return (dataManager.db().tenantDao()
      .getFeatureEnabled(dataManager.preference().tenantId, Constants.TenantConfig.E2E_CHAT) == "1")
  }
}