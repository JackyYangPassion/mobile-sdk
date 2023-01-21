package io.inappchat.inappchat.remote.model.request


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class GroupParticipantsRequest(
  @SerializedName("participants") @Expose val participants: List<String>
)