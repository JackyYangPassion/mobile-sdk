package com.ripbull.coresdk.chat.mapper

import com.google.gson.Gson
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MessageType
import com.ripbull.coresdk.core.type.MessageType.CONTACT
import com.ripbull.coresdk.core.type.MessageType.GIPHY
import com.ripbull.coresdk.core.type.MessageType.LOCATION
import com.ripbull.coresdk.core.type.MessageType.TEXT
import com.ripbull.coresdk.e2e.ECDHUtils
import com.ripbull.ertc.cache.database.dao.EKeyDao
import com.ripbull.ertc.cache.database.dao.GroupDao
import com.ripbull.ertc.cache.database.entity.EKeyTable
import com.ripbull.ertc.cache.database.entity.Thread
import com.ripbull.ertc.cache.database.entity.User
import com.ripbull.ertc.remote.model.request.Email
import com.ripbull.ertc.remote.model.request.EncryptedChat
import com.ripbull.ertc.remote.model.request.ForwardChat
import com.ripbull.ertc.remote.model.request.LocationReq
import com.ripbull.ertc.remote.model.request.MediaReq
import com.ripbull.ertc.remote.model.request.Mentions
import com.ripbull.ertc.remote.model.request.MessageRequest
import com.ripbull.ertc.remote.model.request.MetaData
import com.ripbull.ertc.remote.model.request.PhoneContactReq
import com.ripbull.ertc.remote.model.request.PhoneNumber
import com.ripbull.ertc.remote.model.request.ReplyThread
import com.ripbull.ertc.remote.model.request.SenderKeyDetails

/**
 * Created by DK on 13/06/20.
 */
object ChatRequestMapper {
  // API - Request
  fun request(
    thread: Thread? = null,
    message: Message,
    msgId: String,
    replyThread: ReplyThread? = null,
    mentionedChatUserIds: MutableList<String>? = null,
    senderId: String? = null,
    threadId: String? = null,
    forwardChat: ForwardChat? = null,
    recipientAppUserId: String? = null,
    mediaReq: MediaReq? = null,
    customData: String? = null
  ): MessageRequest {
    if (message.giphy != null) {
      return MessageRequest(
        threadId = threadId ?: thread?.id,
        senderId = senderId ?: thread?.senderChatId,
        message = message.message,
        msgType = GIPHY.type,
        metaData = MetaData(msgId),
        replyThread = replyThread,
        gifPath = message.giphy.gifPath,
        mentionsList = mentions(message, mentionedChatUserIds),
        forwardChat = forwardChat,
        recipientAppUserId = recipientAppUserId,
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData
      )
    }
    if (message.contact != null) {
      return MessageRequest(
        threadId = threadId ?: thread?.id,
        senderId = senderId ?: thread?.senderChatId,
        message = message.message,
        msgType = CONTACT.type,
        metaData = MetaData(msgId),
        replyThread = replyThread,
        contact = message.contact.contactName?.let {
          PhoneContactReq(
            it, convertToReqPhoneNumList(message.contact.phoneNumberRecord),
            convertToReqEmailList(message.contact.eMailRecords)
          )
        },
        mentionsList = mentions(message, mentionedChatUserIds),
        forwardChat = forwardChat,
        recipientAppUserId = recipientAppUserId,
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData
      )
    }
    if (message.location != null) {
      return MessageRequest(
        threadId = threadId ?: thread?.id,
        senderId = senderId ?: thread?.senderChatId,
        message = message.message,
        msgType = LOCATION.type,
        metaData = MetaData(msgId),
        replyThread = replyThread,
        location = message.location.address?.let {
          LocationReq(
            it,
            message.location.latitude, message.location.longitude
          )
        },
        mentionsList = mentions(message, mentionedChatUserIds),
        forwardChat = forwardChat,
        recipientAppUserId = recipientAppUserId,
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData
      )
    }
    if (message.file != null) {
      return MessageRequest(
        threadId = threadId ?: thread?.id,
        senderId = senderId ?: thread?.senderChatId,
        message = message.message,
        msgType = MessageType.FILE.type,
        metaData = MetaData(msgId),
        replyThread = replyThread,
        mediaReq = mediaReq,
        mentionsList = mentions(message, mentionedChatUserIds),
        forwardChat = forwardChat,
        recipientAppUserId = recipientAppUserId,
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData
      )
    }
    if (message.media != null) {
      return MessageRequest(
        threadId = threadId ?: thread?.id,
        senderId = senderId ?: thread?.senderChatId,
        message = message.message,
        msgType = message.media.type.type,
        metaData = MetaData(msgId),
        replyThread = replyThread,
        mediaReq = mediaReq,
        mentionsList = mentions(message, mentionedChatUserIds),
        forwardChat = forwardChat,
        recipientAppUserId = recipientAppUserId,
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData
      )
    }

    return MessageRequest(
      threadId = threadId ?: thread?.id,
      senderId = senderId ?: thread?.senderChatId,
      message = message.message,
      msgType = TEXT.type,
      metaData = MetaData(msgId),
      replyThread = replyThread,
      mentionsList = mentions(message, mentionedChatUserIds),
      forwardChat = forwardChat,
      recipientAppUserId = recipientAppUserId,
      senderTimeStampMs = message.clientCreatedAt,
      customData = customData
    )
  }

