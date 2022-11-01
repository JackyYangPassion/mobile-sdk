package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class Participant(
  @Expose @SerializedName("user") val eRTCRecipientId: String?,
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String?, //duplicate ID
  @Expose @SerializedName("appUserId") val appUserId: String?,
  @Expose @SerializedName("notificationSettings") val notificationSettings: NotificationSettings? = null,
  @Expose @SerializedName("role") val role: String?,
  @Expose @SerializedName("joinedAtDate") val joinedAtDate: String?
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }

  inner class NotificationSettings(
    @SerializedName("allowFrom") @Expose var allowFrom: String
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }
}