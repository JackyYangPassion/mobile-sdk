package com.ripbull.ertc.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationSettingsRequest(
  @SerializedName("notificationSettings") @Expose var settings: NotificationSettings? = null
)

data class NotificationSettings(
  @SerializedName("allowFrom") @Expose var allowFrom: String? = null,
  @SerializedName("validTill") @Expose var validTill: String? = null,
  @SerializedName("validTillDisplayValue") @Expose var validTillDisplayValue: String? = null
)