  fun mentions(
    message: Message,
    mentionedChatUserIds: MutableList<String>?
  ): List<Mentions>? {
    return if (message.mentions == null && mentionedChatUserIds == null) {
      null
    } else {
      val arrayListOfMention = arrayListOf<Mentions>()
      if (message.mentions != null) {
        arrayListOfMention.add(Mentions("generic", message.mentions.type))
      }
      if (!mentionedChatUserIds.isNullOrEmpty()) {
        mentionedChatUserIds.forEach {
          arrayListOfMention.add(Mentions("user", it))
        }
      }
      arrayListOfMention
    }
  }

  fun requestE2E(
    thread: Thread,
    message: Message,
    msgId: String,
    replyThread: ReplyThread? = null,
    eKeyTable: EKeyTable,
    ekeyDao: EKeyDao,
    groupDao: GroupDao,
    mentionedChatUserIds: MutableList<String>?,
    parallelDeviceList: ArrayList<EKeyTable>?,
    customData: String?,
    forwardChat: ForwardChat? = null
  ): MessageRequest {

    // giphy
    if (message.giphy != null) {
      var encryptedChatList: List<EncryptedChat>?
      if (message.chatType == ChatType.SINGLE || message.chatType == ChatType.SINGLE_CHAT_THREAD) {
        var tableList =
          ekeyDao.getKeyByErtcId(thread.recipientChatId, thread.tenantId) as ArrayList<EKeyTable>
        if (!parallelDeviceList.isNullOrEmpty()) {
          for (e2eKey in parallelDeviceList) {
            tableList.add(e2eKey)
          }
        }
        encryptedChatList = singleTypeEncryptedChatList(
          tableList,
          message.giphy.gifPath,
          GIPHY,
          eKeyTable.privateKey
        )
      } else {
        val group = groupDao.getGroupByThreadId(thread.tenantId, thread.id)
        encryptedChatList = ArrayList()
        for (groupUser: User in group.groupUsers) {
          if (!groupUser.ertcId.equals(thread.senderChatId)) {
            val tableList =
              ekeyDao.getKeyByErtcId(groupUser.ertcId, thread.tenantId) as ArrayList<EKeyTable>
            if (!parallelDeviceList.isNullOrEmpty()) {
              for (e2eKey in parallelDeviceList) {
                tableList.add(e2eKey)
              }
            }
            encryptedChatList.addAll(
              singleTypeEncryptedChatList(
                tableList,
                message.giphy.gifPath,
                GIPHY,
                eKeyTable.privateKey
              )
            )
          }
        }
      }

      return MessageRequest(
        threadId = thread.id,
        senderId = thread.senderChatId,
        encryptedChatList = encryptedChatList,
        msgType = GIPHY.type,
        metaData = MetaData(msgId),
        senderKeyDetails = senderKeyDetails(eKeyTable),
        replyThread = replyThread,
        mentionsList = mentions(message, mentionedChatUserIds),
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData,
        forwardChat = forwardChat
      )
    }
    // contact
    if (message.contact != null) {
      val contact = message.contact.contactName?.let {
        PhoneContactReq(
          it, convertToReqPhoneNumList(message.contact.phoneNumberRecord),
          convertToReqEmailList(message.contact.eMailRecords)
        )
      }
      val contactStr = Gson().toJson(contact, PhoneContactReq::class.java)

      val encryptedChatList: List<EncryptedChat>?
      if (message.chatType == ChatType.SINGLE || message.chatType == ChatType.SINGLE_CHAT_THREAD) {
        val tableList =
          ekeyDao.getKeyByErtcId(thread.recipientChatId, thread.tenantId) as ArrayList<EKeyTable>
        if (!parallelDeviceList.isNullOrEmpty()) {
          for (e2eKey in parallelDeviceList) {
            tableList.add(e2eKey)
          }
        }
        encryptedChatList = singleTypeEncryptedChatList(
          tableList,
          contactStr,
          CONTACT,
          eKeyTable.privateKey
        )
      } else {
        val group = groupDao.getGroupByThreadId(thread.tenantId, thread.id)
        encryptedChatList = ArrayList()
        for (groupUser: User in group.groupUsers) {
          if (!groupUser.ertcId.equals(thread.senderChatId)) {
            val tableList =
              ekeyDao.getKeyByErtcId(groupUser.ertcId, thread.tenantId) as ArrayList<EKeyTable>
            if (!parallelDeviceList.isNullOrEmpty()) {
              for (e2eKey in parallelDeviceList) {
                tableList.add(e2eKey)
              }
            }
            encryptedChatList.addAll(
              singleTypeEncryptedChatList(
                tableList,
                contactStr,
                CONTACT,
                eKeyTable.privateKey
              )
            )
          }
        }
      }

      return MessageRequest(
        threadId = thread.id,
        senderId = thread.senderChatId,
        encryptedChatList = encryptedChatList,
        msgType = CONTACT.type,
        metaData = MetaData(msgId),
        senderKeyDetails = senderKeyDetails(eKeyTable),
        replyThread = replyThread,
        mentionsList = mentions(message, mentionedChatUserIds),
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData,
        forwardChat = forwardChat
      )
    }

    // location
    if (message.location != null) {
      val location = message.location.address?.let {
        LocationReq(
          it,
          message.location.latitude, message.location.longitude
        )
      }
      val locStr = Gson().toJson(location, LocationReq::class.java)

      val encryptedChatList: List<EncryptedChat>?
      if (message.chatType == ChatType.SINGLE || message.chatType == ChatType.SINGLE_CHAT_THREAD) {
        val tableList =
          ekeyDao.getKeyByErtcId(thread.recipientChatId, thread.tenantId) as ArrayList<EKeyTable>
        if (!parallelDeviceList.isNullOrEmpty()) {
          for (e2eKey in parallelDeviceList) {
            tableList.add(e2eKey)
          }
        }
        encryptedChatList = singleTypeEncryptedChatList(
          tableList,
          locStr,
          LOCATION,
          eKeyTable.privateKey
        )
      } else {
        val group = groupDao.getGroupByThreadId(thread.tenantId, thread.id)
        encryptedChatList = ArrayList()
        for (groupUser: User in group.groupUsers) {
          if (!groupUser.ertcId.equals(thread.senderChatId)) {
            val tableList =
              ekeyDao.getKeyByErtcId(groupUser.ertcId, thread.tenantId) as ArrayList<EKeyTable>
            if (!parallelDeviceList.isNullOrEmpty()) {
              for (e2eKey in parallelDeviceList) {
                tableList.add(e2eKey)
              }
            }
            encryptedChatList.addAll(
              singleTypeEncryptedChatList(
                tableList,
                locStr,
                LOCATION,
                eKeyTable.privateKey
              )
            )
          }
        }
      }

      return MessageRequest(
        threadId = thread.id,
        senderId = thread.senderChatId,
        encryptedChatList = encryptedChatList,
        msgType = LOCATION.type,
        metaData = MetaData(msgId),
        senderKeyDetails = senderKeyDetails(eKeyTable),
        replyThread = replyThread,
        mentionsList = mentions(message, mentionedChatUserIds),
        senderTimeStampMs = message.clientCreatedAt,
        customData = customData,
        forwardChat = forwardChat
      )
    }

    // text
    val encryptedChatList: List<EncryptedChat>?
    if (message.chatType == ChatType.SINGLE || message.chatType == ChatType.SINGLE_CHAT_THREAD) {
      val tableList =
        ekeyDao.getKeyByErtcId(thread.recipientChatId, thread.tenantId) as ArrayList<EKeyTable>
      if (!parallelDeviceList.isNullOrEmpty()) {
        for (e2eKey in parallelDeviceList) {
          tableList.add(e2eKey)
        }
      }
      encryptedChatList = singleTypeEncryptedChatList(
        tableList,
        message.message,
        TEXT,
        eKeyTable.privateKey
      )
    } else {
      val group = groupDao.getGroupByThreadId(thread.tenantId, thread.id)
      encryptedChatList = ArrayList()
      for (groupUser: User in group.groupUsers) {
        if (!groupUser.ertcId.equals(thread.senderChatId)) {
          val tableList = ekeyDao.getKeyByErtcId(groupUser.ertcId, thread.tenantId) as ArrayList<EKeyTable>
          if (!parallelDeviceList.isNullOrEmpty()) {
            for (e2eKey in parallelDeviceList) {
              tableList.add(e2eKey)
            }
          }
          encryptedChatList.addAll(
            singleTypeEncryptedChatList(
              tableList,
              message.message,
              TEXT,
              eKeyTable.privateKey
            )
          )
        }
      }
    }

    return MessageRequest(
      threadId = thread.id,
      senderId = thread.senderChatId,
      encryptedChatList = encryptedChatList,
      msgType = TEXT.type,
      metaData = MetaData(msgId),
      senderKeyDetails = senderKeyDetails(eKeyTable),
      replyThread = replyThread,
      mentionsList = mentions(message, mentionedChatUserIds),
      senderTimeStampMs = message.clientCreatedAt,
      customData = customData,
      forwardChat = forwardChat
    )
  }

