package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class UserAdditionalInfo(
  @Expose @SerializedName("chatUserDetails") val userList: List<UserResponse>?
) :
  ValidItem {

  override fun isValid(): Boolean {
    return userList?.isNotEmpty()?:false
  }
}