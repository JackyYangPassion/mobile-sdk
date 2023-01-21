package io.inappchat.inappchat.fcm


import java.io.Serializable

/**
 * Created by DK on 06/03/20.
 */

data class NotificationRecord @JvmOverloads constructor(
  var title: String? = null,
  var body: String?= null
) : Serializable