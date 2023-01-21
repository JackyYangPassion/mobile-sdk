package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem
import io.inappchat.inappchat.remote.model.request.MetaData

data class MessageResponse(
  @Expose @SerializedName("msgUniqueId") val msgUniqueId: String,
  @Expose @SerializedName("threadId") val threadId: String?,
  @Expose @SerializedName("tenantId") val tenantId: String?,
  @Expose @SerializedName("msgType") val msgType: String,
  @Expose @SerializedName("message") val message: String?,
  @Expose @SerializedName("metadata") val data: MetaData?,
  @Expose @SerializedName("createdAt") val createdAt: Long?,
  @Expose @SerializedName("replyThreadFeatureData") val replyThreadFeatureData: ReplyThreadResponse?,
  @Expose @SerializedName("thread") val thread: CreateThreadResponse?,
  @Expose @SerializedName("media") val media: Media?,
  @Expose @SerializedName("contact") val contact: Contact?,
  @Expose @SerializedName("location") val location: Location?,
  @Expose @SerializedName("forwardChatFeatureData") val forwardChatFeature: ForwardChatFeature?,
  @Expose @SerializedName("reactions") val reactions: List<Reactions>?,
  @Expose @SerializedName("gify") val gify: String?,
  @Expose @SerializedName("sendereRTCUserId") val sendereRTCUserId: String?,
  @Expose @SerializedName("senderTimeStampMs") val senderTimeStampMs: Long?,
  @Expose @SerializedName("customData") val customData: String?,
  @Expose @SerializedName("replies") val replies: List<MessageResponse>?,
  @Expose @SerializedName("isStarred") val isStarred: Boolean?,
  @Expose @SerializedName("isEdited") val isEdited: Boolean?,
  @Expose @SerializedName("follow") val follow: Boolean?
) : ValidItem {

  override fun isValid(): Boolean {
    return msgUniqueId.isNotBlank()
  }
}


data class Media(
  @SerializedName("path") @Expose var path: String? = null,
  @SerializedName("name") @Expose var name: String? = null,
  @SerializedName("thumbnail") @Expose var thumbnail: String? = null
) : ValidItem {
  override fun isValid(): Boolean  = true
}


data class ReplyThreadResponse(
  @SerializedName("baseMsgUniqueId") @Expose var parentMsgId: String? = null,
  @SerializedName("replyMsgConfig") @Expose var replyMsgConfig: Int? = null,
  @SerializedName("numOfReplies") @Expose var numOfReplies: Int? = 1,
  @SerializedName("responderList") @Expose var responderList: List<Responder>? = null
) : ValidItem {
  override fun isValid(): Boolean  = true
}

data class Location(
  @SerializedName("address") @Expose var address: String?,
  @SerializedName("latitude") @Expose var latitude: Double? = null,
  @SerializedName("longitude") @Expose var longitude: Double? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class Contact(
  @SerializedName("name") @Expose var name: String,
  @SerializedName("numbers") @Expose var numbersList: List<PhoneNumber>? = null,
  @SerializedName("emails") @Expose var emailsList: List<Email>? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class PhoneNumber(
  @SerializedName("type") @Expose var type: String? = null,
  @SerializedName("number") @Expose var number: String? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class Email(
  @SerializedName("type") @Expose var type: String? = null,
  @SerializedName("email") @Expose var email: String? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class ForwardChatFeature(
  @SerializedName("originalMsgUniqueId") @Expose var originalMsgUniqueId: String? = null,
  @SerializedName("isForwarded") @Expose var isForwarded: Boolean? = false
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class Reactions(
  @SerializedName("emojiCode") @Expose var emojiCode: String? = null,
  @SerializedName("count") @Expose var count: Int? = 0,
  @SerializedName("users") @Expose var users: List<String>? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class Responder(
  @SerializedName("eRTCUserId") @Expose var eRTCUserId: String? = null,
  @SerializedName("appUserId") @Expose var appUserId: String? = null
): ValidItem {
  override fun isValid(): Boolean  = true
}