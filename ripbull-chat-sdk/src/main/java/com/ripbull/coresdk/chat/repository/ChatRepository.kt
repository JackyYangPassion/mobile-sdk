package com.ripbull.coresdk.chat.repository

import android.app.Activity
import android.content.Context
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import com.ripbull.coresdk.chat.mapper.ChatEvent
import com.ripbull.coresdk.chat.mapper.ChatMetaDataRecord
import com.ripbull.coresdk.chat.mapper.ChatReactionRecord
import com.ripbull.coresdk.chat.mapper.ChatSettingsRecord
import com.ripbull.coresdk.chat.mapper.DomainDataRecord
import com.ripbull.coresdk.chat.mapper.FollowThreadRecord
import com.ripbull.coresdk.chat.mapper.MessageMetaDataRecord
import com.ripbull.coresdk.chat.mapper.MessageRecord
import com.ripbull.coresdk.chat.mapper.ProfanityDataRecord
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.chat.model.MessageMetaData
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.entity.EKeyTable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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