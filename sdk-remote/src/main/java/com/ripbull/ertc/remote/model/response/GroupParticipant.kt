package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class GroupParticipant(
  @Expose @SerializedName("appUserId") val appUserId: String,
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String,
  @Expose @SerializedName("role") val role: String,
  @Expose @SerializedName("joinedAtDate") val joinedAtDate: String,
  @Expose @SerializedName("e2eEncryptionKeys") val e2eEncryptionKeys: List<E2EKey>? = null,
  @Expose @SerializedName("isDeleted") val isDeleted: Boolean = false
) : ValidItem {

  override fun isValid(): Boolean {
    return appUserId.isNotBlank() && eRTCUserId.isNotBlank()
  }
}