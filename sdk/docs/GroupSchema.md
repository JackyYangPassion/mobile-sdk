
# GroupSchema

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**groupId** | **kotlin.String** | Group ID |  [optional]
**name** | **kotlin.String** | Group Name |  [optional]
**groupType** | **kotlin.String** | Type of group. for example privte/public. only private is supported as of now. |  [optional]
**description** | **kotlin.String** | Description of group |  [optional]
**profilePic** | **kotlin.String** | Profile pic url. use chatServer URL as prefix to generate complete URL  |  [optional]
**profilePicThumb** | **kotlin.String** | Profile pic thumbnail url. use chatServer URL as prefix to generate complete URL  |  [optional]
**createdAt** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Group creation epoch timeStamp |  [optional]
**creatorId** | **kotlin.String** | appUserId of creator |  [optional]
**threadId** | **kotlin.String** | ThreadId associated with group. To be used for chat |  [optional]
**tenantId** | **kotlin.String** | Tenant ID |  [optional]
**participants** | [**kotlin.collections.List&lt;GroupParticipantSchema&gt;**](GroupParticipantSchema.md) | List of participants |  [optional]



