package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ThreadMemberResponse(
  @Expose @SerializedName("user") val user: String, @Expose @SerializedName("joinedAt") val joinedAt: String
) : ValidItem {

  override fun isValid(): Boolean {
    return user.isNotBlank()
  }
}