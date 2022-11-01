package com.ripbull.coresdk.chat.mapper

import com.ripbull.coresdk.core.type.ChatEventType
import com.ripbull.coresdk.core.type.MessageUpdateType
import com.ripbull.coresdk.extension.isEqual
import com.ripbull.coresdk.fcm.NotificationRecord
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserRecord
import com.ripbull.ertc.cache.database.entity.ChatThread
import com.ripbull.ertc.cache.database.entity.EmailContact
import com.ripbull.ertc.cache.database.entity.PhoneContact
import com.ripbull.ertc.cache.database.entity.SingleChat
import com.ripbull.ertc.remote.model.response.MessageResponse

/** Created by DK on 23/02/19.  */
/**
 * It maps response/entity to entity.i.e return record
 */
object ChatRecordMapper {

  fun transformDelete(
    chatThread: ChatThread,
    senderRecord: UserRecord? = null,
    receiverRecord: UserRecord? = null,
    groupRecord: GroupRecord? = null,
    notificationRecord: NotificationRecord? = null,
    chatEventType: ChatEventType? = ChatEventType.OUTGOING,
    chatReactions : List<ChatReactionRecord>? = null,
    isSilent : Boolean? = false
  ): MessageRecord {

    return MessageRecord(
      chatThread.id,
      chatThread.createdAt,
      chatThread.message,
      chatThread.read!!,
      chatThread.type,
      chatThread.threadId,
      chatThread.nextMessageId,
      chatThread.lastMessageId,
      chatThread.status,
      chatThread.msgType,
      chatThread.senderAppUserId,
      chatThread.mediaPath,
      chatThread.mediaThumbnail,
      LocationRecord(chatThread.location?.address, chatThread.location?.latitude, chatThread.location?.longitude),
      chatThread.contactName,
      convertToRecordPhoneContactList(chatThread.phoneContactList),
      convertToRecordEmailContactList(chatThread.emailContactList),
      isFavoriteMessage = isEqual(chatThread.isStarredChat),
      gifPath = chatThread.gifPath,
      senderRecord = senderRecord,
      receiverRecord = receiverRecord,
      groupRecord = groupRecord,
      mediaName = chatThread.mediaName,
      notificationRecord = notificationRecord,
      localMediaPath = chatThread.localMediaPath,
      downloadStatus = chatThread.downloadStatus,
      chatThreadMetadata = ThreadMessageMetadata(
        parentMsgId = chatThread.parentMsgId,
        sendToChannel = chatThread.sendToChannel
      ),
      chatEventType = chatEventType,
      reactions = chatReactions,
      isSilent = isSilent,
      updateType = getMsgUpdateType(chatThread.updateType),
      messageDeleteType = chatThread.deleteType!!,
      clientCreatedAt = chatThread.clientCreatedAt,
      customData = chatThread.customData
    )
  }

  fun transform(
    singleChat: SingleChat,
    senderRecord: UserRecord? = null,
    receiverRecord: UserRecord? = null,
    groupRecord: GroupRecord? = null,
    notificationRecord: NotificationRecord? = null,
    chatThreadList: List<ChatThread>? = null,
    chatEventType: ChatEventType? = ChatEventType.OUTGOING,
    chatReactions: List<ChatReactionRecord>? = null,
    isSilent: Boolean? = false
  ): MessageRecord {
    var chatThreadMetadata: ThreadMessageMetadata? = null
    if (singleChat.sendToChannel != null && singleChat.sendToChannel == 1) {
      chatThreadMetadata = ThreadMessageMetadata(
        parentMsgId = singleChat.parentMsgId,
        parentMsg = singleChat.parentMsg,
        sendToChannel = singleChat.sendToChannel
      )
    } else {
      if (chatThreadList != null && chatThreadList.isNotEmpty()) {
        //val chatThread = chatThreadList[0]
        chatThreadMetadata = ThreadMessageMetadata(
          chatThreadCount = chatThreadList.size,
          sendToChannel = singleChat.sendToChannel
        )
      }
    }
    return MessageRecord(
      singleChat.id,
      singleChat.clientCreatedAt,
      singleChat.message,
      singleChat.read!!,
      singleChat.type,
      singleChat.threadId,
      singleChat.nextMessageId,
      singleChat.lastMessageId,
      singleChat.status,
      singleChat.msgType,
      singleChat.senderAppUserId,
      singleChat.mediaPath,
      singleChat.mediaThumbnail,
      LocationRecord(
        singleChat.location?.address,
        singleChat.location?.latitude,
        singleChat.location?.longitude
      ),
      singleChat.contactName,
      convertToRecordPhoneContactList(singleChat.phoneContactList),
      convertToRecordEmailContactList(singleChat.emailContactList),
      isFavoriteMessage = isEqual(singleChat.isStarredChat),
      gifPath = singleChat.gifPath,
      senderRecord = senderRecord,
      receiverRecord = receiverRecord,
      groupRecord = groupRecord,
      mediaName = singleChat.mediaName,
      notificationRecord = notificationRecord,
      localMediaPath = singleChat.localMediaPath,
      downloadStatus = singleChat.downloadStatus,
      chatThreadMetadata = chatThreadMetadata,
      chatEventType = chatEventType,
      reactions = chatReactions,
      isSilent = isSilent,
      isForwardMessage = singleChat.isForwardMessage == 1,
      updateType = getMsgUpdateType(singleChat.updateType),
      clientCreatedAt = singleChat.clientCreatedAt,
      customData = singleChat.customData,
      isFollowThread = singleChat.isFollowed == 1,
      chatReportId = singleChat.chatReportId,
      reportType = singleChat.reportType,
      reason = singleChat.reason,
      mentions = singleChat.mentions,
      mentionedUsers = singleChat.mentionedUsers
    )
  }

