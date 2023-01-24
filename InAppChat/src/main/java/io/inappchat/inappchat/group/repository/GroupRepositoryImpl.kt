package io.inappchat.inappchat.group.repository

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.cache.database.dao.GroupDao
import io.inappchat.inappchat.cache.database.entity.Group
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.remote.model.response.GroupResponse
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.EventType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupMapper.transform
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.thread.mapper.ThreadMapper
import io.inappchat.inappchat.utils.Constants
import io.inappchat.inappchat.utils.Utils
import io.inappchat.inappchat.utils.transform
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.collections.ArrayList

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class GroupRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val eventHandler: EventHandler
) :
  BaseRepository(dataManager), GroupRepository {
  private val groupDao: GroupDao = dataManager.db().groupDao()

  private fun getRemoteGroups(tenantId: String): Single<List<GroupRecord>> {
    return dataManager.network().api().getGroups(tenantId, chatUserId)
      .map { (groups) ->
        if (groups.isNullOrEmpty()) {
          ArrayList<GroupRecord>()
        } else {
          val transform =
            groups.transform { group: GroupResponse? ->
              transform(
                group,
                getTenantId(),
                dataManager.db(),
                dataManager.preference().chatServer!!,
                dataManager.preference().appUserId,
                dataManager.preference().chatUserId
              )
            }
          transform
        }
      }
  }

  private fun getLocalGroups(tenantId: String): Flowable<List<GroupRecord>> {
    return groupDao.getAllGroup(tenantId)
      .flatMap { groups: List<Group> ->
        val transform =
          groups.transform { group: Group? ->
            transform(
              group!!,
              getTenantId(),
              dataManager.preference().appUserId
            )
          }
        Flowable.just(transform)
      }
  }

  override fun getGroups(tenantId: String): Flowable<List<GroupRecord>>? {
    return getLocalGroups(tenantId).subscribeOn(Schedulers.computation())?.flatMap {
      if (it.isEmpty()) {
        getRemoteGroups(tenantId).flatMapPublisher { groups: List<GroupRecord> ->
          saveGroupsInSync(groups.transform { groupRecord: GroupRecord? ->
            transform(groupRecord)
          })
          Flowable.just(groups)
        }
      } else {
        Flowable.just(it)
      }
    }
  }

  override fun saveGroupsInSync(groupList: List<Group>): Boolean {
    groupDao.insertWithIgnore(groupList)
    saveGroupsThread(groupList)
    return true
  }

  override fun createPrivateGroup(group: GroupRecord): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    val participants: MutableList<String> = ArrayList()
    for ((id) in group.groupUsers) {
      id?.let { participants.add(it) }
    }
    return dataManager.network()
      .api()
      .createGroup(
        tenantId, chatUserId, group.name, GroupRecord.TYPE_PRIVATE,
        group.groupDesc, participants, group.groupPic,
        Utils.getMimeType(group.groupPic,"image")
      )
      .map { response: GroupResponse ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        saveGroupThread(response)

        // save e2e keys when e2e feature is enabled
        if (response.participants != null && isE2EFeatureEnabled()) {
          for (participant in response.participants) {
            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
              val eKeyDao = dataManager.db().ekeyDao()
              for (e2eKey in participant.e2eEncryptionKeys!!) {
                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                } else {
                  //update Remaining keys
                  val updatedRow: Int = eKeyDao.updateKey(
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
                    eKeyDao.save(eKeyTableUpdated)
                  }
                }
              }
            }
          }
        }

        groupRecord
      }
  }

  override fun updateGroupDetail(
    groupId: String, groupName: String, groupDesc: String,
    groupImgPath: String
  ): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .updateGroup(tenantId, chatUserId, groupId, groupName, groupDesc, groupImgPath,Utils.getMimeType(groupImgPath,"image"))
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        groupRecord
      }
  }

  override fun removeParticipants(
    groupId: String,
    users: List<String>
  ): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .removeGroupParticipants(tenantId, chatUserId, groupId, users)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        groupRecord
      }
  }

  override fun addParticipants(
    groupId: String,
    users: List<String>
  ): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .addGroupParticipants(tenantId, chatUserId, groupId, users)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))

        // save e2e keys when e2e feature is enabled
        if (response?.participants != null && isE2EFeatureEnabled()) {
          for (participant in response.participants) {
            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
              val eKeyDao = dataManager.db().ekeyDao()
              for (e2eKey in participant.e2eEncryptionKeys!!) {
                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                } else {
                  //update Remaining keys
                  val updatedRow: Int = eKeyDao.updateKey(
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
                    eKeyDao.save(eKeyTableUpdated)
                  }
                }
              }
            }
          }
        }
        groupRecord
      }
  }

  override fun getGroupById(groupId: String): Flowable<GroupRecord>? {
    return getLocalGroupById(groupId).subscribeOn(Schedulers.computation())?.flatMap {
      if (it.groupUsers.isEmpty()) {
        getRemoteGroupById(groupId).flatMapPublisher { group: GroupRecord ->
          Flowable.just(group)
        }
      } else {
        Flowable.just(it)
      }
    }
  }

  private fun getLocalGroupById(groupId: String): Flowable<GroupRecord> {
    return dataManager.db().groupDao().getGroupById(tenantId, groupId).flatMap { group: Group ->
      val groupRecord: GroupRecord = transform(group, tenantId, dataManager.preference().appUserId)
      Flowable.just(groupRecord)
    }
  }

  private fun getRemoteGroupById(groupId: String): Single<GroupRecord> {
    return dataManager.network()
      .api()
      .getGroup(tenantId, chatUserId, groupId)
      .map { response: GroupResponse ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        saveGroupThread(response)

        // save e2e keys when e2e feature is enabled
        if (response.participants != null && isE2EFeatureEnabled()) {
          for (participant in response.participants) {
            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
              val eKeyDao = dataManager.db().ekeyDao()
              for (e2eKey in participant.e2eEncryptionKeys!!) {
                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                } else {
                  //update Remaining keys
                  val updatedRow: Int = eKeyDao.updateKey(
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
                    eKeyDao.save(eKeyTableUpdated)
                  }
                }
              }
            }
          }
        }
        groupRecord
      }
  }

  override fun getGroupByThreadId(threadId: String): Single<GroupRecord> {
    return dataManager.network()
      .api()
      .getGroupByThreadId(tenantId, chatUserId, threadId)
      .map { response: GroupResponse ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        saveGroupThread(response)

        // save e2e keys when e2e feature is enabled
        if (response.participants != null && isE2EFeatureEnabled()) {
          for (participant in response.participants) {
            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
              val eKeyDao = dataManager.db().ekeyDao()
              for (e2eKey in participant.e2eEncryptionKeys!!) {
                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                } else {
                  //update Remaining keys
                  val updatedRow: Int = eKeyDao.updateKey(
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
                    eKeyDao.save(eKeyTableUpdated)
                  }
                }
              }
            }
          }
        }
        groupRecord
      }
  }

  override fun addAdmin(
    groupId: String,
    user: String
  ): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .adminChanges(tenantId, chatUserId, groupId, "make", user)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        groupRecord
      }
  }

  override fun removeAdmin(
    groupId: String,
    user: String
  ): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .adminChanges(tenantId, chatUserId, groupId, "dismiss", user)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        groupRecord
      }
  }

  override fun exitGroup(groupId: String): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    val participants: MutableList<String> = ArrayList()
    participants.add(appUserId)
    return dataManager.network()
      .api()
      .removeGroupParticipants(tenantId, chatUserId, groupId, participants)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
