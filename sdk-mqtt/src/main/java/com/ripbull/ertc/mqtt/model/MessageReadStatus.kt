package com.ripbull.mqtt.model

import com.ripbull.ertc.mqtt.model.ForwardChat
import com.ripbull.ertc.mqtt.model.ReplyThread

/**
 * Created by DK on 03/04/19.
 */


data class MessageReadStatus @JvmOverloads constructor(
  var tenantId: String,
  var msgUniqueId: String,
  var sendereRTCUserId: String,
  var threadId: String?,
  var eRTCUserId: String,
  var msgStatusEvent: String,
  var deviceId: String? = null,
  var timeStamp: Long,
  var replyThreadFeatureData : ReplyThread? = null,
  var forwardChatFeatureData: ForwardChat? = null
) {
  override fun toString(): String {
    return "MessageReadStatus(" +
        "tenantId='$tenantId', " +
        "msgUniqueId='$msgUniqueId'," +
        "sendereRTCUserId='$sendereRTCUserId'," +
        "threadId='$threadId', " +
        "eRTCUserId='$eRTCUserId', " +
        "msgStatusEvent='$msgStatusEvent', " +
        "deviceId='$deviceId', " +
        "timeStamp=$timeStamp, " +
        "replyThreadFeatureData=${replyThreadFeatureData}, " +
        "forwardChatFeatureData=$forwardChatFeatureData)"
  }
}
