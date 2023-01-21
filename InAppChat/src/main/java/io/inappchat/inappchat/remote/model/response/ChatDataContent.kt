package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class ChatDataContent(
  @Expose @SerializedName("message") val message: String
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}