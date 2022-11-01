package com.ripbull.coresdk.chat


import android.app.Activity
import android.content.Context
import com.ripbull.coresdk.chat.mapper.ChatEvent
import com.ripbull.coresdk.chat.mapper.ChatMetaDataRecord
import com.ripbull.coresdk.chat.mapper.ChatReactionRecord
import com.ripbull.coresdk.chat.mapper.ChatSettingsRecord
import com.ripbull.coresdk.chat.mapper.DomainDataRecord
import com.ripbull.coresdk.chat.mapper.MessageMetaDataRecord
import com.ripbull.coresdk.chat.mapper.MessageRecord
import com.ripbull.coresdk.chat.mapper.FollowThreadRecord
import com.ripbull.coresdk.chat.mapper.ProfanityDataRecord
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.chat.model.MessageMetaData
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.core.type.RestoreType
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.thread.mapper.ThreadRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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