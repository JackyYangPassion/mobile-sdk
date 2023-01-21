package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChatDataContent(
  @SerializedName("message") @Expose val message: String
)