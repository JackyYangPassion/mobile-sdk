package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class ChatReactionResponse(
  @Expose @SerializedName("msgUniqueId") val msgUniqueId: String,
  @Expose @SerializedName("emojiCode") val emojiCode: String?
) : ValidItem {

  override fun isValid(): Boolean {
    return msgUniqueId.isNotBlank()
  }
}