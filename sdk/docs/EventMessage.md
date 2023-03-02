
# EventMessage

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**eRTCUserId** | **kotlin.String** | The user ID | 
**eventList** | [**kotlin.collections.List&lt;GroupUpdateEventItem&gt;**](GroupUpdateEventItem.md) |  | 
**eventTriggeredByUser** | [**APIUser**](APIUser.md) |  | 
**groupId** | **kotlin.String** | The group receiving the typing status | 
**threadId** | **kotlin.String** | Thread ID of associated group | 
**msgUniqueId** | **kotlin.String** | The ID of the message | 
**emojiCode** | **kotlin.String** | Emoje code string | 
**action** | **kotlin.String** | Reaction actionType. It can be set/clear | 
**totalCount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Total count of particular reaction with emojiCode | 
**updateType** | [**inline**](#UpdateType) | Type of update.  | 
**deleteType** | [**inline**](#DeleteType) | in case of delete updateType, it specifies sub-type of delete such as self/everyone | 
**typingStatusEvent** | [**inline**](#TypingStatusEvent) | Whether or not the user is typing | 
**msgReadStatus** | [**inline**](#MsgReadStatus) | The status of the message | 
**availabilityStatus** | [**AvailabilityStatus**](AvailabilityStatus.md) |  | 
**message** | [**APIMessage**](APIMessage.md) |  | 
**tenantId** | **kotlin.String** |  | 
**chatReportId** | **kotlin.String** |  |  [optional]
**event** | [**ChatReportEventEvent**](ChatReportEventEvent.md) |  |  [optional]
**appUserId** | **kotlin.String** | The user receiving the typing status |  [optional]


<a name="UpdateType"></a>
## Enum: updateType
Name | Value
---- | -----
updateType | delete, edit


<a name="DeleteType"></a>
## Enum: deleteType
Name | Value
---- | -----
deleteType | self, everyone


<a name="TypingStatusEvent"></a>
## Enum: typingStatusEvent
Name | Value
---- | -----
typingStatusEvent | on, off


<a name="MsgReadStatus"></a>
## Enum: msgReadStatus
Name | Value
---- | -----
msgReadStatus | delivered, seen



