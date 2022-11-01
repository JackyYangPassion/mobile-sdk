package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class GroupResponse(
  @Expose @SerializedName("name") val name: String,
  @Expose @SerializedName("groupType") val groupType: String,
  @Expose @SerializedName("description") val description: String,
  @Expose @SerializedName("participants") val participants: List<GroupParticipant>,
  @Expose @SerializedName("groupId") val groupId: String,
  @Expose @SerializedName("profilePic") val profilePic: String,
  @Expose @SerializedName("profilePicThumb") val profilePicThumb: String,
  @Expose @SerializedName("creatorId") val creatorId: String,
  @Expose @SerializedName("threadId") val threadId: String,
  @Expose @SerializedName("tenantId") val tenantId: String,
  @Expose @SerializedName("createdAt") val createdAt: Long,
  @Expose @SerializedName("joined") val joined: Boolean = true,
  @Expose @SerializedName("participantsCount") val participantsCount: Int = 0,
  @Expose @SerializedName("freeze") val freeze: Freeze? = null
) : ValidItem {

  override fun isValid(): Boolean {
    return name.isNotBlank() && groupId.isNotBlank()
  }

  inner class Freeze(
    @Expose @SerializedName("enabled") val enabled: Boolean = false
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }
  }
}