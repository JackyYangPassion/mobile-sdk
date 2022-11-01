package com.ripbull.ertc.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem

/**
 * Created by DK on 01/01/2022.
 */
data class FollowThreadResponse(
  @Expose @SerializedName("chats") val chats: List<MessageResponse>?
): ValidItem {
  override fun isValid(): Boolean {
    return true
  }
}
