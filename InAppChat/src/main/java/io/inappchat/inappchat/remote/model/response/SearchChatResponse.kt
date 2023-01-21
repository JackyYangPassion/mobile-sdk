package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

data class SearchChatResponse (
  @Expose @SerializedName("messages") val searchMessageResponse: SearchMessageResponse
) : ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}

data class SearchMessageResponse(
  @SerializedName("total") @Expose var total: Int = 0,
  @Expose @SerializedName("chats") val messageResponse: List<MessageResponse>,
  @Expose @SerializedName("threads") val threads: List<CreateThreadResponse>
) : ValidItem {
  override fun isValid(): Boolean  = true
}