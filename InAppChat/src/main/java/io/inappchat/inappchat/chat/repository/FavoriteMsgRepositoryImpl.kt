package io.inappchat.inappchat.chat.repository

import androidx.annotation.RestrictTo
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatRecordMapper.transform
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.core.base.BaseRepository
import io.inappchat.inappchat.core.type.ChatEventType
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MessageStatus
import io.inappchat.inappchat.data.DataManager
import io.inappchat.inappchat.group.mapper.GroupMapper
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.user.mapper.UserMapper
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.cache.database.dao.ChatReactionDao
import io.inappchat.inappchat.cache.database.dao.ChatThreadDao
import io.inappchat.inappchat.cache.database.dao.GroupDao
import io.inappchat.inappchat.cache.database.dao.SingleChatDao
import io.inappchat.inappchat.cache.database.dao.ThreadDao
import io.inappchat.inappchat.cache.database.dao.UserDao
import io.inappchat.inappchat.cache.database.entity.ChatReactionEntity
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.cache.database.entity.ChatThread
import io.inappchat.inappchat.cache.database.entity.SingleChat
import io.inappchat.inappchat.cache.database.entity.ThreadEmbedded
import io.inappchat.inappchat.remote.model.request.StarMessageRequest
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/** @author meeth
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FavoriteMsgRepositoryImpl private constructor(
  private val dataManager: DataManager,
  private val chatDao: SingleChatDao = dataManager.db().singleChatDao(),
  private val chatThreadDao: ChatThreadDao = dataManager.db().chatThreadDao(),
  private val threadDao: ThreadDao = dataManager.db().threadDao(),
  private val userDao: UserDao = dataManager.db().userDao(),
  private val groupDao: GroupDao = dataManager.db().groupDao(),
  private val chatReactionDao: ChatReactionDao = dataManager.db().chatReactionDao()
) : BaseRepository(dataManager), FavoriteMsgRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager
    ): FavoriteMsgRepository {
      return FavoriteMsgRepositoryImpl(dataManager)
    }
  }

  override fun markAsFavorite(
    tenantId: String, threadId: String?,
    messages: List<MessageRecord?>, isFavorite: Boolean
  ): Single<List<MessageRecord>> {
    return Single.create { e: SingleEmitter<List<MessageRecord>> ->
      val singleChatDao = chatDao
      val appUserId = appUserId
      val favMessages = ArrayList<MessageRecord>()

      messages.forEach { record ->
        if (record?.type == ChatType.SINGLE.type || record?.type == ChatType.GROUP.type) {
          val singleChatEmbedded = singleChatDao.getSingleChatByMsgId(record?.id)
            ?: throw UnsupportedOperationException("Please wait for message to send to add to favorites")
          singleChatEmbedded.singleChat?.let { singleChat ->
            if (singleChat.status == MessageStatus.SENDING.status || singleChat.status == MessageStatus.FAILED.status) {
              throw UnsupportedOperationException("Please wait for message to send to add to favorites")
            }

            if (noInternetConnection()) {
              throw UnsupportedOperationException("Network unavailable")
            }

            var senderUserRecord : UserRecord? = null
            if(singleChat.type.equals(ChatType.GROUP.type)){
              senderUserRecord = UserMapper.transform(
                userDao.getUserByIdInSync(tenantId, singleChat.senderAppUserId)
              )
            }

            val response = dataManager.network().api()
              .starMessage(
                tenantId,
                chatUserId,
                StarMessageRequest(singleChat.msgUniqueId, singleChat.threadId, isFavorite)
              ).blockingGet()

            if (response.msgUniqueId.isNullOrEmpty()) {
              return@forEach
            }

            singleChat.isStarredChat = if (isFavorite) "1" else "0"
            singleChatDao.update(singleChat)
            if (record?.chatThreadMetadata != null && record.chatThreadMetadata!!.sendToChannel == 1 && !record.isForwardMessage) {
              // if reply_config =1, update chat thread as well
              val chatThread = dataManager.db().chatThreadDao()
                .getChatByServerMsgId(singleChat.msgUniqueId, threadId)
              chatThread.isStarredChat = if (isFavorite) "1" else "0"
              dataManager.db().chatThreadDao().update(chatThread)
            }

            val eventType = if (singleChat.senderAppUserId != appUserId) {
              ChatEventType.INCOMING
            } else {
              ChatEventType.OUTGOING
            }
            val reactionsFromDatabase = chatReactionDao.getAllReactions(singleChat.id).blockingGet()
            val reactionRecords = composeReactionList(reactionsFromDatabase)
            transform(
              singleChat = singleChat,
              senderRecord = senderUserRecord,
              chatThreadList = singleChatEmbedded.listChatThread,
              chatEventType = eventType,
              chatReactions = reactionRecords
            )
          }?.let { favMessages.add(it) }

        } else {
          val chatThread = dataManager.db().chatThreadDao().getChatByClientId(record?.id, threadId)
            ?: throw UnsupportedOperationException("Please wait for message to send to add to favorites")
          if (chatThread.status == MessageStatus.SENDING.status || chatThread.status == MessageStatus.FAILED.status) {
            throw UnsupportedOperationException("Please wait for message to send to add to favorites")
          }

          if (noInternetConnection()) {
            throw UnsupportedOperationException("Network unavailable")
          }

          val response = dataManager.network().api()
            .starMessage(
              tenantId,
              chatUserId,
              StarMessageRequest(chatThread.msgUniqueId, chatThread.threadId, isFavorite)
            ).blockingGet()

          if (response.msgUniqueId.isNullOrEmpty()) {
            return@forEach
          }

          chatThread.isStarredChat = if(isFavorite) "1" else "0"
          dataManager.db().chatThreadDao().update(chatThread)
          if (record?.chatThreadMetadata != null && record.chatThreadMetadata!!.sendToChannel == 1) {
            // if reply_config =1, update single chat as well
            val singleChat = singleChatDao.getChatByServerMsgId(chatThread.msgUniqueId, threadId)
            singleChat.isStarredChat = if (isFavorite) "1" else "0"
            singleChatDao.update(singleChat)
          }

          var recipientRecord: UserRecord? = null
          if (chatThread?.type.equals(ChatType.GROUP_CHAT_THREAD.type)) {
            recipientRecord = UserMapper.transform(
              userDao.getUserByIdInSync(
                tenantId, chatThread?.senderAppUserId
              )
            )
          }
          val eventType = if (chatThread?.senderAppUserId != appUserId) {
            ChatEventType.INCOMING
          } else {
            ChatEventType.OUTGOING
          }
          // List of participants should be loaded
          chatThread?.let {
            val reactionsFromDatabase = chatReactionDao.getAllThreadReactions(it.id).blockingGet()
            val reactionRecords = composeReactionList(reactionsFromDatabase)

            transform(
              chatThread = it,
              receiverRecord = recipientRecord,
              chatEventType = eventType,
              chatReactions = reactionRecords
            )
          }
            ?.let { favMessages.add(it) }
        }
      }
      e.onSuccess(favMessages)
    }.subscribeOn(Schedulers.single())
  }

  override fun getAllFavoriteMessages(tenantId: String): Observable<List<MessageRecord>>? {
    return Observable.zip(
      chatDao.getAllFavoriteMessages("1"),
      threadDao.getThreads(tenantId).toObservable(),
      BiFunction { singleChatList: List<SingleChat>, threadEmbeddeds: List<ThreadEmbedded> ->
        compostFavMessageRecord(
          tenantId, singleChatList,
          threadEmbeddeds
        )
      }
    )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }


  // markAsRead -> reply_config =1 this needs te updated in both table. msgUnigueId is same.
  // pull all fav messages main messages
  // pull main chat specific fav message along chatThread
  // pull chat thread specific fav message

  private fun compostFavMessageRecord(
    tenantId: String,
    singleChatList: List<SingleChat>,
    threadEmbeddeds: List<ThreadEmbedded>
  ): List<MessageRecord> {
    val messageRecordArrayList: MutableList<MessageRecord> = ArrayList()
    val appUserId = appUserId
    for (singleChat in singleChatList) {
      var senderRecord: UserRecord? = null
      var receiverRecord: UserRecord? = null
      var groupRecord: GroupRecord? = null
      for (threadEmbedded in threadEmbeddeds) {
        val threadUserLinks =
          threadEmbedded.threadUserLinks
        val type = threadEmbedded.thread.type
        if (type == ChatType.SINGLE.type) {
          for (threadUserLink in threadUserLinks) {
            val senderId = threadUserLink.senderId
            val recipientId = threadUserLink.recipientId
            if (threadEmbedded.thread.id == singleChat.threadId) {
              if (senderId == singleChat.senderAppUserId) {
                senderRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, senderId))
                receiverRecord =
                  UserMapper.transform(userDao.getUserByIdInSync(tenantId, recipientId))
              } else {
                receiverRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, senderId))
                senderRecord =
                  UserMapper.transform(userDao.getUserByIdInSync(tenantId, recipientId))
              }
              break
            }
          }
        } else if (type == ChatType.GROUP.type) {
          if (threadEmbedded.thread.id == singleChat.threadId) {
            val group = groupDao.getGroupByIdInSync(
              tenantId,
              threadEmbedded.thread.recipientChatId
            )
            groupRecord = GroupMapper.transform(group, tenantId, appUserId)
          }
        }
      }
      val eventType = if (singleChat.senderAppUserId != appUserId) {
        ChatEventType.INCOMING
      } else {
        ChatEventType.OUTGOING
      }

      messageRecordArrayList.add(
        transform(
          singleChat = singleChat,
          senderRecord = senderRecord,
          receiverRecord = receiverRecord,
          groupRecord = groupRecord,
          chatEventType = eventType
        )
      )
    }
    return messageRecordArrayList
  }

  override fun getAllFavoriteMessages(
    tenantId: String,
    threadId: String
  ): Observable<List<MessageRecord>>? {
    return Observable.zip(
      chatDao.getFavoriteMessages("1", threadId),
      threadDao.getThreads(tenantId).toObservable(),
      BiFunction { singleChatList: List<SingleChat>, threadEmbeddeds: List<ThreadEmbedded> ->
        compostFavMessageRecord(
          tenantId, singleChatList,
          threadEmbeddeds
        )
      }
    ).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  override fun getAllFavoriteMessages(
    tenantId: String,
    threadId: String,
    parentMsgId: String
  ): Flowable<List<MessageRecord>>? {
    return chatThreadDao.getFavoriteMessages("1", threadId, parentMsgId)
      .flatMap {chatThreadList: List<ChatThread> ->
      threadDao.getThreadByIdInAsync(threadId).flatMapPublisher { thread: Thread ->
        Flowable.just(compostFavMessageRecordForThread(tenantId, chatThreadList, thread))
      }
    }
  }

  private fun compostFavMessageRecordForThread(
    tenantId: String,
    chatThreadList: List<ChatThread>,
    thread: Thread
  ): List<MessageRecord> {
    val messageRecordArrayList: MutableList<MessageRecord> = ArrayList()
    val appUserId = appUserId
    for (chatThread in chatThreadList) {
      var senderRecord: UserRecord? = null
      var receiverRecord: UserRecord? = null
      var groupRecord: GroupRecord? = null

      val type = thread.type
      if (type == ChatType.SINGLE.type) {
        val senderId = thread.senderUserId
        val recipientId = thread.recipientUserId
        if (thread.id == chatThread.threadId) {
          if (senderId == chatThread.senderAppUserId) {
            senderRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, senderId))
            receiverRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, recipientId))
          } else {
            receiverRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, senderId))
            senderRecord = UserMapper.transform(userDao.getUserByIdInSync(tenantId, recipientId))
          }
        }
      } else if (type == ChatType.GROUP.type) {
        if (thread.id == chatThread.threadId) {
          val group = groupDao.getGroupByIdInSync(
            tenantId,
            thread.recipientChatId
          )
          groupRecord = GroupMapper.transform(group, tenantId, appUserId)
        }
      }

      val eventType = if (chatThread.senderAppUserId != appUserId) {
        ChatEventType.INCOMING
      } else {
        ChatEventType.OUTGOING
      }

      messageRecordArrayList.add(
        transform(
          chatThread = chatThread,
          senderRecord = senderRecord,
          receiverRecord = receiverRecord,
          groupRecord = groupRecord,
          chatEventType = eventType
        )
      )
    }
    return messageRecordArrayList
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

  private fun composeReactionList(listOfReactionEntities: List<ChatReactionEntity>?): ArrayList<ChatReactionRecord> {
    val chatReactionList = arrayListOf<ChatReactionRecord>()
    val groupBy = listOfReactionEntities?.groupBy { it.unicode }
    if (groupBy != null) {
      for ((k, reactionList) in groupBy) {

        val toList = reactionList.groupBy { it.userChatId }.keys.toList()
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
}