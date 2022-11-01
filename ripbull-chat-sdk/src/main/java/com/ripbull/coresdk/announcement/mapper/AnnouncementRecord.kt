package com.ripbull.coresdk.announcement.mapper

data class AnnouncementRecord(
  val details: String,
  val groupId: String? = null,
  val groupType: String? = null,
  val name: String? = null
)
