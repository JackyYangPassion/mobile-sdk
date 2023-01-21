package io.inappchat.inappchat.mqtt.model

data class ChatUpdateResponse @JvmOverloads constructor(
  var tenantId: String,
  var threadId: String?,
  var eRTCUserId: String,
  var updateType: String?,
  var chats : ArrayList<Chats>? = null,
  var msgCorrelationId : String?,
  var deleteType: String?,
  var sender: Sender?,
  var thread: Thread?,
  var senderKeyDetails: SenderKeyDetails?
) {
  override fun toString(): String {
    return "MessageReadStatus(" +
        "tenantId='$tenantId', " +
        "threadId='$threadId', " +
        "updateType='$updateType', " +
        "eRTCUserId='$eRTCUserId', " +
        "chats=${chats}"
  }
}

data class Chats(
  val msgUniqueId: String ?,
  val message: String ? = null,
  val keyId: String? = null,
  val deviceId: String? = null,
  val publicKey: String? = null,
  val eRTCUserId: String? = null,
  val isStarred: Boolean? = null,
  val replyThreadFeatureData: ReplyThreadChatUpdate? = null
)

data class ReplyThreadChatUpdate(
  val baseMsgUniqueId: String?,
  var replyMsgConfig: Int? = null
)