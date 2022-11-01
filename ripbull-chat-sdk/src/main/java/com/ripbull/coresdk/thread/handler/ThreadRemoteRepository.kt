package com.ripbull.coresdk.thread.handler

import android.text.TextUtils
import com.ripbull.coresdk.chat.mapper.ChatResponseToEntityMapper
import com.ripbull.coresdk.core.type.ChatEventType
import com.ripbull.coresdk.core.type.NotificationSettingsType
import com.ripbull.coresdk.core.type.SettingAppliedFor
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.e2e.E2EMapper
import com.ripbull.coresdk.thread.mapper.ThreadMapper
import com.ripbull.coresdk.thread.mapper.ThreadRecord
import com.ripbull.ertc.cache.database.entity.EKeyTable
import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.database.entity.ThreadUserLink
import com.ripbull.ertc.cache.database.entity.User
import com.ripbull.ertc.remote.model.request.CreateThreadRequest
import com.ripbull.ertc.remote.model.response.CreateThreadResponse
import com.ripbull.ertc.remote.model.response.MessageResponse
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.Objects

/** Created by DK on 24/02/19.  */
class ThreadRemoteRepository internal constructor(private val dataManager: DataManager) :
  ThreadRepository {
  private fun data(): DataManager {
    return dataManager
  }

  override fun hasThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<List<ThreadUserLink?>?> {
    return Single.just(null)
  }

  override fun getThreadByIdSync(threadId: String): Thread? = null

  override fun getThreadByIdAsync(threadId: String): Single<Thread?> {
    return Single.just(null)
  }

  override fun createThread(
    tenantId: String?,
    currentUser: User?,
    recipientUser: User?
  ): Single<String?> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    val chatUserId = data().preference().chatUserId
    return dataManager
      .network()
      .api()
      .createThread(
        tenantId!!, CreateThreadRequest(
          Objects.requireNonNull(chatUserId)!!, recipientUser!!.id
        ), chatUserId!!
      )
      .flatMap { response: CreateThreadResponse ->

        insertThreadData(response, chatUserId, tenantId, currentUser!!, recipientUser)

        val e2eKeys = response.e2eKeys
        if (e2eKeys != null && e2eKeys.isNotEmpty()) {
          for (e2EKey in e2eKeys) {
            if (e2EKey.deviceId == data().preference().deviceId) {
              //you can update keyId here but now don't need to update, once we implement things for parallel device
            } else {
              //update Remaining keys
              val updatedRow: Int = data().db().ekeyDao().updateKey(
                e2EKey.eRTCUserId, e2EKey.publicKey, e2EKey.keyId, e2EKey.deviceId, System.currentTimeMillis()
              )

              if (updatedRow == 0) {
                val eKeyTableUpdated = EKeyTable(
                  keyId = e2EKey.keyId,
                  deviceId = e2EKey.deviceId,
                  publicKey = e2EKey.publicKey,
                  privateKey = "",
                  ertcUserId = e2EKey.eRTCUserId,
                  tenantId = tenantId
                )
                data().db().ekeyDao().save(eKeyTableUpdated)
              }
            }
          }
        }
        Single.just(response.threadId)
      }
  }

  override fun insertThreadData(
    response: CreateThreadResponse,
    chatUserId: String,
    tenantId: String,
    currentUser: User,
    recipientUser: User,
    lastMessage: MessageResponse?
  ) {
    val participantsList = response.participantsList
    var recipientAppUserId = response.recipientAppUserId
    val userDao = dataManager.db().userDao()
    var recipientChatId = ""
    var muteSettings = NotificationSettingsType.ALL.mute
    var validTillValue = SettingAppliedFor.ALWAYS.duration

    for (participant in participantsList) {
      if ((!TextUtils.isEmpty(participant.eRTCRecipientId)
                && participant.eRTCRecipientId != chatUserId) || (!TextUtils.isEmpty(participant.eRTCUserId)
                && participant.eRTCUserId != chatUserId)) {
        if (recipientAppUserId.isNullOrEmpty()) {
          recipientAppUserId = participant.appUserId
        }
        val userByIdInSync =
          userDao.getUserByIdInSync(tenantId, recipientAppUserId)
        if (userByIdInSync != null) {
          if (participant.eRTCRecipientId != null) {
            userByIdInSync.userChatId = participant.eRTCRecipientId
          } else if (participant.eRTCUserId != null) {
            userByIdInSync.userChatId = participant.eRTCUserId
          }
          recipientChatId = userByIdInSync.userChatId.toString()
          userDao.insertWithReplace(userByIdInSync)
        }
      }

      if (participant.notificationSettings != null
        && participant.eRTCRecipientId == chatUserId) {
        muteSettings = participant.notificationSettings!!.allowFrom
      } else if (participant.notificationSettings != null
        && participant.eRTCUserId == chatUserId){
        muteSettings = participant.notificationSettings!!.allowFrom
      }
    }

    val thread = ThreadMapper.from(
      response, chatUserId, tenantId, currentUser, recipientUser, recipientChatId, muteSettings, 0, validTillValue
    )
    dataManager.db().threadDao().insertWithReplace(thread)

    val threadUserLinkDao = ThreadMapper.from(
      currentUser.id, recipientUser.id, response.threadId
    )
    dataManager.db().threadUserLinkDao().insertWithReplace(threadUserLinkDao)

    if (lastMessage != null) {
      val eventType = if (chatUserId != lastMessage.sendereRTCUserId) {
        ChatEventType.INCOMING
      } else {
        ChatEventType.OUTGOING
      }

      if (lastMessage.replyThreadFeatureData != null &&
        lastMessage.replyThreadFeatureData?.replyMsgConfig == 0) {
        // insert data into chat thread table
        return
      }

      val singleChat = ChatResponseToEntityMapper.getChatRow(
        thread = thread, message = lastMessage, chatEventType = eventType.type,
        baseUrl = dataManager.preference()?.chatServer,
        senderUserId = if (eventType == ChatEventType.OUTGOING) thread?.senderUserId else thread?.recipientUserId
      )

      dataManager.db().singleChatDao().insertWithReplace(singleChat)

      if (lastMessage.replyThreadFeatureData != null &&
        lastMessage.replyThreadFeatureData?.replyMsgConfig == 1) {
        //insert data into chat thread table as well
        val chatThread = ChatResponseToEntityMapper.getChatThreadRow(
          thread = thread, message = lastMessage, parentMsgId = singleChat.id,
          baseUrl = dataManager.preference()?.chatServer,
          senderUserId = if (eventType == ChatEventType.OUTGOING) thread?.senderUserId else thread?.recipientUserId
        )

        dataManager.db().chatThreadDao().insertWithAbort(chatThread)
      }
    }
  }

  override fun getThread(tenantId: String, threadId: String): Single<ThreadRecord> {
    return Single.just(null)
  }

  override fun getThreads(tenantId: String): Flowable<MutableList<ThreadRecord>>? =
    Flowable.just(null)

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