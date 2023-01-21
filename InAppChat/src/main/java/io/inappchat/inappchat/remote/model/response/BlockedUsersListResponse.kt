package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class BlockedUsersListResponse(
  @Expose @SerializedName("blockedUsers") val userList: List<BlockedUsersResponse>
) : ValidItem {

  override fun isValid(): Boolean {
    return !userList.isEmpty()
  }
}
