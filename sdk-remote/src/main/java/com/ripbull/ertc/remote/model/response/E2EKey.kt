package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class E2EKey(
  @Expose @SerializedName("keyId") val keyId: String,
  @Expose @SerializedName("deviceId") val deviceId: String,
  @Expose @SerializedName("publicKey") val publicKey: String,
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String,
  @Expose @SerializedName("returnCode") val returnCode: String? = null
) : ValidItem {

  override fun isValid(): Boolean {
    return publicKey.isNotEmpty() && eRTCUserId.isNotEmpty()
  }
}