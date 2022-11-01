package com.ripbull.ertc.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Sagar on 24/11/2019.
 */
data class Logout(
  @SerializedName("appUserId") @Expose val appUserId: String,
  @SerializedName("deviceId") @Expose val deviceId: String
)
