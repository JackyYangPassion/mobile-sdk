package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChatReactionRequest @JvmOverloads constructor(
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String? = null,
  @SerializedName("emojiCode") @Expose val emojiCode: String? = null,
  @SerializedName("action") @Expose val action: String? = null
)
