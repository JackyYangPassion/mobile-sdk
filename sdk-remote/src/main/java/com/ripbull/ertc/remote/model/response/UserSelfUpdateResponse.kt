package com.ripbull.ertc.remote.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.ripbull.ertc.remote.core.ValidItem


data class UserSelfUpdateResponse(
  @Expose @SerializedName("eRTCUserId") val eRTCUserId: String,
  @Expose @SerializedName("eventList") val eventList: List<EventItem>
) : ValidItem {

  override fun isValid(): Boolean {
    return true
  }

  inner class EventItem(
    @SerializedName("eventType") @Expose var eventType: String,
    @SerializedName("eventData") @Expose var eventData: EventData
  ) : ValidItem {
    override fun isValid(): Boolean {
      return true
    }

    inner class EventData(
      @SerializedName("threadId") @Expose var threadId: String?,
      @SerializedName("notificationSettings") @Expose var notificationSettings: NotificationSettings?,
      @SerializedName("blockedStatus") @Expose var blockedStatus: String?,
      @SerializedName("availabilityStatus") @Expose var availabilityStatus: String?,
      @SerializedName("targetUser") @Expose var targetUser: TargetUser?,
    ) : ValidItem {
      override fun isValid(): Boolean {
        return true
      }

      inner class NotificationSettings(
        @SerializedName("allowFrom") @Expose var allowFrom: String
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }

      inner class TargetUser(
        @SerializedName("eRTCUserId") @Expose var eRTCUserId: String,
        @SerializedName("appUserId") @Expose var appUserId: String
      ) : ValidItem {
        override fun isValid(): Boolean {
          return true
        }
      }
    }
  }
}