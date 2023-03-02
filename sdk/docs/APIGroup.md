
# APIGroup

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**groupId** | **kotlin.String** | Group ID | 
**name** | **kotlin.String** | Group Name | 
**groupType** | [**inline**](#GroupType) | Type of group. for example privte/public. only private is supported as of now. | 
**createdAt** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Group creation epoch timeStamp | 
**description** | **kotlin.String** | Description of group |  [optional]
**profilePic** | **kotlin.String** | Profile pic url. use chatServer URL as prefix to generate complete URL |  [optional]
**profilePicThumb** | **kotlin.String** | Profile pic thumbnail url. use chatServer URL as prefix to generate complete URL |  [optional]
**creatorId** | **kotlin.String** | appUserId of creator |  [optional]
**threadId** | **kotlin.String** | ThreadId associated with group. To be used for chat |  [optional]
**participants** | [**kotlin.collections.List&lt;Participant&gt;**](Participant.md) | List of participants |  [optional]


<a name="GroupType"></a>
## Enum: groupType
Name | Value
---- | -----
groupType | public, private



