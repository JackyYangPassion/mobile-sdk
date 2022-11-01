package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class ChatData(
  @Expose @SerializedName("type") val type: String,
  @Expose @SerializedName("content") val c: String
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}