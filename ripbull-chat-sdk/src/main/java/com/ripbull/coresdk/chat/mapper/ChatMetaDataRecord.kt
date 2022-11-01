package com.ripbull.coresdk.chat.mapper



/**
 * Created by Sagar on 11/06/2020.
 */

data class ChatMetaDataRecord(
  val threadId: String? = null,
  val notificationSettings: String? = null,
  val chatCleared: Boolean? = false
)