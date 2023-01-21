package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem

/**
 * @author Vivek
 */

data class Token(
  @Expose @SerializedName("refreshToken") val refreshToken: String,
  @Expose @SerializedName("accessToken") val accessToken: String,
  @Expose @SerializedName("expiresIn") val expiresIn: Integer
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }
}