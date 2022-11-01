package com.ripbull.coresdk.chat.repository

import com.ripbull.coresdk.core.base.BaseRepository
import com.ripbull.coresdk.core.event.EventHandler
import com.ripbull.coresdk.core.type.RestoreType
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupMapper
import com.ripbull.coresdk.thread.handler.ThreadRepository
import com.ripbull.coresdk.thread.mapper.ThreadMapper
import com.ripbull.coresdk.user.repository.UserRepository
import com.ripbull.ertc.cache.database.dao.ChatReactionDao
import com.ripbull.ertc.cache.database.dao.ChatThreadDao
import com.ripbull.ertc.cache.database.dao.EKeyDao
import com.ripbull.ertc.cache.database.dao.SingleChatDao
import com.ripbull.ertc.cache.database.dao.UserDao
import com.ripbull.ertc.cache.database.entity.User
import com.ripbull.ertc.cache.preference.PreferenceManager
import com.ripbull.ertc.mqtt.MqttManager
import com.ripbull.ertc.remote.NetworkConfig
import com.ripbull.ertc.remote.NetworkManager
import com.ripbull.sdk.downloader.handler.DownloadRepository
import io.reactivex.Single

/**
 * Created by DK on 08/12/20.
 */
class ChatRestoreRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val threadRepository: ThreadRepository,
  private val userRepository: UserRepository,
  private val eventHandler: EventHandler,
  private val downloadRepository: DownloadRepository,
  private val singleChatDao: SingleChatDao = dataManager.db().singleChatDao(),
  private val chatThreadDao: ChatThreadDao = dataManager.db().chatThreadDao(),
  private val chatReactionDao: ChatReactionDao = dataManager.db().chatReactionDao(),
  private val userDao: UserDao = dataManager.db().userDao(),
  private val ekeyDao: EKeyDao = dataManager.db().ekeyDao(),
  private val networkManager: NetworkManager = dataManager.network(),
  private val preference: PreferenceManager = dataManager.preference(),
  private val mqtt: MqttManager = dataManager.mqtt(),
  private val networkConfig: NetworkConfig = dataManager.networkConfig()
) : BaseRepository(dataManager), ChatRestoreRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      threadRepository: ThreadRepository,
      userRepository: UserRepository,
      eventHandler: EventHandler,
      downloadRepository: DownloadRepository
    ): ChatRestoreRepository {
      return ChatRestoreRepositoryImpl(
        dataManager,
        threadRepository,
        userRepository,
        eventHandler,
        downloadRepository
      )
    }
  }

  override fun restore(restoreType : RestoreType): Single<Result> {
    return dataManager.network().api().getThreadHistory(tenantId, chatUserId)
      .flatMap { threadRestoreResponse ->
        // Add thread details to DB
        // Add Chat details to DB
        val currentUser =
          userDao.getUserById(tenantId, appUserId).blockingGet()
        threadRestoreResponse.threads?.forEach { threadFromServerResponse ->
          val recipientAppUserId = threadFromServerResponse.thread.user?.appUserId
          val participantsList = threadFromServerResponse.thread.participantsList
          var recipientUser : User? = null
          participantsList.forEach { participant ->
            if(participant.appUserId != chatUserId){
              participant.appUserId?.let {
                val user = User(id = it, tenantId = tenantId,userChatId = participant.eRTCUserId)
                recipientUser = user
                return@forEach
              }
            }
          }

          recipientUser?.let {
            threadRepository.insertThreadData(
              threadFromServerResponse.thread,
              chatUserId,
              tenantId,
              currentUser,
              it,
              lastMessage = threadFromServerResponse.thread.lastMessage ?: threadFromServerResponse.lastMessage
            )
          }

          /*val groupResponse = threadFromServerResponse.thread.group
          if (groupResponse != null) {
            val groupRecord = GroupMapper.transform(
              groupResponse.copy(participants = threadFromServerResponse.thread.participantsList),
              tenantId,
              dataManager.db(),
              dataManager.preference().chatServer!!,
              dataManager.preference().appUserId
            )
            dataManager.db().groupDao().insertWithReplace(GroupMapper.transform(groupRecord))
            val thread =
              ThreadMapper.fromGroupResponse(groupResponse, appUserId, chatUserId)
            dataManager.db().threadDao().insertWithIgnore(thread)
            dataManager.db()
              .threadUserLinkDao()
              .insertWithIgnore(
                ThreadMapper.from(
                  appUserId, groupResponse?.groupId,
                  groupResponse?.threadId
                )
              )
          }*/
        }
        Single.just(Result(true, "Successfully restored"))
      }
  }

  override fun chatRestore(): Single<Result> {
    return threadRestore()
    /*return userRepository.getChatUsersRemote(tenantId).flatMap { userRecordsList ->
      // Get list of users
      // Added thread details to DB
      // Add Chat Data to DB
      // set skipRestore flag false
      preference.skipRestoreFlag = false
      val userDao = data().db().userDao()
      val currentUser =
        userDao.getUserById(tenantId, appUserId).blockingGet()
      val appUserIds = arrayListOf<String>()
      userRecordsList.forEach { userRecord ->
        userRecord.id?.let { appUserIds.add(it) }
      }
      val userEntitiesByUserAppIds =
        userDao.getUserEntitiesByUserAppIds(appUserIds.toTypedArray()).blockingGet()
      //Threads
      dataManager.network().api().getThreadHistory(tenantId, chatUserId)
        .flatMap { threadRestoreResponse ->
          // Add thread details to DB
          // Add Chat details to DB
          threadRestoreResponse.threads?.forEach { threadFromServerResponse ->
            val recipientAppUserId = threadFromServerResponse.thread.user?.appUserId
            var recipientUser : User? = null
            userEntitiesByUserAppIds.forEach { user ->
              if(recipientAppUserId == user.id){
                recipientUser = user
                return@forEach
              }
            }
            recipientUser?.let {
              threadRepository.insertThreadData(
                threadFromServerResponse.thread,
                chatUserId,
                tenantId,
                currentUser,
                it,
                threadFromServerResponse.lastMessage
              )
            }

            *//*val groupResponse = threadFromServerResponse.thread.group
            if (groupResponse != null) {
              val groupRecord = GroupMapper.transform(
                groupResponse.copy(participants = threadFromServerResponse.thread.participantsList),
                tenantId,
                dataManager.db(),
                dataManager.preference().chatServer!!,
                dataManager.preference().appUserId
              )
              dataManager.db().groupDao().insertWithReplace(GroupMapper.transform(groupRecord))
              val thread =
                ThreadMapper.fromGroupResponse(groupResponse, appUserId, chatUserId)
              dataManager.db().threadDao().insertWithIgnore(thread)
              dataManager.db()
                .threadUserLinkDao()
                .insertWithIgnore(
                  ThreadMapper.from(
                    appUserId, groupResponse?.groupId,
                    groupResponse?.threadId
                  )
                )
            }*//*
          }
          Single.just(Result(true, "Successfully restored"))
        }
    }*/
  }

  private fun threadRestore(): Single<Result> {
    return userRepository.getChatUsersRemote(tenantId).flatMap { userRecordsList ->
      // Get list of users
      // Added thread details to DB
      // Add Chat Data to DB
      // set skipRestore flag false
      preference.skipRestoreFlag = false
      val userDao = data().db().userDao()
      val currentUser =
        userDao.getUserById(tenantId, appUserId).blockingGet()
      val appUserIds = arrayListOf<String>()
      userRecordsList.forEach { userRecord ->
        userRecord.id?.let { appUserIds.add(it) }
      }
      val userEntitiesByUserAppIds =
        userDao.getUserEntitiesByUserAppIds(appUserIds.toTypedArray()).blockingGet()
      //Threads
      dataManager.network().api().getThreadHistory(tenantId, chatUserId)
        .flatMap { threadRestoreResponse ->
          // Add thread details to DB
          // Add Chat details to DB
          threadRestoreResponse.threads?.forEach { threadFromServerResponse ->
            val recipientAppUserId = threadFromServerResponse.thread.user?.appUserId
            var recipientUser : User? = null
            userEntitiesByUserAppIds.forEach { user ->
              if(recipientAppUserId == user.id){
                recipientUser = user
                return@forEach
              }
            }
            recipientUser?.let {
              threadRepository.insertThreadData(
                threadFromServerResponse.thread,
                chatUserId,
                tenantId,
                currentUser,
                it,
                threadFromServerResponse.lastMessage
              )
            }
            /*val groupResponse = threadFromServerResponse.thread.group
            if (groupResponse != null) {
              val groupRecord = GroupMapper.transform(
                groupResponse.copy(participants = threadFromServerResponse.thread.participantsList),
                tenantId,
                dataManager.db(),
                dataManager.preference().chatServer!!,
                dataManager.preference().appUserId
              )
              dataManager.db().groupDao().insertWithReplace(GroupMapper.transform(groupRecord))
              val thread =
                ThreadMapper.fromGroupResponse(groupResponse, appUserId, chatUserId)
              dataManager.db().threadDao().insertWithIgnore(thread)
              dataManager.db()
                .threadUserLinkDao()
                .insertWithIgnore(
                  ThreadMapper.from(
                    appUserId, groupResponse?.groupId,
                    groupResponse?.threadId
                  )
                )
            }*/
          }
          Single.just(Result(true, "Successfully restored"))
        }
    }
  }

  override fun chatSkipRestore(): Single<Result> {
    preference.skipRestoreFlag = true
    return Single.just(Result(true, "Chat restoration skipped"))
  }
}