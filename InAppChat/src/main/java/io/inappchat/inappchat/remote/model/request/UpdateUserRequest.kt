package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class UpdateUserRequest @JvmOverloads constructor(
  @SerializedName("appUserId") @Expose val appUserId: String? = null,
  @SerializedName("fcmToken") @Expose val fcmToken: String? = null,
  @SerializedName("deviceId") @Expose val deviceId: String? = null,
  @SerializedName("deviceType") @Expose val deviceType: String? = null,
  @SerializedName("publicKey") @Expose val publicKey: String? = null,
  @SerializedName("availabilityStatus") @Expose val availabilityStatus: String? = null,
  @SerializedName("authPayload") @Expose val authPayload: String? = null
)
