package com.ripbull.ertc.mqtt.model

/**
 * Created by DK on 03/04/19.
 */

data class ChatReaction(
  var eRTCUserId: String?,
  var msgUniqueId: String,
  var emojiCode: String,
  var action: String?,
  var totalCount: Int?,
  var threadId: String?,
  var tenantId: String?,
  var replyThreadFeatureData : ReplyThread?
)
