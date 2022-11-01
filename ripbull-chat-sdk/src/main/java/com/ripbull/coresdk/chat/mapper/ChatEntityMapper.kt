package com.ripbull.coresdk.chat.mapper

import android.text.TextUtils
import com.google.gson.Gson
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.core.type.ChatEventType
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MessageStatus
import com.ripbull.coresdk.core.type.MessageType
import com.ripbull.coresdk.e2e.ECDHUtils
import com.ripbull.coresdk.group.model.Event
import com.ripbull.coresdk.group.model.EventTriggeredOnUsers
import com.ripbull.coresdk.group.model.GroupUpdate
import com.ripbull.ertc.cache.database.dao.EKeyDao
import com.ripbull.ertc.cache.database.dao.UserDao
import com.ripbull.ertc.cache.database.entity.ChatReactionEntity
import com.ripbull.ertc.cache.database.entity.ChatThread
import com.ripbull.ertc.cache.database.entity.EmailContact
import com.ripbull.ertc.cache.database.entity.Location
import com.ripbull.ertc.cache.database.entity.PhoneContact
import com.ripbull.ertc.cache.database.entity.SingleChat
import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.preference.PreferenceManager
import com.ripbull.ertc.mqtt.model.ChatTopicResponse
import com.ripbull.ertc.mqtt.model.Contact
import com.ripbull.ertc.mqtt.model.EncryptedChat
import com.ripbull.ertc.remote.model.request.ForwardChat
import com.ripbull.ertc.remote.model.response.MessageResponse
import java.util.*

/**
 * Created by DK on 13/06/20.
 */
/**
 * It maps request/MQTT response/record to entity.i.e return entity
 */
object ChatEntityMapper {

