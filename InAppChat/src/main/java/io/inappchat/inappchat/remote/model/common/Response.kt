package io.inappchat.inappchat.remote.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

data class Response(
  @Expose @SerializedName("success") var success: Boolean = false,
  @Expose @SerializedName("msg") var message: String? = null,
  @Expose @SerializedName("errorCode") var errorCode: String? = null
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}