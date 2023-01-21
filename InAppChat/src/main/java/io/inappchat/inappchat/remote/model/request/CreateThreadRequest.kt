package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CreateThreadRequest(
  @SerializedName("sendereRTCUserId") @Expose val senderERTCUserId: String,
  @SerializedName("recipientAppUserId") @Expose val recipientAppUserId: String
)