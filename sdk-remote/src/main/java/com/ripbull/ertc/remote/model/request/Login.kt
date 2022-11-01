package com.ripbull.ertc.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by DK on 03/01/19.
 */

data class Login(
  @SerializedName("loginType") @Expose val loginType: String,
  @SerializedName("appUserId") @Expose val appUserId: String,
  @SerializedName("password") @Expose val password: String,
  @SerializedName("fcmToken") @Expose val fcmToken: String? = null,
  @SerializedName("deviceId") @Expose val deviceId: String,
  @SerializedName("deviceType") @Expose val deviceType: String
)
