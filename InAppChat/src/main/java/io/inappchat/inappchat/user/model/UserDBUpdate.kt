package io.inappchat.inappchat.user.model

/**
 * Created by DK on 03/04/19.
 */

data class UserDBUpdate(
  var event: String,
  var appUserIds: List<String>
)
