package io.inappchat.inappchat.chat.repository

import android.app.Activity
import android.content.Context
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatSettingsRecord
import io.inappchat.inappchat.chat.mapper.DomainDataRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.ProfanityDataRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.inappchat.inappchat.cache.database.entity.EKeyTable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/** @author meeth
 */
@RestrictTo(LIBRARY_GROUP)
interface ChatRepository {

  fun getMessages(
    tenantId: String,
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<MutableList<MessageRecord>>

  fun getChatThreadMessages(
    tenantId: String,
    threadId: String,
    parentMsgId: String
  ): Single<MutableList<MessageRecord>>

  fun sendMessage(
    message: Message, tenantId: String, threadId: String, deviceId: String, customData: String?
  ): Single<MessageRecord>

  fun sendE2EMessage(
    message: Message,
    tenantId: String,
    threadId: String,
    deviceId: String,
    parallelDeviceList: ArrayList<EKeyTable>? = ArrayList(),
    customData: String?
  ): Single<MessageRecord>

  fun getChatUserId(
    tenantId: String, userId: String?, fcmToken: String?, deviceId: String?
  ): Single<Result>

  fun markAsRead(tenantId: String, threadId: String?, parentMsgId: String?): Completable

  fun blockUnblockUser(
    tenantId: String, action: String?, appUserId: String?
  ): Single<Result>

  // Publish subject
  fun messageOn(
    tenantId: String, threadId: String, parentMsgId: String?, chatType: ChatType
  ): Observable<NetworkEvent>

  fun messageOn(
    tenantId: String
  ): Observable<NetworkEvent>


  fun msgReadStatus(
    tenantId: String, threadId: String, parentMsgId: String?, chatType: ChatType
  ): Observable<NetworkEvent>

  fun downloadOn(
    tenantId: String, threadId: String, parentMsgId: String?, chatType: ChatType
  ): Observable<NetworkEvent>

  fun downloadMedia(
    msgId: String,
    serverUrl: String,
    dirPath: String,
    mediaType: String
  ): Single<Boolean>

  fun chatMetaDataOn(
    threadId: String
  ): Observable<ChatMetaDataRecord>

  fun sendReaction(
    threadId: String,
    messageMetaData: MessageMetaData
  ): Single<ChatReactionRecord>

  fun messageMetaDataOn(
    threadId: String
  ): Observable<MessageMetaDataRecord>

  fun sourceOnMain(
    tenantId: String
  ): Observable<ChatEvent>

  fun forwardChat(
    tenantId: String,
    messageList: List<Message>,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    deviceId: String,
    chatType: ChatType,
    customData: String?,
    isE2E: Boolean = false,
  ): Single<Result>

  fun getMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord>

  fun getChatThreadMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord>

  fun deleteMessage(
    deleteType: String,
    threadId: String,
    messageList: ArrayList<Message>
  ): Single<List<MessageRecord>>

  fun editMessage(
    threadId: String,
    message: Message
  ): Single<MessageRecord>

  fun editE2EMessage(
    threadId: String,
    message: Message,
    deviceId: String,
    parallelDeviceList: ArrayList<EKeyTable>? = ArrayList()
  ): Single<MessageRecord>

  fun searchMessages(
    searchedText: String
  ): Single<ArrayList<MessageRecord>>

  fun globalSearch(
    searchedText: String,
    threadId: String? = null
  ): Single<ArrayList<MessageRecord>>

  fun followThread(
    threadId: String,
    messageRecord: MessageRecord,
    isFollowed: Boolean
  ): Single<MessageRecord>

  fun reportMessage(
    threadId: String,
    messageRecord: MessageRecord,
    reportType: String,
    reason: String
  ): Single<MessageRecord>

  fun clearChat(
    threadId: String
  ): Single<Result>

  fun sendMessageAgain(
    threadId: String,
    msgId: String,
    parentMsgId: String? = null
  ): Single<MessageRecord>

  fun getMediaGallery(
    tenantId: String,
    threadId: String,
    msgType: String,
    currentMsgId: String? = null,
    direction: String? = null,
    pageSize: Int? = null
  ): Single<MutableList<MessageRecord>>

  fun forwardMediaChat(
    tenantId: String,
    message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    deviceId: String,
    chatType: ChatType,
    customData: String?
  ): Single<Result>

  fun copyMessage(activity: Activity, message: String): Single<String>

  fun getFollowThreads(
    threadId: String?,
    currentMsgId: String?,
    followedThread: Boolean,
    direction: String?,
    pageSize: Int?
  ): Single<MutableList<FollowThreadRecord>>

  fun followThread(
    threadId: String,
    messageId: String,
    isFollowed: Boolean
  ): Single<Result>

  fun isChatRestored(): Boolean

  fun getChatSettings(): Single<ChatSettingsRecord>

  fun getProfanityFilters(): Flowable<MutableList<ProfanityDataRecord>>

  fun getDomainFilters(): Flowable<MutableList<DomainDataRecord>>
}