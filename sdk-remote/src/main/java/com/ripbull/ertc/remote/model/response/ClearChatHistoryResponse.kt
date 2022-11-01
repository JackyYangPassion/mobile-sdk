package com.ripbull.ertc.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

data class ClearChatHistoryResponse(
  @Expose @SerializedName("threadId") val threadId: String?,
  @Expose @SerializedName("timestamp") val timestamp: String?
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}
