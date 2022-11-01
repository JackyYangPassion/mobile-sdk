package com.ripbull.ertc.mqtt.model

/**
 * Created by DK on 03/04/19.
 */

data class UserMetaData(
  var eRTCUserId: String?,
  var appUserId: String?,
  var availabilityStatus: String?
)
