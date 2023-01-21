package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class ThreadMemberResponse(
  @Expose @SerializedName("user") val user: String, @Expose @SerializedName("joinedAt") val joinedAt: String
) : ValidItem {

  override fun isValid(): Boolean {
    return user.isNotBlank()
  }
}