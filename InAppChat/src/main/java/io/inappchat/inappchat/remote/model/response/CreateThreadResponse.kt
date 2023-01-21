package io.inappchat.inappchat.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.inappchat.inappchat.remote.core.ValidItem


data class CreateThreadResponse(
  @Expose @SerializedName("threadId") val threadId: String,
  @Expose @SerializedName("tenantId") val tenantId: String,
  @Expose @SerializedName("threadType") val threadType: String,
  @Expose @SerializedName("recipientAppUserId") val recipientAppUserId: String?,
  @Expose @SerializedName("createdAt") val createdAt: Long,
  @Expose @SerializedName("participants") val participantsList: List<Participant>,
  @Expose @SerializedName("e2eEncryptionKeys") val e2eKeys: List<E2EKey>?,
  @Expose @SerializedName("group") val group: GroupResponse?,
  @Expose @SerializedName("user") val user: User?,
  @Expose @SerializedName("lastMessage") val lastMessage: MessageResponse?   //V2
) : ValidItem {

  override fun isValid(): Boolean {
    return threadId.isNotEmpty() && participantsList.isNotEmpty()
  }
}

data class User(
  @Expose @SerializedName("appUserId")
  val appUserId: String?,
  @Expose @SerializedName("availabilityStatus")
  val availabilityStatus: String?,
  @Expose @SerializedName("eRTCUserId")
  val eRTCUserId: String?,
  @Expose @SerializedName("name")
  val name: String?
): ValidItem {
  override fun isValid(): Boolean  = true
}