//        dataManager.db().groupDao().deleteByGroupId(tenantId, groupId)
//        dataManager.db().threadDao().deleteThreadById(groupRecord.threadId)
//        dataManager.db().singleChatDao().deleteByThreadId(groupRecord.threadId)
        groupRecord
      }
  }

  private fun saveGroupThread(groupResponse: GroupResponse) {
    val thread =
      ThreadMapper.fromGroupResponse(groupResponse, appUserId, chatUserId)
    dataManager.db().threadDao().insertWithIgnore(thread)
    dataManager.db()
      .threadUserLinkDao()
      .insertWithIgnore(
        ThreadMapper.from(
          appUserId, groupResponse.groupId,
          groupResponse.threadId
        )
      )
  }

  override fun removeGroupPic(groupId: String): Single<Result> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network()
      .api()
      .removeGroupPic(tenantId, chatUserId, groupId)
      .map { response: GroupResponse? ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        Result(true, "Group picture removed!", "")
      }
  }

  override fun subscribeToGroupUpdate(): Observable<GroupRecord> {
    return eventHandler.source().filter(NetworkEvent.filterType(EventType.GROUP_UPDATED))
      .flatMap { networkEvent ->
        Observable.just(networkEvent.groupRecord())
      }
  }

  override fun createPublicGroup(group: GroupRecord): Single<GroupRecord> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    val participants: MutableList<String> = ArrayList()
    for ((id) in group.groupUsers) {
      id?.let { participants.add(it) }
    }
    return dataManager.network()
      .api()
      .createGroup(
        tenantId, chatUserId, group.name, GroupRecord.TYPE_PUBLIC,
        group.groupDesc, participants, group.groupPic,
        Utils.getMimeType(group.groupPic,"image")
      )
      .map { response: GroupResponse ->
        val groupRecord = transform(
          response,
          tenantId,
          dataManager.db(),
          dataManager.preference().chatServer!!,
          dataManager.preference().appUserId,
          dataManager.preference().chatUserId
        )
        // save group into db
        dataManager.db().groupDao().insertWithReplace(transform(groupRecord))
        saveGroupThread(response)

        // save e2e keys when e2e feature is enabled
        if (response.participants != null && isE2EFeatureEnabled()) {
          for (participant in response.participants) {
            if (!participant.e2eEncryptionKeys.isNullOrEmpty()) {
              val eKeyDao = dataManager.db().ekeyDao()
              for (e2eKey in participant.e2eEncryptionKeys!!) {
                if (e2eKey.deviceId == dataManager.preference().deviceId) {

                } else {
                  //update Remaining keys
                  val updatedRow: Int = eKeyDao.updateKey(
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
                    eKeyDao.save(eKeyTableUpdated)
                  }
                }
              }
            }
          }
        }

        groupRecord
      }
  }

  override fun getSearchedChannels(
    keyword: String,
    channelType: String?,
    joined: String?
  ): Single<List<GroupRecord>> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return dataManager.network().api().getSearchedGroups(tenantId, chatUserId, keyword, channelType, joined)
      .map { (groups) ->
        if (groups.isNullOrEmpty()) {
          ArrayList<GroupRecord>()
        } else {
          val transform =
            groups.transform { group: GroupResponse? ->
              transform(
                group,
                getTenantId(),
                dataManager.db(),
                dataManager.preference().chatServer!!,
                dataManager.preference().appUserId,
                dataManager.preference().chatUserId
              )
            }
          transform
        }
      }
  }

  override fun getActiveGroups(tenantId: String): Flowable<List<GroupRecord>>? {
    return groupDao.getActiveGroup(GroupRecord.STATUS_ACTIVE)
      .flatMap { groups: List<Group> ->
        val groupRecords = ArrayList<GroupRecord>()
        for (group in groups) {
          val groupRecord = transform(
            group,
            getTenantId(),
            dataManager.preference().appUserId
          )

          if (!groupRecord.isNotInGroup) {
            groupRecords.add(groupRecord)
          }
        }
        Flowable.just(groupRecords)
      }
  }

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler
    ): GroupRepository {
      return GroupRepositoryImpl(dataManager, eventHandler)
    }
  }

  private fun saveGroupsThread(groupList: List<Group>?) {
    if (groupList != null && groupList.isNotEmpty()) {
      for (group in groupList) {
        val thread: Thread? = ThreadMapper.fromGroup(group, appUserId, chatUserId)
        dataManager.db()
          .threadDao()
          .insertWithIgnore(thread)
        dataManager.db()
          .threadUserLinkDao()
          .insertWithIgnore(
            ThreadMapper.from(
              appUserId, group.groupId, group.threadId
            )
          )
      }
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