
# FcmMqttChatUpdate

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**eRTCUserId** | **kotlin.String** | User unique Identifier |  [optional]
**chats** | [**kotlin.collections.List&lt;ChatObjIndeleteChatResponse&gt;**](ChatObjIndeleteChatResponse.md) | List of deleted chats |  [optional]
**msgUniqueId** | **kotlin.String** | Chat unique Identifier |  [optional]
**updateType** | **kotlin.String** | Type of update. eg. delete/edit |  [optional]
**threadId** | **kotlin.String** | Thread unique identifier |  [optional]
**tenantId** | **kotlin.String** | Tenant unique identifier |  [optional]
**msgCorrelationId** | **kotlin.String** | Client generated unique identifier used to trace message delivery till receiver. |  [optional]
**deleteType** | **kotlin.String** | in case of delete updateType, it specifies sub-type of delete such as self/everyone |  [optional]



