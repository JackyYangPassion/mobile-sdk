package com.ripbull.coresdk.chat.mapper

import com.ripbull.coresdk.group.mapper.GroupRecord
import com.ripbull.coresdk.user.mapper.UserRecord

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
