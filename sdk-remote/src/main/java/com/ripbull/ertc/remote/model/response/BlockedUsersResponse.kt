package com.ripbull.ertc.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

data

class BlockedUsersResponse(
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String, @Expose @SerializedName("appUserId") val appUserId: String, @Expose @SerializedName(
    "tenantId"
  ) val tenantId: String
) : ValidItem {
  override fun isValid(): Boolean {
    return appUserId.isNotBlank() && tenantId.isNotBlank()
  }
}