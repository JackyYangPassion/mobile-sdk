package com.ripbull.ertc.mqtt.model

/**
 * Created by DK on 03/04/19.
 */

data class ChatTopicResponse(
  var sender: Sender,
  var thread: Thread,
  var message: String?,
  var media: Media?,
  var msgType: String,
  var tenantId: String,
  var createdAt: Long,
  var msgUniqueId: String,
  var location: Location?,
  var gify: String?,
  var contact: Contact?,
  var replyThreadFeatureData: ReplyThread? = null,
  var forwardChatFeatureData: ForwardChat? = null,

  // e2e
  var senderKeyDetails: SenderKeyDetails? = null,
  var encryptedChat: EncryptedChat? = null,
  var isSilent : Boolean?,
  var senderTimeStampMs: Long,
  var customData: String? = null,

  // follow/unfollow thread
  var isFollowThread: Int? = 0,
  var follow: Boolean? = false
)

data class ReplyThread(
  var baseMsgUniqueId: String,
  var replyMsgConfig: Int? = null
)

data class Sender(
  var appUserId: String,
  var eRTCUserId: String,
  var name: String? = null
)

data class Thread(
  var threadType: String,
  var tenantId: String,
  var createdAt: Long,
  var threadId: String,
  var unreadCount:Int,
  var participants: List<User>
)

data class Location(
  var address: String? = "",
  var latitude: Double? = 0.0,
  var longitude: Double? = 0.0
)

data class Contact(
  var name: String? = "",
  var emails: ArrayList<EmailRow>?,
  var numbers: ArrayList<PhoneRow>?
)

data class PhoneRow(
  var type: String? = "",
  var number: String? = ""
)

data class EmailRow(
  var type: String? = "",
  var email: String? = ""
)

data class SenderKeyDetails(
  var deviceId: String? = "",
  var keyId: String? = "",
  var publicKey: String? = ""
)

data class Media(
  var path: String? = "", var thumbnail: String? = "", var name: String? = ""
)

data class User(
  var user: String = "",
  var appUserId: String? = ""
)

data class EncryptedChat(
  var deviceId: String? = "",
  var keyId: String? = "",
  var publicKey: String? = "",
  var eRTCUserId: String? = "",
  var message: String? = "",
  var path: String? = "",
  var thumbnail: String? = "",
  var location: String? = "",
  var contact: String? = "",
  var gify: String?
)

data class ForwardChat(
  var originalMsgUniqueId: String? = null,
  var isForwarded: Boolean? = false
)