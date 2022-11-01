package com.ripbull.ertc.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

/**
 * Created by DK on 08/12/20.
 */
data class ChatRestoreResponse(
  @Expose @SerializedName("chats")
  val chats: List<MessageResponse>?,
  @Expose @SerializedName("total")
  val total: Int?
): ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}
