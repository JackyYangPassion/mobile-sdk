package com.ripbull.ertc.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UserInfoRequest(
  @Expose @SerializedName("appUserIds") val appUserIds: List<String?> ? = null,
  @Expose @SerializedName("statusKeys") val statusKeys: List<String?> ? = arrayListOf("availabilityStatus","blockedStatus")
)