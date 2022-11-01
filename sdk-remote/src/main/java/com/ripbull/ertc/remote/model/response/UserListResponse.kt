package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

/**
 * @author meeth
 */

data class UserListResponse(
  @Expose @SerializedName("chatUsers") val userList: List<UserResponse>,
  @Expose @SerializedName("moreDataAvailable") val moreDataAvailable: Boolean
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}
