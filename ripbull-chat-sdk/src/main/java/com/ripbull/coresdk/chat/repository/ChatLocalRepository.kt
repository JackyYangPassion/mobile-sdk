package com.ripbull.coresdk.chat.repository

/**
 * Created by DK on 18/12/20.
 */

import android.app.Activity
import android.content.Context
import com.ripbull.coresdk.chat.mapper.ChatEvent
import com.ripbull.coresdk.chat.mapper.ChatMetaDataRecord
import com.ripbull.coresdk.chat.mapper.ChatReactionRecord
import com.ripbull.coresdk.chat.mapper.ChatRecordMapper
import com.ripbull.coresdk.chat.mapper.ChatSettingsRecord
import com.ripbull.coresdk.chat.mapper.DomainDataRecord
import com.ripbull.coresdk.chat.mapper.FollowThreadRecord
import com.ripbull.coresdk.chat.mapper.MessageMetaDataRecord
import com.ripbull.coresdk.chat.mapper.MessageRecord
import com.ripbull.coresdk.chat.mapper.ProfanityDataRecord
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.chat.model.MessageMetaData
import com.ripbull.coresdk.core.base.BaseRepository
import com.ripbull.coresdk.core.type.ChatEventType
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserMapper
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.dao.SingleChatDao
import com.ripbull.ertc.cache.database.dao.UserDao
import com.ripbull.ertc.cache.database.entity.ChatReactionEntity
import com.ripbull.ertc.cache.database.entity.EKeyTable
import com.ripbull.ertc.cache.database.entity.SingleChatEmbedded
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by DK on 08/12/20.
 */
class ChatLocalRepository private constructor(
  private val dataManager: DataManager,
  private val singleChatDao: SingleChatDao = dataManager.db().singleChatDao(),
  private val userDao: UserDao = dataManager.db().userDao()
) : BaseRepository(dataManager), ChatRepository {

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager
    ): ChatLocalRepository {
      return ChatLocalRepository(
        dataManager
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
    return singleChatDao.getAll(threadId)
      .flatMap { singleChatList: List<SingleChatEmbedded?> ->
        val messageRecords: MutableList<MessageRecord> = ArrayList()
        val appUserId = appUserId
        for (singleChatEmbedded in singleChatList) {
          var senderUserRecord: UserRecord? = null
          if (singleChatEmbedded?.singleChat?.type.equals(ChatType.GROUP.type)) {
            senderUserRecord = UserMapper.transform(
              userDao.getUserByIdInSync(
                tenantId, singleChatEmbedded?.singleChat?.senderAppUserId
              )
            )
          }
          singleChatEmbedded?.let { it ->
            it.singleChat?.let { singleChat ->
              val eventType = if (singleChat.chatEventType != null &&
                singleChat.chatEventType == ChatEventType.CHAT_META_DATA.type
              ) {
                ChatEventType.CHAT_META_DATA
              } else if (singleChat.senderAppUserId != appUserId) {
                ChatEventType.INCOMING
              } else {
                ChatEventType.OUTGOING
              }

              val reactionRecords = if (it.reactionEntities.isNullOrEmpty()) {
                arrayListOf<ChatReactionRecord>()
              } else {
                composeReactionList(it.reactionEntities)
              }

              ChatRecordMapper.transform(
                singleChat = singleChat,
                senderRecord = senderUserRecord,
                chatThreadList = it.listChatThread,
                chatEventType = eventType,
                chatReactions = reactionRecords
              )
            }
          }?.let { messageRecords.add(it) }
        }
        Single.just(messageRecords)
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
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
    TODO("Not yet implemented")
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
    TODO("Not yet implemented")
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

}