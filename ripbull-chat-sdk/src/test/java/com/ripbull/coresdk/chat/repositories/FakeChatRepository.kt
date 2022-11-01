package com.ripbull.coresdk.chat.repositories

import android.content.Context
import com.ripbull.coresdk.chat.FakeRepoData
import com.ripbull.coresdk.chat.mapper.ChatEvent
import com.ripbull.coresdk.chat.mapper.ChatMetaDataRecord
import com.ripbull.coresdk.chat.mapper.ChatReactionRecord
import com.ripbull.coresdk.chat.mapper.FollowThreadRecord
import com.ripbull.coresdk.chat.mapper.MessageMetaDataRecord
import com.ripbull.coresdk.chat.mapper.MessageRecord
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.chat.model.MessageMetaData
import com.ripbull.coresdk.chat.repository.ChatRepository
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.DeleteType
import com.ripbull.coresdk.core.type.MessageUpdateType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.entity.EKeyTable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers

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