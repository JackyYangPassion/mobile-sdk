package com.ripbull.ertc.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

data class MetaData(@SerializedName("requestId") @Expose val requestId: String? = null) :
  ValidItem {
  override fun isValid(): Boolean {
    return requestId?.isNotEmpty() ?: false
  }

}