package com.ripbull.ertc.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReportMessageRequest(
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String?,
  @SerializedName("category") @Expose var category: String,
  @SerializedName("reason") @Expose var reason: String?
)
