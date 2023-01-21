package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChatData(
  @SerializedName("type") @Expose val type: String,
  @SerializedName("content") @Expose val content: ChatDataContent
)