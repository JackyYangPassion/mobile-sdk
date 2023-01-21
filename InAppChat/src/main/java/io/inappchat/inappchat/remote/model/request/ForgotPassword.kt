package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by DK on 03/01/19.
 */

data class ForgotPassword(
  @SerializedName("loginType") @Expose val loginType: String,
  @SerializedName("appUserId") @Expose val appUserId: String
)
