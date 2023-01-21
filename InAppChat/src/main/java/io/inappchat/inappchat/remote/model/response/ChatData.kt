package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class ChatData(
  @Expose @SerializedName("type") val type: String,
  @Expose @SerializedName("content") val c: String
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}