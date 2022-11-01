package com.ripbull.coresdk.chat.mapper


import com.ripbull.coresdk.core.type.ChatReactionType
import com.ripbull.coresdk.user.mapper.UserRecord

/**
 * Created by DK on 2019-08-20.
 */

data class MessageMetaDataRecord(
  val chatReactionRecord: ChatReactionRecord? = null,
  val threadMetadata: ThreadMessageMetadata? = null

)


data class ChatReactionRecord(
  val threadId: String? = null,
  val msgUniqueId: String? = null,
  val emojiCode: String? = null,
  val chatReactionType: ChatReactionType? = null,
  val userRecord: List<UserRecord>? = null,
  val count: Int? = null,
  val parentMsgId: String? = null
)

