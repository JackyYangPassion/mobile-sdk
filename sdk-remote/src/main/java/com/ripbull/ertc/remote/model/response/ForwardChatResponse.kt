package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ForwardChatResponse (
  @Expose @SerializedName("results") val messageResponse: List<MessageResponse>
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}