  private fun getMsgUpdateType(updateType : String?) : MessageUpdateType?{
    if (updateType == MessageUpdateType.DELETE.type){
      return MessageUpdateType.DELETE
    } else if(updateType == MessageUpdateType.EDIT.type){
      return MessageUpdateType.EDIT
    } else if(updateType == MessageUpdateType.FAVORITE.type){
      return MessageUpdateType.FAVORITE
    }
    return null
  }

  fun transform(
    chatThread: ChatThread,
    senderRecord: UserRecord? = null,
    receiverRecord: UserRecord? = null,
    groupRecord: GroupRecord? = null,
    notificationRecord: NotificationRecord? = null,
    chatEventType: ChatEventType? = ChatEventType.OUTGOING,
    chatReactions: List<ChatReactionRecord>? = null,
    isSilent: Boolean? = false
  ): MessageRecord {

    return MessageRecord(
      chatThread.id,
      chatThread.createdAt,
      chatThread.message,
      chatThread.read!!,
      chatThread.type,
      chatThread.threadId,
      chatThread.nextMessageId,
      chatThread.lastMessageId,
      chatThread.status,
      chatThread.msgType,
      chatThread.senderAppUserId,
      chatThread.mediaPath,
      chatThread.mediaThumbnail,
      LocationRecord(
        chatThread.location?.address,
        chatThread.location?.latitude,
        chatThread.location?.longitude
      ),
      chatThread.contactName,
      convertToRecordPhoneContactList(chatThread.phoneContactList),
      convertToRecordEmailContactList(chatThread.emailContactList),
      isFavoriteMessage = isEqual(chatThread.isStarredChat),
      gifPath = chatThread.gifPath,
      senderRecord = senderRecord,
      receiverRecord = receiverRecord,
      groupRecord = groupRecord,
      mediaName = chatThread.mediaName,
      notificationRecord = notificationRecord,
      localMediaPath = chatThread.localMediaPath,
      downloadStatus = chatThread.downloadStatus,
      chatThreadMetadata = ThreadMessageMetadata(
        parentMsgId = chatThread.parentMsgId,
        sendToChannel = chatThread.sendToChannel
      ),
      chatEventType = chatEventType,
      reactions = chatReactions,
      isSilent = isSilent,
      updateType = getMsgUpdateType(chatThread.updateType),
      clientCreatedAt = chatThread.clientCreatedAt,
      customData = chatThread.customData,
      chatReportId = chatThread.chatReportId,
      reportType = chatThread.reportType,
      reason = chatThread.reason,
      mentions = chatThread.mentions,
      mentionedUsers = chatThread.mentionedUsers
    )
  }

  fun transform(
    messageResponse: MessageResponse,
    senderRecord: UserRecord? = null,
    receiverRecord: UserRecord? = null,
    groupRecord: GroupRecord? = null,
    threadType: String? = null,
    notificationRecord: NotificationRecord? = null,
    chatThreadList: List<ChatThread>? = null,
    chatEventType: ChatEventType? = ChatEventType.OUTGOING,
    chatReactions: List<ChatReactionRecord>? = null,
    isSilent: Boolean? = false
  ): MessageRecord {
    return MessageRecord(
      id = messageResponse.msgUniqueId,
      timestamp = messageResponse.senderTimeStampMs,
      message = messageResponse.message,
      threadId = messageResponse.threadId,
      type = threadType,
      msgType = messageResponse.msgType,
      senderId = senderRecord?.id,
      mediaPath = messageResponse.media?.path,
      mediaThumbnail = messageResponse.media?.thumbnail,
      senderRecord = senderRecord,
      receiverRecord = receiverRecord,
      groupRecord = groupRecord,
      mediaName = messageResponse.media?.name,
      notificationRecord = notificationRecord,
      chatEventType = chatEventType,
      reactions = chatReactions,
      isSilent = isSilent,
      customData = messageResponse.customData
    )
  }

  private fun convertToRecordPhoneContactList(list: List<PhoneContact>?): List<PhoneContactRecord> {
    val record = ArrayList<PhoneContactRecord>()
    if (list != null) {
      for (phoneContact in list) {
        record.add(PhoneContactRecord(phoneContact.type, phoneContact.number))
      }
    }
    return record
  }

  private fun convertToRecordEmailContactList(list: List<EmailContact>?): List<EmailContactRecord> {
    val record = ArrayList<EmailContactRecord>()
    if (list != null) {
      for (emailContact in list) {
        record.add(EmailContactRecord(emailContact.type, emailContact.email))
      }
    }
    return record
  }

}
