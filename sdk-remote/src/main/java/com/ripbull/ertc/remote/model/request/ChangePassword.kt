package com.ripbull.ertc.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by DK on 03/01/19.
 */
data class ChangePassword(
  @SerializedName("loginType") @Expose val loginType: String,
  @SerializedName("appUserId") @Expose val appUserId: String,
  @SerializedName("currentPassword") @Expose val currentPassword: String,
  @SerializedName("newPassword") @Expose val newPassword: String
)
