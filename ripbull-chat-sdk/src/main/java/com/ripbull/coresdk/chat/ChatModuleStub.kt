package com.ripbull.coresdk.chat

import android.app.Activity
import android.content.Context
import com.ripbull.coresdk.R
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
import com.ripbull.coresdk.core.ChatSDKException
import com.ripbull.coresdk.core.ChatSDKException.Error.InvalidModule
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MessageType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.core.type.RestoreType
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.eRTCSDK
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.thread.mapper.ThreadRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.coresdk.utils.Constants
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/** @author meeth
 */
class ChatModuleStub private constructor(private val appContext: Context) : ChatModule {
  override fun getThreads(): Flowable<MutableList<ThreadRecord>> {
    return Flowable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun createThread(recipientId: String): Single<String> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun hasThread(): Single<Boolean> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun getMessages(
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<MutableList<MessageRecord>> {
    return Single.just(null)
  }

  override fun getChatThreadMessages(
    threadId: String,
    parentMsgId: String?
  ): Single<MutableList<MessageRecord>> {
    return Single.just(null);
  }

  override fun sendE2EMessage(
    threadId: String,
    message: Message,
    customData: String?
  ): Single<MessageRecord> = sendMessage(threadId,message)

  override fun sendMessage(
    threadId: String,
    message: Message,
    customData: String?,
    isReplyThreadDisabled: Boolean?,
    isUserMentions: Boolean?
  ): Single<MessageRecord> {

    if (isReplyThreadDisabled == true) {
      return Single.error(
        ChatSDKException(
          InvalidModule(),
          appContext.getString(
            R.string.alert_message,
            Constants.Features.REPLY_THREAD
          )
        ))
    } else if (isUserMentions == true) {
      return Single.error(
        ChatSDKException(
          InvalidModule(),
          appContext.getString(
            R.string.alert_message,
            Constants.Features.USER_MENTIONS
          )
        ))
    }

    when {
      MessageType.IMAGE == message.media?.type       -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND + message.media.type.type
            )
          )
        )
      }
      MessageType.AUDIO == message.media?.type       -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND + message.media.type.type
            )
          )
        )
      }
      MessageType.VIDEO == message.media?.type       -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND + message.media.type.type
            )
          )
        )
      }
      MessageType.GIF == message.media?.type         -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND + message.media.type.type
            )
          )
        )
      }
      MessageType.GIPHY == message.giphy?.type       -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND + message.giphy.type.type
            )
          )
        )
      }
      MessageType.CONTACT == message.contact?.type   -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND_CONTACT
            )
          )
        )
      }
      MessageType.LOCATION == message.location?.type -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND_LOCATION
            )
          )
        )
      }
      MessageType.FILE == message.file?.type         -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND_DOCUMENT
            )
          )
        )
      }
      else                                           -> {
        return Single.error(
          ChatSDKException(
            InvalidModule(),
            appContext.getString(
              R.string.alert_message,
              Constants.Features.SEND_TEXT
            )
          ))
      }
    }
  }

  override fun getChatUserId(): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun markAsRead(
    threadId: String,
    parentMsgId: String?
  ): Completable {
    return Completable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.READ_RECEIPT
        )
      )
    )
  }

  override fun msgReadStatus(threadId: String, parentMsgId: String?, chatType: ChatType): Observable<NetworkEvent> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.READ_RECEIPT
        )
      )
    )
  }

  override fun messageOn(threadId: String, parentMsgId: String?, chatType: ChatType): Observable<NetworkEvent> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun messageOn(): Observable<NetworkEvent> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  override fun blockUnblock(
    action: String,
    appUserId: String
  ): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.BLOCK_USER
        )
      )
    )
  }

  override fun markAsFavorite(
    threadId: String,
    list: List<MessageRecord>,
    isFavorite: Boolean
  ): Single<List<MessageRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.ADD_TO_FAVOURITE
        )
      )
    )
  }

  override fun getAllFavoriteMessages(): Observable<List<MessageRecord>> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.ADD_TO_FAVOURITE
        )
      )
    )
  }

  override fun getAllFavoriteMessages(threadId: String): Observable<List<MessageRecord>> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.ADD_TO_FAVOURITE
        )
      )
    )
  }

  override fun getAllFavoriteMessages(
    threadId: String,
    parentMsgId: String
  ): Flowable<List<MessageRecord>> {
    return Flowable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.ADD_TO_FAVOURITE
        )
      )
    )
  }

  override fun downloadMedia(
    msgId: String,
    serverUrl: String,
    dirPath: String,
    mediaType: String
  ): Single<Boolean> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.DOWNLOAD_MEDIA
        )
      )
    )
  }

  override fun downloadOn(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.DOWNLOAD_MEDIA
        )
      )
    )
  }

  override fun chatMetaDataOn(threadId: String): Observable<ChatMetaDataRecord> {
    return Observable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT_MUTE_NOTIFICATIONS
        )
      )
    )
  }

  override fun messageMetaDataOn(threadId: String): Observable<MessageMetaDataRecord> {
    return Observable.just(null)
  }

  override fun sourceOnMain(): Observable<ChatEvent> {
    return Observable.just(null)
  }

  override fun deleteMessage(
    deleteType: String,
    threadId: String,
    messageList: ArrayList<Message>
  ): Single<List<MessageRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.DELETE_CHAT
        )
      )
    )
  }

  override fun sendReaction(
    threadId: String,
    messageMetaData: MessageMetaData
  ): Single<ChatReactionRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.CHAT_REACTIONS)
      )
    )
  }

  override fun forwardChat(
    messageList: List<Message>,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String?,
    isE2E: Boolean
  ): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.FORWARD_CHAT
        )
      )
    )
  }

  override fun editMessage(
    threadId: String,
    message: Message,
    isUserMentions: Boolean?
  ): Single<MessageRecord> {
    if (isUserMentions == true) {
      return Single.error(
        ChatSDKException(
          InvalidModule(),
          appContext.getString(
            R.string.alert_message,
            Constants.Features.USER_MENTIONS
          )
        ))
    }

    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.EDIT_CHAT
        )
      )
    )
  }

  override fun editE2EMessage(
    threadId: String,
    message: Message,
    isUserMentions: Boolean?
  ): Single<MessageRecord> = editMessage(threadId, message, isUserMentions)

  override fun getMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord> {
    return Single.just(null)
  }

  override fun getChatThreadMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord> {
    return Single.just(null)
  }

  override fun chatRestore(): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT_RESTORE
        )
      )
    )
  }

  override fun chatSkipRestore(): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT_RESTORE
        )
      )
    )
  }

  override fun restore(restoreType: RestoreType): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT_RESTORE
        )
      )
    )
  }

  override fun searchMessages(
    searchedText: String
  ): Single<ArrayList<MessageRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.LOCAL_SEARCH
        )
      )
    )
  }

  override fun globalSearch(
    searchedText: String,
    threadId: String?
  ): Single<ArrayList<MessageRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.GLOBAL_SEARCH
        )
      )
    )
  }

  override fun followThread(
    threadId: String,
    messageRecord: MessageRecord,
    isFollowed: Boolean
  ): Single<MessageRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.FOLLOW_THREAD
        )
      )
    )
  }

  override fun followThread(
    threadId: String,
    messageId: String,
    isFollowed: Boolean
  ): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.FOLLOW_THREAD)
      )
    )
  }

  override fun reportMessage(
    threadId: String,
    messageRecord: MessageRecord,
    reportType: String,
    reason: String
  ): Single<MessageRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.REPORT_MESSAGE
        )
      )
    )
  }

  override fun clearChat(threadId: String): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CLEAR_CHAT
        )
      )
    )
  }

  override fun sendMessageAgain(
    threadId: String,
    msgId: String,
    parentMsgId: String?
  ): Single<MessageRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.SEND_MEDIA)
      )
    )
  }

  override fun getMediaGallery(
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?
  ): Single<MutableList<MessageRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.MEDIA)
      )
    )
  }

  override fun forwardMediaChat(
    message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String?
  ): Single<Result> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.FORWARD_CHAT
        )
      )
    )
  }

  override fun copyMessage(activity: Activity, message: String): Single<String> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.COPY)
      )
    )
  }

  override fun getFollowThreads(
    threadId: String?,
    currentMsgId: String?,
    followedThread: Boolean,
    direction: String?,
    pageSize: Int?
  ): Single<MutableList<FollowThreadRecord>> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.FOLLOW_THREAD
        )
      )
    )
  }

  override fun isChatRestored(): Boolean {
    return true
  }

  override fun getChatSettings(): Single<ChatSettingsRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.MODERATION
        )
      )
    )
  }

  override fun getProfanityFilters(): Flowable<MutableList<ProfanityDataRecord>> {
    return Flowable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.PROFANITY_FILTER
        )
      )
    )
  }

  override fun getDomainFilters(): Flowable<MutableList<DomainDataRecord>> {
    return Flowable.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.DOMAIN_FILTER
        )
      )
    )
  }

  override fun getThread(threadId: String): Single<ThreadRecord> {
    return Single.error(
      ChatSDKException(
        InvalidModule(),
        appContext.getString(
          R.string.alert_message,
          Constants.Features.CHAT
        )
      )
    )
  }

  companion object {
    fun newInstance(): ChatModule {
      return ChatModuleStub(eRTCSDK.getAppContext())
    }
  }

}