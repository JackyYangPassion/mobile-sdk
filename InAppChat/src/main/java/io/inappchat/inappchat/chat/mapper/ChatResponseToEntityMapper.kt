package io.inappchat.inappchat.chat.mapper

import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MessageStatus
import io.inappchat.inappchat.cache.database.entity.ChatThread
import io.inappchat.inappchat.cache.database.entity.EmailContact
import io.inappchat.inappchat.cache.database.entity.Location
import io.inappchat.inappchat.cache.database.entity.PhoneContact
import io.inappchat.inappchat.cache.database.entity.SingleChat
import io.inappchat.inappchat.cache.database.entity.Thread
import io.inappchat.inappchat.remote.model.response.Email
import io.inappchat.inappchat.remote.model.response.MessageResponse
import io.inappchat.inappchat.remote.model.response.PhoneNumber
import java.util.*

/**
 * Created by DK on 13/06/20.
 */
/**
 * It maps server response to entity.i.e return entity
 */
object ChatResponseToEntityMapper {

  // Chat response to entity mapping
  fun getChatRow(
    thread: Thread?,
    message: MessageResponse,
    msgId: String = UUID.randomUUID().toString(),
    chatEventType: String,
    senderUserId: String?,
    parentMsgId: String? = null, // only when send to channel
    parentMsg: String? = null,
    baseUrl: String? = null
  ): SingleChat {
    var createdAt = System.currentTimeMillis()
    message.senderTimeStampMs?.let { createdAt = it }

    if (message.media != null) {
      return chatRowCompose(
        thread = thread!!,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId =  message.msgUniqueId,
        mediaPath = baseUrl + message.media!!.path,
        mediaThumbnail = baseUrl + message.media!!.thumbnail,
        senderUserId = senderUserId,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        isForwardMessage = message.forwardChatFeature?.isForwarded,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }

    if (message.gify != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId =  message.msgUniqueId,
        gifPath = message.gify,
        senderUserId = senderUserId,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        isForwardMessage = message.forwardChatFeature?.isForwarded,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }
    if (message.contact != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId =  message.msgUniqueId,
        contactName = message.contact!!.name,
        phoneNumberRecord = message.contact!!.numbersList,
        eMailRecords = message.contact!!.emailsList,
        senderUserId = senderUserId,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        isForwardMessage = message.forwardChatFeature?.isForwarded,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }
    if (message.location != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId =  message.msgUniqueId,
        address = message.location!!.address,
        latitude = message.location!!.latitude,
        longitude = message.location!!.longitude,
        senderUserId = senderUserId,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        isForwardMessage = message.forwardChatFeature?.isForwarded,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }

    return chatRowCompose(
      thread = thread!!,
      message = message.message,
      msgId = msgId,
      msgType = message.msgType,
      msgStatus = MessageStatus.DELIVERED.status,
      createdAt = createdAt,
      isRead = true,
      msgUniqueId = message.msgUniqueId,
      senderUserId = senderUserId,
      sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
      parentMsgId = parentMsgId,
      parentMsg = parentMsg,
      chatEventType = chatEventType,
      isForwardMessage = message.forwardChatFeature?.isForwarded,
      clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
      customData = message.customData,
      isFavoriteMessage = message.isStarred
    )
  }


  private fun chatRowCompose(
    thread: Thread?,
    message: String? = null,
    msgId: String,
    msgType: String,
    msgStatus: String,
    createdAt: Long,
    isRead: Boolean,
    msgUniqueId: String? = "",
    mediaPath: String? = null,
    mediaThumbnail: String? = null,
    mediaName: String? = null,
    address: String? = null,
    latitude: Double? = null,
    longitude: Double? = null,
    contactName: String? = null,
    phoneNumberRecord: List<PhoneNumber?>? = null,
    eMailRecords: List<Email?>? = null,
    gifPath: String? = null,
    senderUserId: String? = null,
    sendToChannel: Int? = null,
    parentMsgId: String? = null,
    parentMsg: String? = null,
    chatEventType: String,
    forwardMsgUniqueId: String? = null,
    isForwardMessage: Boolean? = false,
    clientCreatedAt: Long? = null,
    customData: String? = null,
    isFavoriteMessage: Boolean? = false,
  ): SingleChat { // Text Message`

    return SingleChat(
      id = msgId,
      createdAt = createdAt,
      message = message,
      read = if (isRead) 1 else 0,
      type = thread?.type,
      senderAppUserId = senderUserId,
      threadId = thread?.id,
      status = msgStatus,
      msgType = msgType,
      msgUniqueId = msgUniqueId,
      mediaPath = mediaPath,
      mediaThumbnail = mediaThumbnail,
      mediaName = mediaName,
      contactName = contactName,
      phoneContactList = convertToEntityPhoneContactList(phoneNumberRecord),
      emailContactList = convertToEntityEmailContactList(eMailRecords),
      location = Location(address, latitude, longitude),
      gifPath = gifPath,
      sendToChannel = sendToChannel,
      parentMsgId = parentMsgId,
      parentMsg = parentMsg,
      chatEventType = chatEventType,
      forwardMsgUniqueId = forwardMsgUniqueId,
      isForwardMessage = if (isForwardMessage != null && isForwardMessage) 1 else 0,
      clientCreatedAt = clientCreatedAt,
      customData = customData,
      isStarredChat = if (isFavoriteMessage != null && isFavoriteMessage) "1" else "0"
    )
  }

  // Thread response to entity mapping
  fun getChatThreadRow(
    thread: Thread?,
    message: MessageResponse,
    msgId: String = UUID.randomUUID().toString(),
    parentMsgId: String,
    senderUserId: String?,
    baseUrl : String? = null
  ): ChatThread {
    var createdAt = System.currentTimeMillis()
    message.createdAt?.let { createdAt = it }

    if (message.media != null) {
      return chatThreadRowCompose(
        thread = thread!!,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId = message.replyThreadFeatureData?.parentMsgId,
        mediaPath = baseUrl + message.media!!.path,
        mediaThumbnail = baseUrl + message.media!!.thumbnail,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        senderUserId = senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }

    if (message.gify != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId = message.replyThreadFeatureData?.parentMsgId,
        gifPath = message.gify,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        senderUserId = senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }
    if (message.contact != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId = message.replyThreadFeatureData?.parentMsgId,
        contactName = message.contact!!.name,
        phoneNumberRecord = message.contact!!.numbersList,
        eMailRecords = message.contact!!.emailsList,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        senderUserId = senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }
    if (message.location != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.msgType,
        msgStatus = MessageStatus.DELIVERED.status,
        createdAt = createdAt,
        isRead = true,
        msgUniqueId = msgId,
        address = message.location!!.address,
        latitude = message.location!!.latitude,
        longitude = message.location!!.longitude,
        sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
        senderUserId = senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
        customData = message.customData,
        isFavoriteMessage = message.isStarred
      )
    }

    return chatThreadRowCompose(
      thread = thread!!,
      message = message.message,
      msgId = msgId,
      msgType = message.msgType,
      msgStatus = MessageStatus.DELIVERED.status,
      createdAt = createdAt,
      isRead = true,
      msgUniqueId = msgId,
      parentMsgId = parentMsgId,
      sendToChannel = message.replyThreadFeatureData?.replyMsgConfig,
      senderUserId = senderUserId,
      clientCreatedAt = if (message.senderTimeStampMs != null) message.senderTimeStampMs else createdAt,
      customData = message.customData,
      isFavoriteMessage = message.isStarred
    )
  }

  private fun chatThreadRowCompose(
    thread: Thread?,
    message: String? = null,
    msgId: String,
    msgType: String,
    msgStatus: String,
    createdAt: Long,
    isRead: Boolean,
    msgUniqueId: String? = "Test",
    parentMsgId: String? = null,
    mediaPath: String? = null,
    mediaThumbnail: String? = null,
    mediaName: String? = null,
    address: String? = null,
    latitude: Double? = null,
    longitude: Double? = null,
    contactName: String? = null,
    phoneNumberRecord: List<PhoneNumber?>? = null,
    eMailRecords: List<Email?>? = null,
    gifPath: String? = null,
    sendToChannel: Int? = null,
    senderUserId: String? = null,
    clientCreatedAt: Long? = null,
    customData: String? = null,
    isFavoriteMessage: Boolean? = false,
  ): ChatThread { // Text Message`
    return ChatThread(
      id = msgId,
      createdAt = createdAt,
      message = message,
      read = if (isRead) 1 else 0,
      type = if (thread?.type == ChatType.SINGLE.type) ChatType.SINGLE_CHAT_THREAD.type else ChatType.GROUP_CHAT_THREAD.type,
      senderAppUserId = senderUserId,
      threadId = thread?.id,
      status = msgStatus,
      msgType = msgType,
      msgUniqueId = msgUniqueId,
      mediaPath = mediaPath,
      mediaThumbnail = mediaThumbnail,
      mediaName = mediaName,
      contactName = contactName,
      phoneContactList = convertToEntityPhoneContactList(phoneNumberRecord),
      emailContactList = convertToEntityEmailContactList(eMailRecords),
      location = Location(address, latitude, longitude),
      gifPath = gifPath,
      sendToChannel = sendToChannel,
      parentMsgId = parentMsgId,
      clientCreatedAt = clientCreatedAt,
      customData = customData,
      isStarredChat = if (isFavoriteMessage != null && isFavoriteMessage) "1" else "0"
    )
  }

  private fun convertToEntityPhoneContactList(list: List<PhoneNumber?>?): List<PhoneContact> {
    val phoneContactList = ArrayList<PhoneContact>()
    if (list != null) {
      for (phoneRecord in list) {
        phoneContactList.add(PhoneContact(phoneRecord?.type, phoneRecord?.number))
      }
    }
    return phoneContactList
  }

  private fun convertToEntityEmailContactList(list: List<Email?>?): List<EmailContact> {
    val emailContactList = ArrayList<EmailContact>()
    if (list != null) {
      for (mailRecord in list) {
        emailContactList.add(EmailContact(mailRecord?.type, mailRecord?.email))
      }
    }
    return emailContactList
  }

}