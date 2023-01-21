package io.inappchat.inappchat.chat.mapper

import io.inappchat.inappchat.group.mapper.GroupRecord
import io.inappchat.inappchat.user.mapper.UserRecord

/** Created by Sagar on 01/01/22.  */
data class FollowThreadRecord @JvmOverloads constructor(
  var threadId: String,
  var name: String,
  var type: String,
  var replyCount: Int,
  var participants: List<String>,
  var parentMsgId: String,
  var isFollowThread: Boolean = true,
  var recipient: UserRecord? = null,
  var group: GroupRecord? = null,
  var replies: List<MessageRecord>
)
