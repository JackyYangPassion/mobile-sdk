package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ChatUserResponse(
  @SerializedName("eRTCUserId") @Expose val eRTCUserId: String,
  @SerializedName("appUserId") @Expose val appUserId: String,
  @SerializedName("name") @Expose val name: String,
  @SerializedName("tenantId") @Expose val tenantId: String,
  @SerializedName("token") @Expose val token: Token?,
  @SerializedName("e2eEncryptionKeys") @Expose val e2eKeys: List<E2EKey>?,
  @SerializedName("availabilityStatus") @Expose val availabilityStatus: String? = null,
  @SerializedName("notificationSettings") @Expose val notificationSettings: NotificationSettings? = null
) : ValidItem {
  override fun isValid(): Boolean {
    return eRTCUserId.isNotEmpty() && appUserId.isNotEmpty()
  }

  inner class NotificationSettings(
    @SerializedName("allowFrom") @Expose var allowFrom: String
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }
}
