package io.inappchat.inappchat.chat


import android.app.Activity
import android.content.Context
import io.inappchat.inappchat.chat.mapper.ChatEvent
import io.inappchat.inappchat.chat.mapper.ChatMetaDataRecord
import io.inappchat.inappchat.chat.mapper.ChatReactionRecord
import io.inappchat.inappchat.chat.mapper.ChatSettingsRecord
import io.inappchat.inappchat.chat.mapper.DomainDataRecord
import io.inappchat.inappchat.chat.mapper.MessageMetaDataRecord
import io.inappchat.inappchat.chat.mapper.MessageRecord
import io.inappchat.inappchat.chat.mapper.FollowThreadRecord
import io.inappchat.inappchat.chat.mapper.ProfanityDataRecord
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.chat.model.MessageMetaData
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.NetworkEvent
import io.inappchat.inappchat.core.type.RestoreType
import io.inappchat.inappchat.data.common.Result
import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.thread.mapper.ThreadRecord
import io.inappchat.inappchat.user.mapper.UserRecord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/** @author meeth
 */

public interface ChatModule {
  fun getThreads(): Flowable<MutableList<ThreadRecord>>?

  fun createThread(recipientId: String): Single<String>

  fun hasThread(): Single<Boolean>

  fun getMessages(
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<MutableList<MessageRecord>>

  fun getChatThreadMessages(
    threadId: String,
    parentMsgId: String?
  ): Single<MutableList<MessageRecord>>

  fun sendMessage(
    threadId: String,
    message: Message,
    customData: String? = null,
    isReplyThreadDisabled: Boolean? = false,
    isUserMentions: Boolean? = false
  ): Single<MessageRecord>

  fun sendE2EMessage(
    threadId: String,
    message: Message,
    customData: String? = null
  ): Single<MessageRecord>

  fun getChatUserId(): Single<Result>

  fun markAsRead(
    threadId: String,
    parentMsgId: String?
  ): Completable

  fun msgReadStatus(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent>

  fun messageOn(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent>

  fun messageOn(): Observable<NetworkEvent>

  fun blockUnblock(
    action: String,
    appUserId: String
  ): Single<Result>

  fun markAsFavorite(
    threadId: String,
    list: List<MessageRecord>,
    isFavorite: Boolean
  ): Single<List<MessageRecord>>

  fun getAllFavoriteMessages(): Observable<List<MessageRecord>>

  fun getAllFavoriteMessages(threadId: String): Observable<List<MessageRecord>>

  fun getAllFavoriteMessages(
    threadId: String,
    parentMsgId: String
  ): Flowable<List<MessageRecord>>

  fun downloadMedia(
    msgId: String,
    serverUrl: String,
    dirPath: String,
    mediaType: String
  ): Single<Boolean>

  fun downloadOn(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent>

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

  fun sourceOnMain(): Observable<ChatEvent>

  fun forwardChat(
    messageList: List<Message>,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String? = null,
    isE2E: Boolean = false
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
    message : Message,
    isUserMentions: Boolean? = false
  ): Single<MessageRecord>

  fun editE2EMessage(
    threadId: String,
    message : Message,
    isUserMentions: Boolean? = false
  ): Single<MessageRecord>

  fun chatRestore(): Single<Result>

  fun chatSkipRestore(): Single<Result>

  fun searchMessages(
    searchedText: String
  ): Single<ArrayList<MessageRecord>>

  fun globalSearch(
    searchedText: String,
    threadId: String? = null
  ): Single<ArrayList<MessageRecord>>

  fun restore(restoreType: RestoreType): Single<Result>

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
    threadId: String,
    currentMsgId: String? = null,
    direction: String? = null,
    pageSize: Int? = null
  ): Single<MutableList<MessageRecord>>

  fun forwardMediaChat(
    message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String? = null
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

  fun getThread(threadId: String): Single<ThreadRecord>
}