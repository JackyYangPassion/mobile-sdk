
# UserSelfUpdateEventData

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**threadId** | **kotlin.String** | Optional. Thread id only applicable for notificationSettingsChangedThread |  [optional]
**notificationSettings** | [**NotificationSettings**](NotificationSettings.md) |  |  [optional]
**availabilityStatus** | [**AvailabilityStatus**](AvailabilityStatus.md) |  |  [optional]
**blockedStatus** | [**inline**](#BlockedStatus) | Optional. Only applicable for userBlockedStatusChanged. Blocked status i.e. blocked/unblocked |  [optional]
**targetUser** | [**APIUser**](APIUser.md) |  |  [optional]


<a name="BlockedStatus"></a>
## Enum: blockedStatus
Name | Value
---- | -----
blockedStatus | blocked, unblocked



