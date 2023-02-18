
# ThreadReplyReplies

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **kotlin.String** | uniqueId of the first reply messageL |  [optional]
**threadId** | **kotlin.String** | Id of the thread |  [optional]
**message** | **kotlin.String** | text sent in message |  [optional]
**msgType** | **kotlin.String** | type of message |  [optional]
**sendereRTCUserId** | **kotlin.String** | Sender eRTCUserId of message |  [optional]
**replyThreadFeatureData** | [**ReplyThreadSchemaChatRequest**](ReplyThreadSchemaChatRequest.md) |  |  [optional]
**senderTimeStampMs** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | epoch timestamp (in ms) of message creation generated on sender device |  [optional]
**tenantId** | **kotlin.String** | Tenant Id |  [optional]
**isEdited** | **kotlin.Boolean** | If chat message is starred |  [optional]
**reactions** | [**kotlin.collections.List&lt;ReactionInChatHistorySchema&gt;**](ReactionInChatHistorySchema.md) | list of reactions |  [optional]



