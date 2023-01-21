package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

data class ClearChatHistoryResponse(
  @Expose @SerializedName("threadId") val threadId: String?,
  @Expose @SerializedName("timestamp") val timestamp: String?
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}
