package com.ripbull.coresdk.user.mapper


import java.io.Serializable

/** Created by DK on 09/03/19.  */

data class UserRecord @JvmOverloads constructor(
  var id: String? = null,
  var tenantId: String?= null,
  var name: String?= null,
  var appState: String? = null,
  var loginType: String?= null,
  var profilePic: String?= null,
  var profileThumb: String?= null,
  var profileStatus: String?= null,
  var loginTimestamp: Long?= null,
  var userChatId: String?= null,
  var role: String?= null,
  var ertcId: String?= null,
  var availabilityStatus: String?= null,
  var blockedStatus: String?= null,
  var notificationSettings: String? = null
) : Serializable {
  override fun toString(): String {
    return id.toString()
  }
}