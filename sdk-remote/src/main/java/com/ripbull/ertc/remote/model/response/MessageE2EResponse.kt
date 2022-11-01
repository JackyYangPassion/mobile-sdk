package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem
import com.ripbull.ertc.remote.model.request.MetaData


data class MessageE2EResponse(
  @Expose @SerializedName("msgUniqueId") val msgUniqueId: String?,
  @Expose @SerializedName("threadId") val threadId: String?,
  @Expose @SerializedName("tenantId") val tenantId: String?,
  @Expose @SerializedName("msgType") val msgType: String?,
  @Expose @SerializedName("metadata") val data: MetaData?,
  @Expose @SerializedName("createdAt") val createdAt: Long?,
  @Expose @SerializedName("chatStatus") val chatStatus: ChatStatus? = null,
  @Expose @SerializedName("replyThreadFeatureData") val replyThreadFeatureData: ReplyThreadResponse?
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}


data class ChatStatus(
  @SerializedName("returnCode") @Expose var returnCode: String? = null,
  @SerializedName("keyList") @Expose var keyList: List<E2EKey>? = null,
  @SerializedName("retryRequired") @Expose var retryRequired: Boolean
)  : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}