package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

/**
 * @author meeth
 */

data class UserResponse(
  @Expose @SerializedName("userId") val userId: String?,
  @Expose @SerializedName("appUserId") val appUserId: String,
  @Expose @SerializedName("name") val name: String?,
  @Expose @SerializedName("appState") val appState: String?,
  @Expose @SerializedName("profilePic") val profilePic: String?,
  @Expose @SerializedName("profileStatus") val profileStatus: String?,
  @Expose @SerializedName("loginTimeStamp") val loginTimeStamp: Long? = 0,
  @Expose @SerializedName("profilePicThumb") val profilePicThumb: String?,
  @Expose @SerializedName("token") val token: Token?,
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String?,
  @Expose @SerializedName("statusDetails")val statusDetails: UserState?,
  @Expose @SerializedName("e2eEncryptionKeys") val e2eEncryptionKeys: List<E2EKey>? = null
) : ValidItem {

  override fun isValid(): Boolean = true

  inner class UserState(
    @Expose @SerializedName("availabilityStatus") var availabilityStatus: String?,
    @Expose @SerializedName("blockedStatus") var blockedStatus: String?
  ) : ValidItem {
    override fun isValid(): Boolean = true
  }
}