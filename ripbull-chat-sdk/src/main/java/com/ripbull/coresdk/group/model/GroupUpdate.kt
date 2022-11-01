package com.ripbull.coresdk.group.model



/**
 * Created by DK on 03/04/19.
 */

data class GroupUpdate(
  var groupId: String?,
  var threadId: String?,
  var eventTriggeredByUser: EventTriggerByUser?,
  var eventList: List<Event>?
)


data class EventTriggerByUser(
  var eRTCUserId: String?,
  var appUserId: String?
)


data class Event(
  var eventType: String?,
  var eventData: EventData?
)


data class EventData(
  var changeData: ChangeData?,
  var eventTriggeredOnUserList: List<EventTriggeredOnUsers>?
)


data class ChangeData(
  var name: ChangeDataName?
)


data class ChangeDataName(
  var previous: String?,
  var new: String?
)


data class EventTriggeredOnUsers(
  var eRTCUserId: String?,
  var appUserId: String?
)