  // Sender end
  fun getChatRow(
    thread: Thread?,
    message: Message,
    msgId: String,
    parentMsgId: String? = null,
    parentMsg: String? = null,
    customData: String?,
    forwardChat: ForwardChat? = null
  ): SingleChat {
    if (message.media != null) {
      return chatRowCompose(
        thread = thread!!,
        msgId = msgId,
        msgType = message.media.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        mediaPath = message.media.mediaPath,
        mediaThumbnail = message.media.mediaPath,
        senderUserId = thread.senderUserId,
        sendToChannel = message.sendToChannel,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = ChatEventType.OUTGOING.type,
        localMediaPath = message.media.mediaPath,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        isFollowThread = 1,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString(),
        forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
        isForwardMessage = forwardChat?.isForwardMessage
      )
    }
    if (message.file != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.file.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        mediaPath = message.file.filePath,
        senderUserId = thread?.senderUserId,
        sendToChannel = message.sendToChannel,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = ChatEventType.OUTGOING.type,
        localMediaPath = message.file.filePath,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        isFollowThread = 1,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString(),
        forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
        isForwardMessage = forwardChat?.isForwardMessage
      )
    }
    if (message.giphy != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.giphy.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        gifPath = message.giphy.gifPath,
        senderUserId = thread?.senderUserId,
        sendToChannel = message.sendToChannel,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = ChatEventType.OUTGOING.type,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        isFollowThread = 1,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString(),
        forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
        isForwardMessage = forwardChat?.isForwardMessage
      )
    }
    if (message.contact != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.contact.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        contactName = message.contact.contactName,
        phoneNumberRecord = message.contact.phoneNumberRecord,
        eMailRecords = message.contact.eMailRecords,
        senderUserId = thread?.senderUserId,
        sendToChannel = message.sendToChannel,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = ChatEventType.OUTGOING.type,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        isFollowThread = 1,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString(),
        forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
        isForwardMessage = forwardChat?.isForwardMessage
      )
    }
    if (message.location != null) {
      return chatRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.location.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        address = message.location.address,
        latitude = message.location.latitude,
        longitude = message.location.longitude,
        senderUserId = thread?.senderUserId,
        sendToChannel = message.sendToChannel,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = ChatEventType.OUTGOING.type,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        isFollowThread = 1,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString(),
        forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
        isForwardMessage = forwardChat?.isForwardMessage
      )
    }

    return chatRowCompose(
      thread = thread!!,
      message = message.message,
      msgId = msgId,
      msgType = MessageType.TEXT.type,
      msgStatus = MessageStatus.SENDING.status,
      createdAt = System.currentTimeMillis(),
      isRead = true,
      msgUniqueId = msgId,
      senderUserId = thread.senderUserId,
      sendToChannel = message.sendToChannel,
      parentMsgId = parentMsgId,
      parentMsg = parentMsg,
      chatEventType = ChatEventType.OUTGOING.type,
      clientCreatedAt = message.clientCreatedAt,
      customData = customData,
      isFollowThread = 1,
      mentions = message.mentions?.type,
      mentionedUsers = message.mentioned_users.toString(),
      forwardMsgUniqueId = forwardChat?.forwardMsgUniqueId,
      isForwardMessage = forwardChat?.isForwardMessage
    )
  }

  fun getChatThreadRow(
    thread: Thread?,
    message: Message,
    msgId: String,
    parentMsgId: String,
    customData: String?
  ): ChatThread {
    if (message.media != null) {
      return chatThreadRowCompose(
        thread = thread!!,
        msgId = msgId,
        msgType = message.media.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        mediaPath = message.media.mediaPath,
        mediaThumbnail = message.media.mediaPath,
        sendToChannel = message.sendToChannel,
        senderUserId = thread.senderUserId,
        parentMsgId = parentMsgId,
        localMediaPath = message.media.mediaPath,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString()
      )
    }
    if (message.file != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.file.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        mediaPath = message.file.filePath,
        sendToChannel = message.sendToChannel,
        senderUserId = thread?.senderUserId,
        parentMsgId = parentMsgId,
        localMediaPath = message.file.filePath,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString()
      )
    }
    if (message.giphy != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.giphy.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        gifPath = message.giphy.gifPath,
        sendToChannel = message.sendToChannel,
        senderUserId = thread?.senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString()
      )
    }
    if (message.contact != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.contact.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        contactName = message.contact.contactName,
        phoneNumberRecord = message.contact.phoneNumberRecord,
        eMailRecords = message.contact.eMailRecords,
        sendToChannel = message.sendToChannel,
        senderUserId = thread?.senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString()
      )
    }
    if (message.location != null) {
      return chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        msgType = message.location.type.type,
        msgStatus = MessageStatus.SENDING.status,
        createdAt = System.currentTimeMillis(),
        isRead = true,
        msgUniqueId = msgId,
        address = message.location.address,
        latitude = message.location.latitude,
        longitude = message.location.longitude,
        sendToChannel = message.sendToChannel,
        senderUserId = thread?.senderUserId,
        parentMsgId = parentMsgId,
        clientCreatedAt = message.clientCreatedAt,
        customData = customData,
        mentions = message.mentions?.type,
        mentionedUsers = message.mentioned_users.toString()
      )
    }

    return chatThreadRowCompose(
      thread = thread!!,
      message = message.message,
      msgId = msgId,
      msgType = MessageType.TEXT.type,
      msgStatus = MessageStatus.SENDING.status,
      createdAt = System.currentTimeMillis(),
      isRead = true,
      msgUniqueId = msgId,
      parentMsgId = parentMsgId,
      sendToChannel = message.sendToChannel,
      senderUserId = thread.senderUserId,
      clientCreatedAt = message.clientCreatedAt,
      customData = customData,
      mentions = message.mentions?.type,
      mentionedUsers = message.mentioned_users.toString()
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
    phoneNumberRecord: List<PhoneContactRecord?>? = null,
    eMailRecords: List<EmailContactRecord?>? = null,
    gifPath: String? = null,
    senderUserId: String? = null,
    sendToChannel: Int? = null,
    parentMsgId: String? = null,
    parentMsg: String? = null,
    chatEventType: String,
    forwardMsgUniqueId: String? = null,
    isForwardMessage: Boolean? = false,
    localMediaPath: String? = null,
    clientCreatedAt: Long? = null,
    customData: String? = null,
    isFollowThread: Int? = 0,
    mentions: String? = null,
    mentionedUsers: String? = null
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
      localMediaPath = localMediaPath,
      clientCreatedAt = clientCreatedAt,
      customData = customData,
      isFollowed = isFollowThread,
      mentions = mentions,
      mentionedUsers = mentionedUsers
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
    phoneNumberRecord: List<PhoneContactRecord?>? = null,
    eMailRecords: List<EmailContactRecord?>? = null,
    gifPath: String? = null,
    sendToChannel: Int? = null,
    senderUserId: String? = null,
    localMediaPath: String? = null,
    clientCreatedAt: Long? = null,
    customData: String? = null,
    mentions: String? = null,
    mentionedUsers: String? = null
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
      localMediaPath = localMediaPath,
      clientCreatedAt = clientCreatedAt,
      customData = customData,
      mentions = mentions,
      mentionedUsers = mentionedUsers
    )
  }

  // Receiver end
  fun getChatRow(
    response: ChatTopicResponse,
    thread: Thread? = null,
    preferenceManager: PreferenceManager? = null,
    eKeyDao: EKeyDao? = null,
    parentMsgId: String? = null,
    parentMsg: String? = null,
    msgId: String
  ): SingleChat {
    val msgType = response.msgType
    var encryptedChat: EncryptedChat? = null
    var privateKey: String? = null
    var publicKey: String? = null
    val isE2E: Boolean = response.encryptedChat != null
    val chatEventType: String?
    val messageStatus: MessageStatus?

    if (response.sender.appUserId == preferenceManager?.appUserId) {
      messageStatus = MessageStatus.SENT
      chatEventType = ChatEventType.OUTGOING.type
    } else {
      messageStatus = MessageStatus.DELIVERED
      chatEventType = ChatEventType.INCOMING.type
    }
    if (isE2E) {
      encryptedChat = response.encryptedChat
      val myKeyTable = eKeyDao?.getPrivateKeyByKeyId(
        Objects.requireNonNull(encryptedChat)!!.eRTCUserId,
        encryptedChat!!.deviceId, response.tenantId, encryptedChat.keyId
      )
      privateKey = myKeyTable?.privateKey
      publicKey = Objects.requireNonNull(response.senderKeyDetails)!!.publicKey
    }
    var singleChat: SingleChat? = null
    if (msgType == MessageType.IMAGE.type || msgType == MessageType.AUDIO.type
      || msgType == MessageType.VIDEO.type || msgType == MessageType.FILE.type
      || msgType == MessageType.GIF.type
    ) {
      if (!TextUtils.isEmpty(response.media?.path)) {
        val chatServer = preferenceManager?.chatServer
        singleChat = chatRowCompose(
          thread = thread,
          message = response.message,
          msgId = msgId,
          isRead = true,
          createdAt = response.senderTimeStampMs,
          msgStatus = messageStatus.status,
          msgType = response.msgType,
          msgUniqueId = response.msgUniqueId,
          senderUserId = response.sender.appUserId,
          mediaPath = chatServer + response.media?.path,
          mediaThumbnail = chatServer + response.media?.thumbnail,
          mediaName = response.media?.name,
          sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
          parentMsgId = parentMsgId,
          parentMsg = parentMsg,
          chatEventType = chatEventType,
          forwardMsgUniqueId = response.forwardChatFeatureData?.originalMsgUniqueId,
          isForwardMessage = response.forwardChatFeatureData?.isForwarded,
          clientCreatedAt = response.senderTimeStampMs,
          customData = response.customData,
          isFollowThread = response.isFollowThread
        )
      }
    } else if (msgType == MessageType.LOCATION.type) {
      val location: Location
      location = if (isE2E) {
        val decryptedMessage = ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.location), publicKey,
          privateKey
        )
        Gson().fromJson(
          decryptedMessage,
          Location::class.java
        )
      } else {
        Location(
          Objects.requireNonNull(response.location)!!.address,
          response.location!!.latitude, response.location!!.longitude
        )
      }
      singleChat = chatRowCompose(
        thread = thread,
        message = response.message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        senderUserId = response.sender.appUserId,
        address = location.address,
        latitude = location.latitude,
        longitude = location.longitude,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        forwardMsgUniqueId = response.forwardChatFeatureData?.originalMsgUniqueId,
        isForwardMessage = response.forwardChatFeatureData?.isForwarded,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData,
        isFollowThread = response.isFollowThread
      )

    } else if (msgType == MessageType.CONTACT.type) {
      val contact = if (isE2E) {
        val decryptedMessage = ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.contact), publicKey,
          privateKey
        )
        Gson()
          .fromJson(
            decryptedMessage,
            Contact::class.java
          )
      } else {
        response.contact
      }
      val phoneRecord: ArrayList<PhoneContactRecord>? = ArrayList()
      val emailRecord: ArrayList<EmailContactRecord>? = ArrayList()
      contact?.numbers?.let {
        for (phone in it) {
          phoneRecord?.add(PhoneContactRecord(phone.type, phone.number))
        }
      }

      contact?.emails?.let {
        for (email in it) {
          emailRecord?.add(EmailContactRecord(email.type, email.email))
        }
      }
      singleChat = chatRowCompose(
        thread = thread,
        message = response.message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        senderUserId = response.sender.appUserId,
        contactName = contact?.name,
        phoneNumberRecord = phoneRecord,
        eMailRecords = emailRecord,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        forwardMsgUniqueId = response.forwardChatFeatureData?.originalMsgUniqueId,
        isForwardMessage = response.forwardChatFeatureData?.isForwarded,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData,
        isFollowThread = response.isFollowThread
      )

    } else if (msgType == MessageType.GIPHY.type) {
      val gifPath = if (isE2E) {
        ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.gify), publicKey,
          privateKey
        )
      } else {
        response.gify
      }
      singleChat = chatRowCompose(
        thread = thread,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        senderUserId = response.sender.appUserId,
        gifPath = gifPath,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        forwardMsgUniqueId = response.forwardChatFeatureData?.originalMsgUniqueId,
        isForwardMessage = response.forwardChatFeatureData?.isForwarded,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData,
        isFollowThread = response.isFollowThread
      )
    } else {
      val message = if (isE2E) {
        ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.message), publicKey,
          privateKey
        )
      } else {
        response.message
      }
      singleChat = chatRowCompose(
        thread = thread,
        message = message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        senderUserId = response.sender.appUserId,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        parentMsgId = parentMsgId,
        parentMsg = parentMsg,
        chatEventType = chatEventType,
        forwardMsgUniqueId = response.forwardChatFeatureData?.originalMsgUniqueId,
        isForwardMessage = response.forwardChatFeatureData?.isForwarded,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData,
        isFollowThread = response.isFollowThread
      )
    }
    return singleChat!!
  }

  fun getGroupInfoChatRow(
    thread: Thread? = null,
    senderUserId: String?,
    chatEventType: ChatEventType,
    groupUpdate: GroupUpdate,
    event: Event,
    userDao: UserDao,
    chatUserId: String?
  ): SingleChat {
    // profilePicChanged, descriptionChanged, nameChanged, profilePicRemoved, participantsRemoved, participantsAdded
    var startMessage = ""
    val eventUser: String? = groupUpdate.eventTriggeredByUser?.appUserId
    if (chatEventType == ChatEventType.INCOMING) {
      if (eventUser != null) {
        startMessage = userDao.getName(eventUser)
      }
    } else {
      startMessage = "You"
    }
    var message: String = startMessage
    when (event.eventType) {
      "profilePicChanged"   -> {
        message += " changed profile pic"
      }
      "profilePicRemoved"   -> {
        message += " removed profile pic"
      }
      "nameChanged"         -> {
        message += " changed channel name"
      }
      "descriptionChanged"  -> {
        message += " changed channel description"
      }
      "participantsRemoved" -> {
        if (event.eventData?.eventTriggeredOnUserList?.get(0)?.appUserId.equals(eventUser)) {
          message += " left"
        } else {
          message += " removed "
          for (user: EventTriggeredOnUsers in event.eventData?.eventTriggeredOnUserList!!) {
            message += userDao.getName(user.appUserId) + ", "
          }
          message = message.trim().substring(0, message.length - 2)
        }
      }
      "participantsAdded"   -> {
        message += " added "
        for (user: EventTriggeredOnUsers in event.eventData?.eventTriggeredOnUserList!!) {
          message += userDao.getName(user.appUserId) + ", "
        }
        message = message.trim().substring(0, message.length - 2)
      }
      "adminMade"           -> {
        if (event.eventData?.eventTriggeredOnUserList?.get(0)?.eRTCUserId.equals(chatUserId)) {
          message = "You are now an admin"
        } else {
          message = userDao.getName(event.eventData?.eventTriggeredOnUserList?.get(0)?.appUserId) +
              " is now an admin"
        }
      }
      "adminDismissed"      -> {
        if (event.eventData?.eventTriggeredOnUserList?.get(0)?.eRTCUserId.equals(chatUserId)) {
          message = "You are no longer an admin"
        } else {
          message = userDao.getName(event.eventData?.eventTriggeredOnUserList?.get(0)?.appUserId) +
              " is no longer an admin"
        }
      }
      "created"             -> {
        message += " created channel \"" + event.eventData?.changeData?.name?.new + "\""
        if (chatEventType == ChatEventType.INCOMING) {
          message += "\n$startMessage added you"
        }
      }
      else                  -> {
        message = "channel updated"
      }
    }
    return chatRowCompose(
      thread = thread,
      message = message,
      msgId = UUID.randomUUID().toString(),
      isRead = true,
      createdAt = System.currentTimeMillis(),
      msgStatus = MessageStatus.SEEN.status,
      msgType = MessageType.TEXT.type,
      msgUniqueId = UUID.randomUUID().toString(),
      senderUserId = senderUserId,
      chatEventType = ChatEventType.CHAT_META_DATA.type,
      clientCreatedAt = System.currentTimeMillis()
    )
  }

  fun getThreadChatRow(
    response: ChatTopicResponse,
    thread: Thread,
    preferenceManager: PreferenceManager? = null,
    eKeyDao: EKeyDao? = null,
    parentMsgId: String? = null,
    msgId: String
  ): ChatThread? {
    val msgType = response.msgType
    var encryptedChat: EncryptedChat? = null
    var privateKey: String? = null
    var publicKey: String? = null
    val isE2E: Boolean = response.encryptedChat != null
    val messageStatus = if (response.sender.appUserId == preferenceManager?.appUserId) {
      MessageStatus.SENT
    } else {
      MessageStatus.DELIVERED
    }
    if (isE2E) {
      encryptedChat = response.encryptedChat
      val myKeyTable = eKeyDao?.getPrivateKeyByKeyId(
        Objects.requireNonNull(encryptedChat)!!.eRTCUserId,
        encryptedChat!!.deviceId, response.tenantId, encryptedChat.keyId
      )
      privateKey = myKeyTable?.privateKey
      publicKey = Objects.requireNonNull(response.senderKeyDetails)!!.publicKey
    }
    var chatThread: ChatThread? = null
    if ((msgType == MessageType.IMAGE.type)
      || (msgType == MessageType.AUDIO.type)
      || (msgType == MessageType.VIDEO.type)
      || (msgType == MessageType.FILE.type)
      || (msgType == MessageType.GIF.type)
    ) {
      if (!TextUtils.isEmpty(response.media?.path)) {
        val chatServer = preferenceManager?.chatServer
        chatThread = chatThreadRowCompose(
          thread = thread,
          message = response.message,
          msgId = msgId,
          isRead = true,
          createdAt = response.senderTimeStampMs,
          msgStatus = messageStatus.status,
          msgType = response.msgType,
          msgUniqueId = response.msgUniqueId,
          parentMsgId = parentMsgId,
          senderUserId = response.sender.appUserId,
          mediaPath = chatServer + response.media?.path,
          mediaThumbnail = chatServer + response.media?.thumbnail,
          mediaName = response.media?.name,
          sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
          clientCreatedAt = response.senderTimeStampMs,
          customData = response.customData
        )
      }
    } else if (msgType == MessageType.LOCATION.type) {
      val location: Location
      location = if (isE2E) {
        val decryptedMessage = ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.location), publicKey,
          privateKey
        )
        Gson().fromJson(
          decryptedMessage,
          Location::class.java
        )
      } else {
        Location(
          Objects.requireNonNull(response.location)!!.address,
          response.location!!.latitude, response.location!!.longitude
        )
      }
      chatThread = chatThreadRowCompose(
        thread = thread,
        message = response.message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        parentMsgId = parentMsgId,
        senderUserId = response.sender.appUserId,
        address = location.address,
        latitude = location.latitude,
        longitude = location.longitude,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData
      )

    } else if (msgType == MessageType.CONTACT.type) {
      val contact = if (isE2E) {
        val decryptedMessage = ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.contact), publicKey,
          privateKey
        )
        Gson()
          .fromJson(
            decryptedMessage,
            Contact::class.java
          )
      } else {
        response.contact
      }
      val phoneRecord: ArrayList<PhoneContactRecord>? = ArrayList()
      val emailRecord: ArrayList<EmailContactRecord>? = ArrayList()
      contact?.numbers?.let {
        for (phone in it) {
          phoneRecord?.add(PhoneContactRecord(phone.type, phone.number))
        }
      }

      contact?.emails?.let {
        for (email in it) {
          emailRecord?.add(EmailContactRecord(email.type, email.email))
        }
      }
      chatThread = chatThreadRowCompose(
        thread = thread,
        message = response.message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        parentMsgId = parentMsgId,
        senderUserId = response.sender.appUserId,
        contactName = contact?.name,
        phoneNumberRecord = phoneRecord,
        eMailRecords = emailRecord,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData
      )

    } else if (msgType == MessageType.GIPHY.type) {
      val gifPath = if (isE2E) {
        ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.gify), publicKey,
          privateKey
        )
      } else {
        response.gify
      }
      chatThread = chatThreadRowCompose(
        thread = thread,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        parentMsgId = parentMsgId,
        senderUserId = response.sender.appUserId,
        gifPath = gifPath,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData
      )
    } else {
      val message = if (isE2E) {
        ECDHUtils.decrypt(
          Objects.requireNonNull(encryptedChat!!.message), publicKey,
          privateKey
        )
      } else {
        response.message
      }
      chatThread = chatThreadRowCompose(
        thread = thread,
        message = message,
        msgId = msgId,
        isRead = true,
        createdAt = response.senderTimeStampMs,
        msgStatus = messageStatus.status,
        msgType = response.msgType,
        msgUniqueId = response.msgUniqueId,
        parentMsgId = parentMsgId,
        senderUserId = response.sender.appUserId,
        sendToChannel = response.replyThreadFeatureData?.replyMsgConfig,
        clientCreatedAt = response.senderTimeStampMs,
        customData = response.customData
      )
    }
    return chatThread
  }

  private fun convertToEntityPhoneContactList(list: List<PhoneContactRecord?>?): List<PhoneContact> {
    val phoneContactList = ArrayList<PhoneContact>()
    if (list != null) {
      for (phoneRecord in list) {
        phoneContactList.add(PhoneContact(phoneRecord?.type, phoneRecord?.number))
      }
    }
    return phoneContactList
  }

  private fun convertToEntityEmailContactList(list: List<EmailContactRecord?>?): List<EmailContact> {
    val emailContactList = ArrayList<EmailContact>()
    if (list != null) {
      for (mailRecord in list) {
        emailContactList.add(EmailContact(mailRecord?.type, mailRecord?.email))
      }
    }
    return emailContactList
  }

  fun transform(
    unicode: String,
    threadId: String?,
    chatMsgId: String? = null,
    chatThreadMsgId: String? = null,
    userChatId: String?,
    totalCount: Int? = 0
  ): ChatReactionEntity {
    return ChatReactionEntity(
      unicode = unicode,
      threadId = threadId,
      chatMsgId = chatMsgId,
      chatThreadMsgId = chatThreadMsgId,
      userChatId = userChatId,
      totalCount = totalCount
    )
  }

  fun transform(
    chatThread: ChatThread,
    msgResponse: MessageResponse,
    senderAppUserId: String,
    clientCreatedAt: Long?,
    customData: String?
  ): SingleChat {
    val singleChat = SingleChat(
      id = msgResponse.data?.requestId!!,
      createdAt = clientCreatedAt,
      message = chatThread.message,
      read = 0,
      type = msgResponse.thread?.threadType,
      senderAppUserId = senderAppUserId,
      threadId = msgResponse.threadId,
      status = MessageStatus.SENT.status,
      msgType = chatThread.msgType,
      msgUniqueId = msgResponse.msgUniqueId,
      mediaPath = chatThread.mediaPath,
      mediaThumbnail = chatThread.mediaThumbnail,
      contactName = chatThread.contactName,
      phoneContactList = chatThread.phoneContactList,
      emailContactList = chatThread.emailContactList,
      gifPath = chatThread.gifPath,
      location = chatThread.location,
      mediaName = chatThread.mediaName,
      localMediaPath = chatThread.localMediaPath,
      downloadStatus = chatThread.downloadStatus,
      chatEventType = ChatEventType.OUTGOING.type,
      forwardMsgUniqueId = chatThread.msgUniqueId,
      isForwardMessage = 1,
      isStarredChat = "0",
      clientCreatedAt = clientCreatedAt,
      customData = customData,
      isFollowed = 1
    )
    return singleChat
  }
}