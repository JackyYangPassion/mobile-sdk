package io.inappchat.inappchat.chat.repositories

import android.content.Context
import io.inappchat.inappchat.chat.FakeRepoData
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.chat.repository.ChatRepository
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.DeleteType
import io.inappchat.inappchat.core.type.MessageUpdateType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.entity.EKeyTable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.rxjava3.schedulers.Schedulers

class FakeChatRepository : ChatRepository {

  override fun getMessages(
    tenantId: String,
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<MutableList<MessageRecord>> {
    TODO("Not yet implemented")
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
    customData: String?
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun getMessage(threadId: String, msgId: String): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun getChatThreadMessage(threadId: String, msgId: String): Single<MessageRecord> {
    TODO("Not yet implemented")
  }

  override fun editMessage(threadId: String, message: Message): Single<MessageRecord> {
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
    message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    deviceId: String,
    chatType: ChatType,
    customData: String?
  ): Single<Result> {
    TODO("Not yet implemented")
  }

  override fun copyMessage(context: Context, message: String): Single<String> {
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

  override fun deleteMessage(
    deleteType: String,
    threadId: String,
    messageList: ArrayList<Message>
  ): Single<List<MessageRecord>> {
    return Single.create { e: SingleEmitter<List<MessageRecord>> ->

      val favMessages = ArrayList<MessageRecord>()
      if (deleteType == DeleteType.DELETE_FOR_EVERYONE.type){
        favMessages.add(MessageRecord(id = FakeRepoData.LOCAL_MSG_ID,updateType = MessageUpdateType.DELETE, messageDeleteType = DeleteType.DELETE_FOR_EVERYONE.type))
      }
      if (deleteType == DeleteType.DELETE_FOR_USER.type){
        favMessages.add(MessageRecord(id = FakeRepoData.LOCAL_MSG_ID,updateType = MessageUpdateType.DELETE, messageDeleteType = DeleteType.DELETE_FOR_USER.type))
      }
      e.onSuccess(favMessages)
    }.subscribeOn(Schedulers.single())
  }

}