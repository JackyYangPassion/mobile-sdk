package com.ripbull.ertc.mqtt.model

data class AnnouncementResponse(
  var announcement: Announcement
)

data class Announcement(
  var details: String,
  var group: Group?
)

data class Group(
  var groupId: String,
  var groupType: String?,
  var name: String?
)