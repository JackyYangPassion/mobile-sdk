package com.ripbull.coresdk.chat.model


import com.ripbull.coresdk.chat.mapper.EmailContactRecord
import com.ripbull.coresdk.chat.mapper.PhoneContactRecord
import com.ripbull.coresdk.core.type.ChatReactionType
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MentionType
import com.ripbull.coresdk.core.type.MessageType

/**
 * Created by DK on 2020-02-23.
 */

data class Message constructor(
  val message: String? = null,
  val parentMsgId: String? = null,
  val msgId: String? = null,
  val sendToChannel: Int? = null,
  val media: Media? = null,
  val file: File? = null,
  val giphy: Giphy? = null,
  val contact: Contact? = null,
  val location: Location? = null,
  val chatType: ChatType,
  val mentions: MentionType? = null,
  val mentioned_users: List<String>? = null,
  val forwardMsgId: String? = null,
  val isForwardMessage: Boolean = false,
  var clientCreatedAt: Long? = null
)


data class MessageMetaData constructor(
  val reaction: Reaction? = null
)


data class Reaction constructor(
  val msgId: String,
  val parentMsgId: String? = null,
  val sendToChannel: Int? = null,
  val emojiCode: String,
  val chatReactionType: ChatReactionType
)


data class Media constructor(
  val mediaPath: String,
  val type: MessageType
)


data class File constructor(
  val filePath: String,
  val type: MessageType
)


data class Giphy constructor(
  val gifPath: String,
  val type: MessageType
)


data class Contact constructor(
  val contactName: String?,
  val phoneNumberRecord: List<PhoneContactRecord>?,
  val eMailRecords: List<EmailContactRecord>?,
  val type: MessageType
)


data class Location constructor(
  val address: String?,
  val latitude: Double?,
  val longitude: Double?,
  val type: MessageType
)