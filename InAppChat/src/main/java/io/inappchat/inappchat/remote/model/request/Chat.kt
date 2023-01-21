package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Chat(
  @SerializedName("threadId") @Expose val threadId: String,
  @SerializedName("sender") @Expose val sender: String,
  @SerializedName("data") @Expose val chatData: ChatData
)