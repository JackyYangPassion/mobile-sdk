package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ChatDataContent(
  @Expose @SerializedName("message") val message: String
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}