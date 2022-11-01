package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ThreadResponse(
  @Expose @SerializedName("_id") val id: String, @Expose @SerializedName("createdAt") val createdAt: String,
  @Expose @SerializedName("members") val memberList: List<ThreadMemberResponse>
) : ValidItem {

  override fun isValid(): Boolean {
    return id.isNotBlank()
  }
}