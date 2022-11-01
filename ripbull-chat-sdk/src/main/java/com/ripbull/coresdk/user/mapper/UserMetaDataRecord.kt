package com.ripbull.coresdk.user.mapper



/**
 * Created by Sagar on 11/06/2020.
 */

data class UserMetaDataRecord (
  var appUserId: String?,
  val notificationSettings: String? = null,
  val availabilityStatus: String? = null
)