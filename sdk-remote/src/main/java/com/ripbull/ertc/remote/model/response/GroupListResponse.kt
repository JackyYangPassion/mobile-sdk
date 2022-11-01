package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class GroupListResponse(
  @Expose @SerializedName("groups") val groups: List<GroupResponse>
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}