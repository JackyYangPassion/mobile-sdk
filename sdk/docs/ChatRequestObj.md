
# ChatRequestObj

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**threadId** | **kotlin.String** | Thread Id. This is exclusive peer to recipientAppUserId. |  [optional]
**recipientAppUserId** | **kotlin.String** | App user Id of receiver. This is exclusive peer to threadId. |  [optional]
**sendereRTCUserId** | **kotlin.String** | eRTC user id of source user |  [optional]
**message** | **kotlin.String** | message text |  [optional]
**msgType** | **kotlin.String** | message type. it can be text/contact/location/gify |  [optional]
**contact** | [**ContactSchema**](ContactSchema.md) |  |  [optional]
**location** | [**LocationSchema**](LocationSchema.md) |  |  [optional]
**gify** | **kotlin.String** | gify URL |  [optional]
**metadata** | [**kotlin.Any**](.md) | JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \&quot;abc\&quot; : \&quot;def\&quot; } |  [optional]
**replyThreadFeatureData** | [**ReplyThreadSchemaChatRequest**](ReplyThreadSchemaChatRequest.md) |  |  [optional]
**forwardChatFeatureData** | [**ForwardChatSchemaChatRequest**](ForwardChatSchemaChatRequest.md) |  |  [optional]
**mentions** | [**kotlin.collections.List&lt;MentionSchema&gt;**](MentionSchema.md) |  |  [optional]
**msgCorrelationId** | **kotlin.String** | Client generated unique identifier used to trace message delivery till receiver. |  [optional]
**senderTimeStampMs** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | epoch timestamp (in ms) of message creation generated on sender device |  [optional]
**customData** | [**kotlin.Any**](.md) | JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \&quot;abc\&quot; : \&quot;def\&quot; } |  [optional]



