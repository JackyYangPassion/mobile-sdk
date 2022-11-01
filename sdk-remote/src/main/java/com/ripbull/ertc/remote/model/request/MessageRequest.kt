package com.ripbull.ertc.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MessageRequest(
  //Message request
  @SerializedName("threadId") @Expose var threadId: String? = null,
  @SerializedName("sendereRTCUserId") @Expose var senderId: String? = null,
  @SerializedName("message") @Expose var message: String? = null,
  @SerializedName("msgType") @Expose var msgType: String? = null,
  @SerializedName("metadata") @Expose var metaData: MetaData? = null,
  @SerializedName("location") @Expose var location: LocationReq? = null,
  @SerializedName("contact") @Expose var contact: PhoneContactReq? = null,
  @SerializedName("gify") @Expose var gifPath: String? = null,
  @SerializedName("replyThreadFeatureData") @Expose var replyThread: ReplyThread? = null,
  @SerializedName("mentions") @Expose var mentionsList: List<Mentions>? = null,
  @SerializedName("senderTimeStampMs") @Expose var senderTimeStampMs: Long? = null,
  //Forward Chat
  @SerializedName("recipientAppUserId") @Expose var recipientAppUserId: String? = null,
  @SerializedName("media") @Expose var mediaReq: MediaReq? = null,
  @SerializedName("forwardChatFeatureData") @Expose var forwardChat: ForwardChat? = null,

  // E2E Impl
  @SerializedName("encryptedChatList") @Expose var encryptedChatList: List<EncryptedChat>? = null,
  @SerializedName("senderKeyDetails") @Expose var senderKeyDetails: SenderKeyDetails? = null,
  @SerializedName("msgCorrelationId") @Expose var msgCorrelationId: String? = null,

  // Delete Request
  @SerializedName("chats") @Expose val chats: ArrayList<MessageList>? = null,
  @SerializedName("deleteType") @Expose val deleteType: String? = null,

  // Edit Request
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String? = null,
  // Custom data Request
  @SerializedName("customData") @Expose val customData: String? = null
)


data class MessageList(
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String
)


data class ReplyThread(
  @SerializedName("baseMsgUniqueId") @Expose var parentMsgId: String? = null,
  @SerializedName("replyMsgConfig") @Expose var replyMsgConfig: Int? = null
)


data class LocationReq(
  @SerializedName("address") @Expose var address: String,
  @SerializedName("latitude") @Expose var latitude: Double? = null,
  @SerializedName("longitude") @Expose var longitude: Double? = null
)


data class PhoneContactReq(
  @SerializedName("name") @Expose var name: String,
  @SerializedName("numbers") @Expose var numbersList: List<PhoneNumber>? = null,
  @SerializedName("emails") @Expose var emailsList: List<Email>? = null
)


data class PhoneNumber(
  @SerializedName("type") @Expose var name: String? = null,
  @SerializedName("number") @Expose var senderId: String? = null
)


data class Email(
  @SerializedName("type") @Expose var name: String? = null,
  @SerializedName("email") @Expose var email: String? = null
)


data class EncryptedChat(
  @SerializedName("keyId") @Expose var keyId: String? = null,
  @SerializedName("deviceId") @Expose var deviceId: String? = null,
  @SerializedName("publicKey") @Expose var publicKey: String? = null,
  @SerializedName("eRTCUserId") @Expose var eRTCUserId: String? = null,
  @SerializedName("message") @Expose var message: String? = null,
  @SerializedName("location") @Expose var location: String? = null,
  @SerializedName("contact") @Expose var contact: String? = null,
  @SerializedName("gify") @Expose var gifPath: String? = null,
  @SerializedName("replyThreadFeatureData") @Expose var replyThread: ReplyThread? = null
)


data class SenderKeyDetails(
  @SerializedName("keyId") @Expose var keyId: String? = null,
  @SerializedName("deviceId") @Expose var deviceId: String? = null,
  @SerializedName("publicKey") @Expose var publicKey: String? = null
)


data class Mentions(
  @SerializedName("type") @Expose var type: String? = null,
  @SerializedName("value") @Expose var value: String? = null
)


data class MediaReq(
  @SerializedName("path") @Expose var path: String? = null,
  @SerializedName("thumbnail") @Expose var thumbnail: String? = null,
  @SerializedName("name") @Expose var name: String? = null
)


data class ForwardChat(
  @SerializedName("originalMsgUniqueId") @Expose var forwardMsgUniqueId: String? = null,
  @SerializedName("isForwarded") @Expose var isForwardMessage: Boolean? = true
)