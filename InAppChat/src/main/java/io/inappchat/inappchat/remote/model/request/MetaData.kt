package io.inappchat.inappchat.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

data class MetaData(@SerializedName("requestId") @Expose val requestId: String? = null) :
  ValidItem {
  override fun isValid(): Boolean {
    return requestId?.isNotEmpty() ?: false
  }

}