package com.ripbull.coresdk.chat.mapper


import com.ripbull.coresdk.core.type.ChatEventType
import com.ripbull.coresdk.core.type.DeleteType
import com.ripbull.coresdk.core.type.MessageUpdateType
import com.ripbull.coresdk.fcm.NotificationRecord
import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserRecord

/** Created by DK on 09/03/19.  */

data class MessageRecord @JvmOverloads constructor(
  val id: String,
  val timestamp: Long? = null,
  val message: String? = null,
  val read: Int? = null,
  val type: String? = null, // defines message type
  val threadId: String? = null,
  val nextMessageId: String? = null,
  val lastMessageId: String? = null,
  val status: String? = null,
  val msgType: String? = null,
  val senderId: String? = null,
  val mediaPath: String? = null,
  val mediaThumbnail: String? = null,
  val locationRecord: LocationRecord? = null,
  val contactName: String? = null,
  val phoneContactRecord: List<PhoneContactRecord>? = null,
  val emailContactRecord: List<EmailContactRecord>? = null,
  val isFavoriteMessage: Boolean? = false,
  val gifPath: String? = null,
  val senderRecord: UserRecord? = null,
  val receiverRecord: UserRecord? = null,
  val groupRecord: GroupRecord? = null,
  val mediaName: String? = null,
  val notificationRecord: NotificationRecord? = null,
  val localMediaPath: String? = null,
  val downloadStatus: String? = null,
  //Chat Thread related changes
  var chatThreadMetadata: ThreadMessageMetadata? = null,
  val chatEventType : ChatEventType? = null,
  var reactions : List<ChatReactionRecord>? = null,
  var isSilent : Boolean? = false,
  var messageDeleteType: String = DeleteType.DELETE_FOR_USER.type,
  var isForwardMessage: Boolean = false,
  var updateType: MessageUpdateType? = null,
  val clientCreatedAt: Long? = null,
  val customData: String? = null,
  var isFollowThread: Boolean = false,
  val chatReportId: String? = null,
  val reportType: String? = null,
  val reason: String? = null,
  val mentions: String? = null,
  val mentionedUsers: String? = null
)
