package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ChatResponse(
  @Expose @SerializedName("_id") val id: String, @Expose @SerializedName("threadId") val threadId: String,
  @Expose @SerializedName("sender") val sender: String, @Expose @SerializedName("createdAt") val createdAt: String,
  @Expose @SerializedName("data") val data: ChatData
) : ValidItem {

  override fun isValid(): Boolean {
    return id.isNotBlank() && threadId.isNotBlank()
  }
}