package io.inappchat.inappchat.mqtt.model

/**
 * Created by DK on 03/04/19.
 */

data class TypingIndicator(
  var tenantId: String,
  var threadType: String,
  var participants: ArrayList<String>,
  var threadId: String,
  var eRTCUserId: String,
  var typingStatusEvent: String,
  var name: String
) {
  override fun toString(): String {
    return "TypingIndicator(tenantId='$tenantId'," +
        " participants=$participants, " +
        "threadId='$threadId', " +
        "eRTCUserId='$eRTCUserId', " +
        "typingStatusEvent='$typingStatusEvent'," +
        " name='$name')"
  }
}
