package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Thread(
  @SerializedName("eRTCUserIdSender") @Expose val eRTCUserIdSender: String,
  @SerializedName("appUserIdReceiver") @Expose val appUserIdReceiver: String
)