  fun requestEditE2E(
    thread: Thread,
    message: Message,
    msgUniqueId: String,
    eKeyTable: EKeyTable,
    ekeyDao: EKeyDao,
    groupDao: GroupDao,
    mentionedChatUserIds: MutableList<String>?,
    parallelDeviceList: ArrayList<EKeyTable>?
  ): MessageRequest {

    // text
    val encryptedChatList: List<EncryptedChat>?
    if (message.chatType == ChatType.SINGLE || message.chatType == ChatType.SINGLE_CHAT_THREAD) {
      val tableList =
        ekeyDao.getKeyByErtcId(thread.recipientChatId, thread.tenantId) as ArrayList<EKeyTable>
      if (!parallelDeviceList.isNullOrEmpty()) {
        for (e2eKey in parallelDeviceList) {
          tableList.add(e2eKey)
        }
      }
      encryptedChatList = singleTypeEncryptedChatList(
        tableList,
        message.message,
        TEXT,
        eKeyTable.privateKey
      )
    } else {
      val group = groupDao.getGroupByThreadId(thread.tenantId, thread.id)
      encryptedChatList = ArrayList()
      for (groupUser: User in group.groupUsers) {
        if (!groupUser.ertcId.equals(thread.senderChatId)) {
          val tableList = ekeyDao.getKeyByErtcId(groupUser.ertcId, thread.tenantId) as ArrayList<EKeyTable>
          if (!parallelDeviceList.isNullOrEmpty()) {
            for (e2eKey in parallelDeviceList) {
              tableList.add(e2eKey)
            }
          }
          encryptedChatList.addAll(
            singleTypeEncryptedChatList(
              tableList,
              message.message,
              TEXT,
              eKeyTable.privateKey
            )
          )
        }
      }
    }

    return MessageRequest(
      msgUniqueId = msgUniqueId,
      threadId = thread.id,
      senderId = thread.senderChatId,
      encryptedChatList = encryptedChatList,
      msgType = TEXT.type,
      senderKeyDetails = senderKeyDetails(eKeyTable),
      mentionsList = mentions(message, mentionedChatUserIds)
    )
  }

