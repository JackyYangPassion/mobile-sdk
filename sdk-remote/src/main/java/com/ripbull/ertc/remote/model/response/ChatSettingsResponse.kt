package com.ripbull.ertc.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

data class ChatSettingsResponse(
  @Expose @SerializedName("tenantId") val tenantId: String,
  @Expose @SerializedName("scope") val scope: String?,
  @Expose @SerializedName("profanityFilter") val profanityFilter: ProfanityFilter?,
  @Expose @SerializedName("domainFilter") val domainFilter: DomainFilter?
): ValidItem {

  override fun isValid(): Boolean {
    return tenantId.isNotBlank()
  }
}

data class ProfanityFilter(
  @Expose @SerializedName("keywords") val keywords: List<String>?,
  @Expose @SerializedName("regexes") val regexes: List<String>?,
  @Expose @SerializedName("actionType") val actionType: String?
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}

data class DomainFilter(
  @Expose @SerializedName("domains") val domains: List<String>?,
  @Expose @SerializedName("actionType") val actionType: String?
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}