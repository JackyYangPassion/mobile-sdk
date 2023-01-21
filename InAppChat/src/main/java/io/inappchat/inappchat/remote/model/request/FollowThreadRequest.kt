package io.inappchat.inappchat.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowThreadRequest(
  @SerializedName("msgUniqueId") @Expose val msgUniqueId: String?,
  @SerializedName("threadId") @Expose var threadId: String?,
  @SerializedName("follow") @Expose var follow: Boolean = true
)
