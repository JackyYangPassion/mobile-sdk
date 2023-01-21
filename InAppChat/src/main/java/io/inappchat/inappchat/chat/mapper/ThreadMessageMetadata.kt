package io.inappchat.inappchat.chat.mapper


import io.inappchat.inappchat.user.mapper.UserRecord

/**
 * Created by DK on 15/04/20.
 */

data class ThreadMessageMetadata(
  val parentMsgId: String? = null,
  val parentMsg: String? = null,
  val chatThreadCount: Int? = null,
  val sendToChannel: Int? = null,
  val participants: List<UserRecord>? = null,
  val msgId: String? = null
)