package io.inappchat.inappchat.chat.repository

/**
 * Created by DK on 18/12/20.
 */

import android.app.Activity
import android.content.Context
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper
import io.inappchat.inappchat.chat.mapper.ChatResponseToEntityMapper
import io.inappchat.inappchat.chat.mapper.ChatSettingsRecord
import io.inappchat.inappchat.chat.mapper.DomainDataRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.ProfanityDataRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.event.EventHandler
import io.inappchat.inappchat.core.type.ChatEventType
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupMapper
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.thread.handler.ThreadRepository
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.cache.database.dao.ChatReactionDao
import io.inappchat.inappchat.cache.database.dao.ChatThreadDao
import io.inappchat.inappchat.cache.database.dao.EKeyDao
import io.inappchat.inappchat.cache.database.dao.SingleChatDao
import io.inappchat.inappchat.cache.database.dao.UserDao
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.preference.PreferenceManager
import io.inappchat.inappchat.mqtt.MqttManager
import io.inappchat.inappchat.remote.NetworkConfig
import io.inappchat.inappchat.remote.NetworkManager
import io.inappchat.inappchat.remote.model.response.Reactions
import io.inappchat.inappchat.downloader.handler.DownloadRepository
import io.reactivex.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by DK on 08/12/20.
 */
class ChatRemoteRepository private constructor(
  private val dataManager: DataManager,
  private val threadRepository: ThreadRepository,
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
) : BaseRepository(dataManager), ChatRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      threadRepository: ThreadRepository,
      eventHandler: EventHandler,
      downloadRepository: DownloadRepository
    ): ChatRemoteRepository {
      return ChatRemoteRepository(
        dataManager,
        threadRepository,
        eventHandler,
        downloadRepository
      )
    }
  }

  override fun getMessages(
    tenantId: String,
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<MutableList<MessageRecord>> {
    val currentMessageId = if (currentMsgId.isNullOrEmpty() || isGlobalSearched == true) {
      currentMsgId
    } else {
      val singleChat = singleChatDao.getChatByLocalMsgId(currentMsgId, threadId)
      if (singleChat == null) {
        return Single.just(ArrayList<MessageRecord>())
      } else {
        singleChat.msgUniqueId
      }
    }
    return data().network().api()
      .getMessageHistory(tenantId, chatUserId, threadId, currentMessageId, direction, pageSize, isGlobalSearched)
      .flatMap {

      // Fetch thread data
      val thread = threadRepository.getThreadByIdSync(threadId)
      val messageRecords : MutableList<MessageRecord> = ArrayList()

      it.chats?.forEach { messageResponse ->
        var senderUserId = thread?.recipientUserId
        var eventType = ChatEventType.INCOMING
        if (messageResponse.sendereRTCUserId.equals(chatUserId)) {
          eventType = ChatEventType.OUTGOING
          senderUserId = thread?.senderUserId
        }

        if (thread?.type.equals(ChatType.GROUP.type)) {
          senderUserId = userDao.getAppUserId(messageResponse.sendereRTCUserId)
        }

        if (messageResponse.replyThreadFeatureData != null &&
          messageResponse.replyThreadFeatureData?.replyMsgConfig == 0) {
          // insert data into chat thread table
          return@forEach
        }

        // update chat DB
        val singleChat = ChatResponseToEntityMapper.getChatRow(
          thread = thread,
          message = messageResponse,
          chatEventType = eventType.type,
          baseUrl = preference()?.chatServer,
          senderUserId = senderUserId
        )
        try {
          singleChatDao.insertWithAbort(singleChat)
        } catch (e: Exception) {
        }

        // update thread DB
        if(messageResponse.replyThreadFeatureData != null &&
          messageResponse.replyThreadFeatureData?.replyMsgConfig == 1) {
          val chatThread = ChatResponseToEntityMapper.getChatThreadRow(
            thread = thread,
            message = messageResponse,
            parentMsgId = singleChat.id,
            baseUrl = preference()?.chatServer,
            senderUserId = senderUserId
          )
          try {
            chatThreadDao.insertWithAbort(chatThread)
          } catch (e: Exception) {
          }
        }

        val reactionRecords = composeReactionList(messageResponse.reactions)
        ChatRecordMapper.transform(
          singleChat = singleChat,
          chatEventType = eventType,
          chatReactions = reactionRecords
        )

        val messageRecord = ChatRecordMapper.transform(singleChat = singleChat, chatEventType = eventType)
        messageRecords.add(messageRecord)
        // update Message metadata
      }

      Single.just(messageRecords)
    }.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

  }

  private fun composeReactionList(reactions: List<Reactions>?): ArrayList<ChatReactionRecord> {
    val chatReactionList = arrayListOf<ChatReactionRecord>()
    val map = reactions?.groupBy { it.emojiCode }
    if (map != null) {
      for ((k, reactionList) in map) {

        val toList = reactionList.groupBy { it.emojiCode }.keys.toList()
        val userEntity = userDao.getUserEntityByUserChatIds(toList.toTypedArray())
        val userRecordList = arrayListOf<UserRecord>()
        userEntity.forEach {
          userRecordList.add(UserMapper.transform(it))
        }
        chatReactionList.add(
          ChatReactionRecord(
            emojiCode = k,
            count = reactionList.size,
            userRecord = userRecordList
          )
        )
      }
    }
    return chatReactionList
  }

  override fun getChatThreadMessages(
    tenantId: String,
    threadId: String,
    parentMsgId: String
  ): Single<MutableList<MessageRecord>> {
    TODO("Not yet implemented")
  }

  override fun sendMessage(
    message: Message,
    tenantId: String,
    threadId: String,
    deviceId: String,
    customData: String?
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun sendE2EMessage(
    message: Message,
    tenantId: String,
    threadId: String,
    deviceId: String,
    parallelDeviceList: ArrayList<EKeyTable>?,
    customData: String?
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun getChatUserId(
    tenantId: String,
    userId: String?,
    fcmToken: String?,
    deviceId: String?
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun markAsRead(tenantId: String, threadId: String?, parentMsgId: String?): Completable {
    TODO("Not yet implemented")
  }

  override fun blockUnblockUser(
    tenantId: String,
    action: String?,
    appUserId: String?
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun messageOn(
    tenantId: String,
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    TODO("Not yet implemented")
  }

  override fun messageOn(tenantId: String): Observable<NetworkEvent> {
    TODO("Not yet implemented")
  }

  override fun msgReadStatus(
    tenantId: String,
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    TODO("Not yet implemented")
  }

  override fun downloadOn(
    tenantId: String,
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    TODO("Not yet implemented")
  }

  override fun downloadMedia(
    msgId: String,
    serverUrl: String,
    dirPath: String,
    mediaType: String
  ): Single<Boolean> {
    TODO("Not yet implemented")
  }

  override fun chatMetaDataOn(threadId: String): Observable<ChatMetaDataRecord> {
    TODO("Not yet implemented")
  }

  override fun sendReaction(
    threadId: String,
    messageMetaData: MessageMetaData
  ): Single<ChatReactionRecord> {
    TODO("Not yet implemented")
  }

  override fun messageMetaDataOn(threadId: String): Observable<MessageMetaDataRecord> {
    TODO("Not yet implemented")
  }

  override fun sourceOnMain(tenantId: String): Observable<ChatEvent> {
    TODO("Not yet implemented")
  }

  override fun forwardChat(
    tenantId: String,
    messageList: List<Message>,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    deviceId: String,
    chatType: ChatType,
    customData: String?,
    isE2E: Boolean
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun getMessage(threadId: String, msgId: String): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun getChatThreadMessage(threadId: String, msgId: String): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun deleteMessage(
    deleteType: String,
    threadId: String,
    messageList: ArrayList<Message>
  ): Single<List<MessageRecord>> {
    TODO("Not yet implemented")
  }

  override fun editMessage(threadId: String, message: Message): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun editE2EMessage(
    threadId: String,
    message: Message,
    deviceId: String,
    parallelDeviceList: ArrayList<EKeyTable>?
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun searchMessages(searchedText: String): Single<ArrayList<MessageRecord>> {
    TODO("Not yet implemented")
  }

  override fun globalSearch(searchedText: String, threadId: String?): Single<ArrayList<MessageRecord>> {
    TODO("Not yet implemented")
  }

  override fun followThread(
    threadId: String,
    messageRecord: MessageRecord,
    isFollowed: Boolean
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun followThread(
    threadId: String,
    messageId: String,
    isFollowed: Boolean
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun reportMessage(
    threadId: String,
    messageRecord: MessageRecord,
    reportType: String,
    reason: String
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun clearChat(threadId: String): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun sendMessageAgain(
    threadId: String,
    msgId: String,
    parentMsgId: String?
  ): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun getMediaGallery(
    tenantId: String,
    threadId: String,
    msgType: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?
  ): Single<MutableList<MessageRecord>> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }

    return data().network().api()
      .getMediaGallery(tenantId, chatUserId, threadId, msgType, currentMsgId, direction, pageSize)
      .flatMap {

        // Fetch thread data
        val thread = threadRepository.getThreadByIdSync(threadId)
        val messageRecords : MutableList<MessageRecord> = ArrayList()

        it.chats?.forEach { messageResponse ->
          var senderUserId = thread?.recipientUserId
          var eventType = ChatEventType.INCOMING
          if (messageResponse.sendereRTCUserId.equals(chatUserId)) {
            eventType = ChatEventType.OUTGOING
            senderUserId = thread?.senderUserId
          }

          if (thread?.type.equals(ChatType.GROUP.type)) {
            senderUserId = userDao.getAppUserId(messageResponse.sendereRTCUserId)
          }

          /*if (messageResponse.replyThreadFeatureData != null &&
            messageResponse.replyThreadFeatureData?.replyMsgConfig == 0) {
            // insert data into chat thread table
            return@forEach
          }*/

          val singleChat = ChatResponseToEntityMapper.getChatRow(
            thread = thread,
            message = messageResponse,
            chatEventType = eventType.type,
            baseUrl = preference()?.chatServer,
            senderUserId = senderUserId
          )

          val reactionRecords = composeReactionList(messageResponse.reactions)
          val messageRecord = ChatRecordMapper.transform(
            singleChat = singleChat, chatEventType = eventType, chatReactions = reactionRecords
          )
          messageRecords.add(messageRecord.copy(id = messageResponse.msgUniqueId))
          // update Message metadata
        }

        Single.just(messageRecords)
      }.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  override fun forwardMediaChat(
    tenantId: String,
    Message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    deviceId: String,
    chatType: ChatType,
    customData: String?
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun copyMessage(activity: Activity, message: String): Single<String> {
    TODO("Not yet implemented")
  }

  override fun getFollowThreads(
    threadId: String?,
    currentMsgId: String?,
    followedThread: Boolean,
    direction: String?,
    pageSize: Int?
  ): Single<MutableList<FollowThreadRecord>> {
    if (noInternetConnection()) {
      throw UnsupportedOperationException("Network unavailable")
    }
    return data().network().api()
      .getFollowThread(tenantId, chatUserId, threadId, currentMsgId, direction, pageSize)
      .flatMap {

        val followThreadRecords: MutableList<FollowThreadRecord> = ArrayList()

        it.chats?.forEach { messageResponse ->
          var type: String = ChatType.SINGLE.type
          var name = ""
          var replyCount: Int? = 1
          var recipient: UserRecord? = null
          var groupRecord: GroupRecord? = null
          val participants: MutableList<String> = ArrayList()
          val replyRecords: MutableList<MessageRecord> = ArrayList()
          val senderUserId = userDao.getAppUserId(messageResponse.sendereRTCUserId)
          val senderUserRecord = UserMapper.transform(
            userDao.getUserByIdInSync(tenantId, senderUserId)
          )
          var eventType = ChatEventType.INCOMING
          if (messageResponse.sendereRTCUserId.equals(chatUserId)) {
            eventType = ChatEventType.OUTGOING
          }

          messageResponse.thread?.let { thread ->
            if (thread.threadType == ChatType.GROUP.type) {
              type = ChatType.GROUP.type
              name = dataManager.db().groupDao().getGroupName(thread.threadId)
              groupRecord = GroupMapper.transform(
                dataManager.db().groupDao().getGroupByThreadId(tenantId, thread.threadId),
                tenantId,
                appUserId
              )
            } else {
              for (user in thread.participantsList) {
                if (user.eRTCRecipientId != chatUserId) {
                  val recipientUserId = userDao.getAppUserId(user.eRTCRecipientId)
                  name = userDao.getName(recipientUserId)
                  recipient =
                    UserMapper.transform(userDao.getUserByIdInSync(tenantId, recipientUserId))
                }
              }
            }
          }

          messageResponse.replyThreadFeatureData?.let { replyThread ->
            replyCount = replyThread.numOfReplies
            replyThread.responderList?.let { responderList ->
              for (user in responderList) {
                participants.add(
                  if (user.eRTCUserId.equals(chatUserId)) {
                    "You"
                  } else {
                    userDao.getName(user.appUserId)
                  }
                )
              }
            }
          }

          val singleChat = ChatResponseToEntityMapper.getChatRow(
            thread = Thread(
              messageResponse.threadId!!,
              name,
              type,
              tenantId,
              "",
              "",
              0,
              0,
              0,
              0,
              0,
              "",
              "",
              "",
              "",
              0,
              "",
              0,
              ""
            ),
            message = messageResponse,
            chatEventType = eventType.type,
            baseUrl = preference()?.chatServer,
            senderUserId = senderUserId
          )

          val reactionRecords = composeReactionList(messageResponse.reactions)
          val messageRecord = ChatRecordMapper.transform(
            singleChat = singleChat,
            chatEventType = eventType,
            chatReactions = reactionRecords,
            senderRecord = senderUserRecord,
            groupRecord = groupRecord
          )

          replyRecords.add(messageRecord)
          messageResponse.replies?.let { replies ->
            for (reply in replies) {
              val senderUserId = userDao.getAppUserId(reply.sendereRTCUserId)
              var replyEventType = ChatEventType.INCOMING
              if (reply.sendereRTCUserId.equals(chatUserId)) {
                replyEventType = ChatEventType.OUTGOING
              }

              val singleChat = ChatResponseToEntityMapper.getChatRow(
                thread = Thread(
                  reply.threadId!!,
                  name,
                  type,
                  tenantId,
                  "",
                  "",
                  0,
                  0,
                  0,
                  0,
                  0,
                  "",
                  "",
                  "",
                  "",
                  0,
                  "",
                  0,
                  ""
                ),
                message = reply,
                chatEventType = eventType.type,
                baseUrl = preference()?.chatServer,
                senderUserId = senderUserId
              )

              val reactionRecords = composeReactionList(reply.reactions)
              replyRecords.add(
                ChatRecordMapper.transform(
                  singleChat = singleChat,
                  chatEventType = replyEventType,
                  chatReactions = reactionRecords,
                  senderRecord = UserMapper.transform(
                    userDao.getUserByIdInSync(
                      tenantId,
                      senderUserId
                    )
                  ),
                  groupRecord = groupRecord
                )
              )
            }
          }

          val followThreadRecord = FollowThreadRecord(
            threadId = messageResponse.threadId!!,
            name = name,
            type = type,
            replyCount = replyCount!!,
            participants = participants,
            parentMsgId = messageResponse.msgUniqueId,
            isFollowThread = messageResponse.follow!!,
            recipient = recipient,
            group = groupRecord,
            replies = replyRecords
          )
          followThreadRecords.add(followThreadRecord)
        }

        Single.just(followThreadRecords)
      }.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  override fun isChatRestored(): Boolean {
    TODO("Not yet implemented")
  }

  override fun getChatSettings(): Single<ChatSettingsRecord> {
    TODO("Not yet implemented")
  }

  override fun getProfanityFilters(): Flowable<MutableList<ProfanityDataRecord>> {
    TODO("Not yet implemented")
  }

  override fun getDomainFilters(): Flowable<MutableList<DomainDataRecord>> {
    TODO("Not yet implemented")
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