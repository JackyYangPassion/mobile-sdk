package com.ripbull.coresdk.chat.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.ripbull.coresdk.chat.ChatModule
import com.ripbull.coresdk.chat.ChatModuleImpl
import com.ripbull.coresdk.chat.ChatModuleStub
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
import com.ripbull.coresdk.core.event.EventHandler
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.ChatType.SINGLE
import com.ripbull.coresdk.core.type.ChatType.SINGLE_CHAT_THREAD
import com.ripbull.coresdk.core.type.DeleteType
import com.ripbull.coresdk.core.type.MessageType
import com.ripbull.coresdk.core.type.NetworkEvent
import com.ripbull.coresdk.core.type.RestoreType
import com.ripbull.coresdk.data.DataManager
import com.ripbull.coresdk.data.common.Result
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.thread.mapper.ThreadRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.coresdk.utils.Constants
import com.ripbull.ertc.cache.database.dao.TenantDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class ChatModuleHookImpl private constructor(
  private val chatModule: ChatModule, private val stub: ChatModule,
  private val dataManager: DataManager,
  private val tenantDao: TenantDao = dataManager.db().tenantDao()
) : ChatModuleHook {

  private fun isFeatureEnabled(feature: String): Single<Boolean> {
    return tenantDao.getTenantConfigValue(dataManager.preference().tenantId, feature)
      .flatMap { s: String ->
        Single.just(
          s == "1"
        )
      }
  }

  private fun isE2EFeatureEnabled(feature: String): Single<Boolean> {
    return tenantDao.getTenantConfigValue(dataManager.preference().tenantId, Constants.TenantConfig.E2E_CHAT)
      .flatMap { e2eEnable: String ->
        if (e2eEnable == "0") {
          Single.just(false)
        } else {
          tenantDao.getTenantConfigValue(dataManager.preference().tenantId, feature)
            .flatMap { featureEnable: String ->
              Single.just(featureEnable == "1")
            }
        }
      }
  }

  private fun canSendAlerts(): Single<Boolean> {
    return isFeatureEnabled(Constants.TenantConfig.ReadReceipts.READ)
  }

  private fun canBlockUsers(): Single<Boolean> {
    return isFeatureEnabled(Constants.TenantConfig.BLOCK_USER)
  }

  override fun provideModule(): ChatModuleHook {
    return this
  }

  override fun getThreads(): Flowable<List<ThreadRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).toFlowable()
      .flatMap { aBoolean: Boolean -> if (aBoolean) chatModule.getThreads() else stub.getThreads() }
  }

  override fun createThread(recipientId: String): Single<String> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.createThread(
        recipientId
      ) else stub.createThread(recipientId)
    }
  }

  override fun hasThread(): Single<Boolean> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean -> if (aBoolean) chatModule.hasThread() else stub.hasThread() }
  }

  override fun getMessages(
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?,
    isGlobalSearched: Boolean?
  ): Single<List<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.getMessages(threadId, currentMsgId, direction, pageSize, isGlobalSearched) else stub.getMessages(threadId, currentMsgId, direction, pageSize, isGlobalSearched)
    }
  }

  override fun getChatThreadMessages(
    threadId: String,
    parentMsgId: String?
  ): Single<MutableList<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.getChatThreadMessages(
        threadId,
        parentMsgId
      ) else stub.getChatThreadMessages(threadId, parentMsgId)
    }
  }

  override fun sendMessage(
    threadId: String,
    message: Message,
    customData: String?
  ): Single<MessageRecord> {

    if (message.parentMsgId != null) {
      return isFeatureEnabled(Constants.TenantConfig.REPLY_THREAD).flatMap { aBoolean: Boolean ->
        if (aBoolean) {
          chatMessage(threadId, message, customData = customData)
        } else {
          stub.sendMessage(threadId, message, isReplyThreadDisabled = true)
        }
      }
    }

    return chatMessage(threadId, message, customData = customData)
  }

  override fun getChatUserId(): Single<Result> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.getChatUserId() else stub.getChatUserId()
    }
  }

  @SuppressLint("CheckResult")
  override fun markAsRead(threadId: String, parentMsgId: String?): Completable {
    return canSendAlerts().flatMapCompletable { aBoolean: Boolean ->
      if (aBoolean) chatModule.markAsRead(threadId, parentMsgId) else stub.markAsRead(
        threadId,
        parentMsgId
      )
    }
  }

  override fun msgReadStatus(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    return canSendAlerts().flatMapObservable { aBoolean: Boolean ->
      if (aBoolean) chatModule.msgReadStatus(
        threadId,
        parentMsgId,
        chatType
      ) else stub.msgReadStatus(threadId, parentMsgId, chatType)
    }
  }

  override fun messageOn(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMapObservable { aBoolean: Boolean ->
      if (aBoolean) chatModule.messageOn(threadId, parentMsgId, chatType) else stub.messageOn(
        threadId,
        parentMsgId,
        chatType
      )
    }
  }

  override fun messageOn(): Observable<NetworkEvent> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMapObservable { aBoolean: Boolean ->
      if (aBoolean) chatModule.messageOn() else stub.messageOn()
    }
  }

  override fun blockUnblock(
    action: String,
    appUserId: String
  ): Single<Result> {
    return canBlockUsers().flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.blockUnblock(
        action, appUserId
      ) else stub.blockUnblock(action, appUserId)
    }
  }

  override fun markAsFavorite(
    threadId: String, list: List<MessageRecord>,
    isFavorite: Boolean
  ): Single<List<MessageRecord>> {
    return canUseStarredMessaging().flatMap { aBoolean: Boolean ->
      if (aBoolean) chatModule.markAsFavorite(
        threadId,
        list,
        isFavorite
      ) else stub.markAsFavorite(threadId, list, isFavorite)
    }
  }

  override fun getAllFavoriteMessages(): Observable<List<MessageRecord>> {
    return canUseStarredMessaging().flatMapObservable { aBoolean: Boolean ->
      if (aBoolean) chatModule.getAllFavoriteMessages() else stub.getAllFavoriteMessages()
    }
  }

  override fun getAllFavoriteMessages(threadId: String): Observable<List<MessageRecord>> {
    return canUseStarredMessaging().flatMapObservable { aBoolean: Boolean ->
      if (aBoolean) chatModule.getAllFavoriteMessages(threadId) else stub.getAllFavoriteMessages(
        threadId
      )
    }
  }

  override fun getAllFavoriteMessages(
    threadId: String,
    parentMsgId: String
  ): Flowable<List<MessageRecord>> {
    return canUseStarredMessaging().flatMapPublisher { aBoolean: Boolean ->
      if (aBoolean) {
        chatModule.getAllFavoriteMessages(threadId, parentMsgId)
      } else stub.getAllFavoriteMessages(threadId, parentMsgId)
    }
  }

  override fun downloadMedia(
    msgId: String,
    serverUrl: String,
    dirPath: String,
    mediaType: String
  ): Single<Boolean> {
    return isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_VIDEO).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.downloadMedia(
          msgId, serverUrl, dirPath, mediaType
        )
      } else {
        stub.downloadMedia(msgId, serverUrl, dirPath, mediaType)
      }
    }
  }

  override fun downloadOn(
    threadId: String,
    parentMsgId: String?,
    chatType: ChatType
  ): Observable<NetworkEvent> {
    return chatModule.downloadOn(threadId, parentMsgId, chatType)
  }

  override fun chatMetaDataOn(threadId: String): Observable<ChatMetaDataRecord> {
    return isFeatureEnabled(Constants.TenantConfig.NOTIFICATION).flatMapObservable { aBoolean ->
      if (aBoolean) chatModule.chatMetaDataOn(threadId) else stub.chatMetaDataOn(threadId)
    }
  }

  override fun sendReaction(
    threadId: String,
    messageMetaData: MessageMetaData
  ): Single<ChatReactionRecord> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_REACTIONS).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.sendReaction(threadId, messageMetaData)
      } else {
        stub.sendReaction(threadId, messageMetaData)
      }
    }
  }

  override fun messageMetaDataOn(threadId: String): Observable<MessageMetaDataRecord> {
    return chatModule.messageMetaDataOn(threadId)
  }

  override fun sourceOnMain(): Observable<ChatEvent> {
    return chatModule.sourceOnMain()
  }

  override fun forwardChat(
    messageList: List<Message>,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String?
  ): Single<Result> {
    if (messageList.isNotEmpty()) {
      val message = messageList.get(0)
      val msgType = when {
        message.message != null  -> {
          Constants.TenantConfig.E2E_CHAT_TEXT
        }
        message.giphy != null    -> {
          Constants.TenantConfig.E2E_CHAT_GIFY
        }
        message.contact != null  -> {
          Constants.TenantConfig.E2E_CHAT_CONTACT
        }
        message.location != null -> {
          Constants.TenantConfig.E2E_CHAT_LOCATION
        }
        else                     -> {
          Constants.TenantConfig.E2E_CHAT_MEDIA
        }
      }
      return isE2EFeatureEnabled(msgType).flatMap { e2e: Boolean ->
        if (e2e) {
          isFeatureEnabled(Constants.TenantConfig.FORWARD_CHAT).flatMap { aBoolean ->
            if (aBoolean) {
              chatModule.forwardChat(messageList, userList, groupList, chatType, customData, true)
            } else {
              stub.forwardChat(messageList, userList, groupList, chatType, isE2E = true)
            }
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.FORWARD_CHAT).flatMap { aBoolean ->
            if (aBoolean) {
              chatModule.forwardChat(messageList, userList, groupList, chatType, customData)
            } else {
              stub.forwardChat(messageList, userList, groupList, chatType)
            }
          }
        }
      }
    }

    return isFeatureEnabled(Constants.TenantConfig.FORWARD_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.forwardChat(messageList, userList, groupList, chatType, customData)
      } else {
        stub.forwardChat(messageList, userList, groupList, chatType)
      }
    }
  }

  override fun getMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord> {
    return chatModule.getMessage(threadId, msgId)
  }

  override fun getChatThreadMessage(
    threadId: String,
    msgId: String
  ): Single<MessageRecord> {
    return chatModule.getChatThreadMessage(threadId, msgId)
  }

  override fun deleteMessage(
    deleteType: String,
    threadId: String,
    messageList: ArrayList<Message>
  ): Single<List<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.DELETE_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        when (deleteType) {
          DeleteType.DELETE_FOR_USER.type     -> {
            isFeatureEnabled(Constants.TenantConfig.DeleteChat.DELETE_FOR_SELF).flatMap { aBoolean ->
              if (aBoolean) {
                chatModule.deleteMessage(deleteType, threadId, messageList)
              } else {
                stub.deleteMessage(deleteType, threadId, messageList)
              }
            }
          }
          DeleteType.DELETE_FOR_EVERYONE.type -> {
            isFeatureEnabled(Constants.TenantConfig.DeleteChat.DELETE_FOR_EVERYONE).flatMap { aBoolean ->
              if (aBoolean) {
                chatModule.deleteMessage(deleteType, threadId, messageList)
              } else {
                stub.deleteMessage(deleteType, threadId, messageList)
              }
            }
          }
          else                                -> {
            stub.deleteMessage(deleteType, threadId, messageList)
          }
        }
      } else {
        stub.deleteMessage(deleteType, threadId, messageList)
      }
    }

  }

  override fun editMessage(threadId: String, message: Message): Single<MessageRecord> {
    return isFeatureEnabled(Constants.TenantConfig.USER_MENTIONS).flatMap { aBoolean: Boolean ->
      if (!aBoolean && (message.mentions != null || !message.mentioned_users.isNullOrEmpty())) {
        stub.editMessage(threadId, message, isUserMentions = true)
      } else {
        isFeatureEnabled(Constants.TenantConfig.EDIT_CHAT).flatMap { aBoolean ->
          if (aBoolean) {
            isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_TEXT).flatMap { e2e: Boolean ->
              if (e2e) {
                chatModule.editE2EMessage(threadId, message)
              } else {
                isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { chatEnabled ->
                  if (chatEnabled) {
                    chatModule.editMessage(threadId,message)
                  } else {
                    stub.editMessage(threadId, message)
                  }
                }
              }
            }
          } else {
            stub.editMessage(threadId,message)
          }
        }
      }
    }
  }

  override fun chatRestore(): Single<Result> {
    return chatModule.chatRestore()
  }

  override fun chatSkipRestore(): Single<Result> {
    return chatModule.chatSkipRestore()
  }

  override fun restore(restoreType: RestoreType): Single<Result> {
    return chatModule.restore(restoreType);
  }

  override fun searchMessages(
    searchedText: String
  ): Single<ArrayList<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.LOCAL_SEARCH).flatMap { aBoolean: Boolean ->
      if (aBoolean) {
        chatModule.searchMessages(searchedText)
      } else {
        stub.searchMessages(searchedText)
      }
    }
  }

  override fun globalSearch(
    searchedText: String,
    threadId: String?
  ): Single<ArrayList<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.GLOBAL_SEARCH).flatMap { aBoolean: Boolean ->
      if (aBoolean) {
        chatModule.globalSearch(searchedText, threadId)
      } else {
        stub.globalSearch(searchedText, threadId)
      }
    }
  }

  override fun followThread(
    threadId: String,
    messageRecord: MessageRecord,
    isFollowed: Boolean
  ): Single<MessageRecord> {
    return isFeatureEnabled(Constants.TenantConfig.FOLLOW_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.followThread(threadId, messageRecord, isFollowed)
      } else {
        stub.followThread(threadId, messageRecord, isFollowed)
      }
    }
  }

  override fun followThread(
    threadId: String,
    messageId: String,
    isFollowed: Boolean
  ): Single<Result> {
    return isFeatureEnabled(Constants.TenantConfig.FOLLOW_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.followThread(threadId, messageId, isFollowed)
      } else {
        stub.followThread(threadId, messageId, isFollowed)
      }
    }
  }

  override fun reportMessage(
    threadId: String,
    messageRecord: MessageRecord,
    reportType: String,
    reason: String
  ): Single<MessageRecord> {
    //need to add stub implementation here
    return chatModule.reportMessage(threadId, messageRecord, reportType, reason)
  }

  override fun clearChat(threadId: String): Single<Result> {
    //need to add stub implementation here
    return chatModule.clearChat(threadId)
  }

  override fun sendMessageAgain(
    threadId: String,
    msgId: String,
    parentMsgId: String?
  ): Single<MessageRecord> {
    return chatModule.sendMessageAgain(threadId, msgId, parentMsgId)
  }

  override fun getMediaGallery(
    threadId: String,
    currentMsgId: String?,
    direction: String?,
    pageSize: Int?
  ): Single<List<MessageRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_IMAGE).flatMap { aBoolean: Boolean ->
      chatModule.getMediaGallery(threadId, currentMsgId, direction, pageSize)
    }
  }

  override fun forwardMediaChat(
    message: Message,
    userList: List<UserRecord>,
    groupList: List<GroupRecord>,
    chatType: ChatType,
    customData: String?
  ): Single<Result> {
    return isFeatureEnabled(Constants.TenantConfig.FORWARD_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.forwardMediaChat(message, userList, groupList, chatType, customData)
      } else {
        stub.forwardMediaChat(message, userList, groupList, chatType)
      }
    }
  }

  override fun copyMessage(activity: Activity, message: String): Single<String> {
    return isFeatureEnabled(Constants.TenantConfig.COPY).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.copyMessage(activity, message)
      } else {
        stub.copyMessage(activity, message)
      }
    }
  }

  override fun getFollowThreads(
    threadId: String?,
    currentMsgId: String?,
    followedThread: Boolean,
    direction: String?,
    pageSize: Int?
  ): Single<List<FollowThreadRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.FOLLOW_CHAT).flatMap { aBoolean ->
      if (aBoolean) {
        chatModule.getFollowThreads(threadId, currentMsgId, followedThread, direction, pageSize)
      } else {
        stub.getFollowThreads(threadId, currentMsgId, followedThread, direction, pageSize)
      }
    }
  }

  override fun isChatRestored(): Boolean {
    return chatModule.isChatRestored()
  }

  override fun getChatSettings(): Single<ChatSettingsRecord> {
    return isFeatureEnabled(Constants.TenantConfig.MODERATION).flatMap { aBoolean: Boolean ->
      if (aBoolean) {
        chatModule.getChatSettings()
      } else {
        stub.getChatSettings()
      }
    }
  }

  override fun getProfanityFilters(): Flowable<List<ProfanityDataRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.PROFANITY_FILTER).toFlowable()
      .flatMap { aBoolean: Boolean ->
        if (aBoolean) {
          chatModule.getProfanityFilters()
        } else {
          stub.getProfanityFilters()
        }
      }
  }

  override fun getDomainFilters(): Flowable<List<DomainDataRecord>> {
    return isFeatureEnabled(Constants.TenantConfig.DOMAIN_FILTER).toFlowable()
      .flatMap { aBoolean: Boolean ->
        if (aBoolean) {
          chatModule.getDomainFilters()
        } else {
          stub.getDomainFilters()
        }
      }
  }

  override fun getThread(threadId: String): Single<ThreadRecord> {
    return isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean: Boolean ->
      if (aBoolean) {
        chatModule.getThread(threadId)
      } else {
        stub.getThread(threadId)
      }
    }
  }

  private fun canUseStarredMessaging(): Single<Boolean> {
    return isFeatureEnabled(Constants.TenantConfig.STAR_MESSAGE)
  }

  //this function is used for main chat and chat thread
  private fun chatMessage(threadId: String, message: Message, customData: String?): Single<MessageRecord> {
    when {
      MessageType.IMAGE == message.media?.type       -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_IMAGE).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_IMAGE).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        }
      }
      MessageType.AUDIO == message.media?.type       -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_AUDIO).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_AUDIO).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        }
      }
      MessageType.VIDEO == message.media?.type       -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_VIDEO).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_VIDEO).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        }
      }
      MessageType.GIF == message.media?.type         -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_GIPHY).flatMap { aBoolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_GIPHY).flatMap { aBoolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        }
      }
      MessageType.GIPHY == message.giphy?.type       -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_GIFY).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_GIPHY).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        } else {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_GIFY).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_GIPHY).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        }
      }
      MessageType.CONTACT == message.contact?.type   -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_CONTACT).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_CONTACT).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        } else {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_CONTACT).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_CONTACT).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        }
      }
      MessageType.LOCATION == message.location?.type -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_LOCATION).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_LOCATION).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        } else {
          isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_LOCATION).flatMap { aBoolean: Boolean ->
            if (aBoolean) {
              chatModule.sendE2EMessage(
                threadId, message, customData
              )
            } else {
              isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_LOCATION).flatMap { aBoolean ->
                if (aBoolean) chatModule.sendMessage(
                  threadId, message, customData
                ) else stub.sendMessage(threadId, message)
              }
            }
          }
        }
      }
      MessageType.FILE == message.file?.type         -> {
        return if (message.chatType == SINGLE || message.chatType == SINGLE_CHAT_THREAD) {
          isFeatureEnabled(Constants.TenantConfig.SINGLE_CHAT_DOCUMENT).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        } else {
          isFeatureEnabled(Constants.TenantConfig.GROUP_CHAT_DOCUMENT).flatMap { aBoolean: Boolean ->
            if (aBoolean) chatModule.sendMessage(
              threadId, message, customData
            ) else stub.sendMessage(threadId, message)
          }
        }
      }
      else                                           -> {
        return isFeatureEnabled(Constants.TenantConfig.USER_MENTIONS).flatMap { aBoolean: Boolean ->
          if (!aBoolean && (message.mentions != null || !message.mentioned_users.isNullOrEmpty())) {
            stub.sendMessage(threadId, message, isUserMentions = true)
          } else {
            isE2EFeatureEnabled(Constants.TenantConfig.E2E_CHAT_TEXT).flatMap { aBoolean: Boolean ->
              if (aBoolean) {
                chatModule.sendE2EMessage(
                  threadId, message, customData
                )
              } else {
                isFeatureEnabled(Constants.TenantConfig.CHAT_ENABLED).flatMap { aBoolean ->
                  if (aBoolean) chatModule.sendMessage(
                    threadId, message, customData
                  ) else stub.sendMessage(threadId, message)
                }
              }
            }
          }
        }
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance(
      dataManager: DataManager,
      eventHandler: EventHandler?
    ): ChatModuleHook {
      val chatModule = ChatModuleImpl.newInstance(dataManager, eventHandler)
      val stub = ChatModuleStub.newInstance()
      return ChatModuleHookImpl(chatModule, stub, dataManager)
    }
  }

}