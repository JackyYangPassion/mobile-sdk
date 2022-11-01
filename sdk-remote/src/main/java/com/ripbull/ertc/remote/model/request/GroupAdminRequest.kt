package com.ripbull.ertc.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GroupAdminRequest(
  @SerializedName("targetAppUserId") @Expose val appUserId: String
)