  private fun senderKeyDetails(eKeyTable: EKeyTable) =
    SenderKeyDetails(eKeyTable.keyId, eKeyTable.deviceId, eKeyTable.publicKey)

  private fun singleTypeEncryptedChatList(
    tableList: ArrayList<EKeyTable>,
    msg: String?,
    msgType: MessageType,
    privateKey: String?
  ): ArrayList<EncryptedChat> {
    val list = ArrayList<EncryptedChat>()
    for (table in tableList) {
      val chat = EncryptedChat(
        keyId = table.keyId, deviceId = table.deviceId,
        publicKey = table.publicKey, eRTCUserId = table.ertcUserId
      )
      // location related
      if (msgType == LOCATION)
        chat.location = ECDHUtils.encrypt(msg, chat.publicKey, privateKey)

      // contact related
      if (msgType == CONTACT)
        chat.contact = ECDHUtils.encrypt(msg, chat.publicKey, privateKey)

      // text related
      if (msgType == TEXT)
        chat.message = ECDHUtils.encrypt(msg, chat.publicKey, privateKey)

      // gif related
      if (msgType == GIPHY)
        chat.gifPath = ECDHUtils.encrypt(msg, chat.publicKey, privateKey)
      list.add(chat)
    }
    return list
  }

  @Deprecated("Old stuff", ReplaceWith("request"))
  fun textRequest(
    threadId: String, senderId: String, message: String, msgId: String, clientCreatedAt: Long, customData: String?
  ): MessageRequest {
    return MessageRequest(
      threadId = threadId,
      senderId = senderId,
      message = message,
      msgType = TEXT.type,
      metaData = MetaData(msgId),
      senderTimeStampMs = clientCreatedAt,
      customData = customData
    )
  }

  private fun convertToReqPhoneNumList(list: List<PhoneContactRecord?>?): List<PhoneNumber> {
    val phoneNumberList = ArrayList<PhoneNumber>()
    if (list != null) {
      for (phoneRecord in list) {
        phoneNumberList.add(PhoneNumber(phoneRecord?.type, phoneRecord?.number))
      }
    }
    return phoneNumberList
  }

  private fun convertToReqEmailList(list: List<EmailContactRecord?>?): List<Email> {
    val emailList = ArrayList<Email>()
    if (list != null) {
      for (mailRecord in list) {
        emailList.add(Email(mailRecord?.type, mailRecord?.email))
      }
    }
    return emailList
  }

}