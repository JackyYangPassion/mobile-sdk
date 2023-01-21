package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

data class MessageReportResponse(
  @Expose @SerializedName("chatReportId") val chatReportId: String,
  @Expose @SerializedName("tenantId") val tenantId: String?
) : ValidItem {

  override fun isValid(): Boolean {
    return chatReportId.isNotBlank()
  }
}
