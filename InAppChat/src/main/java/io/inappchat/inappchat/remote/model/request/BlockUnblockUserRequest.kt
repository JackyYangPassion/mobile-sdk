package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BlockUnblockUserRequest(
  @SerializedName("appUserId") @Expose val appUserId: String? = null
)
