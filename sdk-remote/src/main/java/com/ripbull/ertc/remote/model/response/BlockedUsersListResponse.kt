package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class BlockedUsersListResponse(
  @Expose @SerializedName("blockedUsers") val userList: List<BlockedUsersResponse>
) : ValidItem {

  override fun isValid(): Boolean {
    return !userList.isEmpty()
  }
}
