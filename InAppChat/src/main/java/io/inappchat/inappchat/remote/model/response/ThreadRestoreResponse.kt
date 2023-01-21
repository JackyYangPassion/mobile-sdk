package io.inappchat.inappchat.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

/**
 * Created by DK on 07/12/20.
 */
data class ThreadRestoreResponse(
  @Expose @SerializedName("threads") val threads: List<Thread>?,
  @Expose @SerializedName("total") val total: Int?
): ValidItem {
  override fun isValid(): Boolean  = true
}

data class Thread(
  @Expose @SerializedName("thread") val thread: CreateThreadResponse,
  @Expose @SerializedName("lastMessage") val lastMessage: MessageResponse  //V1
): ValidItem {
  override fun isValid(): Boolean  = true
}


