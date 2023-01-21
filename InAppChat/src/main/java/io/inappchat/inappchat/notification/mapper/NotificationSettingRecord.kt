package io.inappchat.inappchat.notification.mapper

data class NotificationSettingRecord @JvmOverloads constructor(
  var allowFrom: String?,
  var validTill: Long? = null,
  var validTillValue: String? = "always"
)
