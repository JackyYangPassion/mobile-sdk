package com.ripbull.ertc.remote.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

data class Response(
  @Expose @SerializedName("success") var success: Boolean = false,
  @Expose @SerializedName("msg") var message: String? = null,
  @Expose @SerializedName("errorCode") var errorCode: String? = null
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}