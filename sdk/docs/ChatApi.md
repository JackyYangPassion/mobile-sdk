# ChatApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**allChatHistoryGet**](ChatApi.md#allChatHistoryGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chat/history | Load chat history
[**chat**](ChatApi.md#chat) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/ | Chat API
[**chatHistoryGet**](ChatApi.md#chatHistoryGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history | Load chat history
[**chatMultiplePost**](ChatApi.md#chatMultiplePost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/multiple | Chat API
[**chatReactionPost**](ChatApi.md#chatReactionPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/reaction | Chat Reaction API
[**chatSearchPost**](ChatApi.md#chatSearchPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/search | Chat Search API
[**clearChatHistory**](ChatApi.md#clearChatHistory) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/{threadId}/history/clear | Load chat history
[**deleteChat**](ChatApi.md#deleteChat) | **DELETE** {version}/tenants/{tenantId}/{eRTCUserId}/chat/ | Delete Chat API
[**e2eEncryptionChat**](ChatApi.md#e2eEncryptionChat) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chat/e2eEncrypted | e2e Encrypted Chat API
[**editChatPut**](ChatApi.md#editChatPut) | **PUT** {version}/tenants/{tenantId}/{eRTCUserId}/chat/ | Edit Chat API
[**getReplyThreads**](ChatApi.md#getReplyThreads) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chat/replyThread/history | Load chat history



Load chat history

Load Chat history on any conversation thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val msgType : kotlin.String = msgType_example // kotlin.String | Msg type, stringified array Example [\"text\".\"gif\"]
val currentMsgId : kotlin.String = currentMsgId_example // kotlin.String | message Id. Example 5fabd417f9e67f996ce84140
val direction : kotlin.String = direction_example // kotlin.String | future/past
val dateFrom : kotlin.String = dateFrom_example // kotlin.String | ISO string of start date
val dateTo : kotlin.String = dateTo_example // kotlin.String | ISO string of end date
val pageSize : kotlin.String = pageSize_example // kotlin.String | page size for pagination
val baseMsgUniqueId : kotlin.String = baseMsgUniqueId_example // kotlin.String | Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5

launch(Dispatchers.IO) {
    val result : ChatHistoryGet200Response = webService.allChatHistoryGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, msgType, currentMsgId, direction, dateFrom, dateTo, pageSize, baseMsgUniqueId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **msgType** | **kotlin.String**| Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] | [optional]
 **currentMsgId** | **kotlin.String**| message Id. Example 5fabd417f9e67f996ce84140 | [optional]
 **direction** | **kotlin.String**| future/past | [optional]
 **dateFrom** | **kotlin.String**| ISO string of start date | [optional]
 **dateTo** | **kotlin.String**| ISO string of end date | [optional]
 **pageSize** | **kotlin.String**| page size for pagination | [optional]
 **baseMsgUniqueId** | **kotlin.String**| Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 | [optional]

### Return type

[**ChatHistoryGet200Response**](ChatHistoryGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Chat API

Send chat over a thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val sendereRTCUserId : kotlin.String = sendereRTCUserId_example // kotlin.String | eRTC user Id of sender. eg. 5c56c9a2218aec4b4a8a976e
val senderTimeStampMs : java.math.BigDecimal = 8.14 // java.math.BigDecimal | epoch timestamp (in ms) of message creation generated on sender device
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is exclusive peer to recipientAppUserId.
val recipientAppUserId : kotlin.String = recipientAppUserId_example // kotlin.String | App user Id of receiver. eg. abc@def.com. This is exclusive peer to threadId.
val message : kotlin.String = message_example // kotlin.String | message text. rg. 'hello'
val msgType : kotlin.String = msgType_example // kotlin.String | message type. Required only for text/contact/location message. Other supported values are gif/sticker
val file : java.io.File = BINARY_DATA_HERE // java.io.File | Media chat file
val replyThreadFeatureData : kotlin.String = replyThreadFeatureData_example // kotlin.String | JSON object of Details in-case it is a reply on another chat. eg. { \\\"baseMsgUniqueId\\\": \\\"5e3841dea49618da0b379480\\\", \\\"replyMsgConfig\\\": 1 }. Please check replyThreadSchemaChatRequest in models
val metadata : kotlin.String = metadata_example // kotlin.String | JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\"abc\\\" : \\\"def\\\" }
val location : kotlin.String = location_example // kotlin.String | JSON object of location. check locationSchema below
val contact : kotlin.String = contact_example // kotlin.String | JSON object of contact. check contactSchema below
val gify : kotlin.String = gify_example // kotlin.String | gify url
val mentions : kotlin.String = mentions_example // kotlin.String | User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\"type\\\":\\\"user\\\",\\\"value\\\":\\\"5c56c9a2218aec4b4a8a976f\\\"}, {\\\"type\\\":\\\"generic\\\",\\\"value\\\":\\\"channel\\\"]
val forwardChatFeatureData : kotlin.String = forwardChatFeatureData_example // kotlin.String | JSON object of Details in-case it is a forward chat. eg. { \\\"originalMsgUniqueId\\\": \\\"5e3841dea49618da0b379480\\\", \\\"isForwarded\\\": true }. Please check forwardChatSchemaChatRequest in models
val media : kotlin.String = media_example // kotlin.String | This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\"path\\\" : \\\"file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\",\\\"name\\\" : \\\"WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\",\\\"thumbnail\\\" : \\\"file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\"}. Please check mediaSchema in models
val msgCorrelationId : kotlin.String = msgCorrelationId_example // kotlin.String | Client generated unique identifier used to trace message delivery till receiver
val customData : kotlin.String = customData_example // kotlin.String | JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\"abc\\\" : \\\"def\\\" }

launch(Dispatchers.IO) {
    val result : Chat200Response = webService.chat(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, sendereRTCUserId, senderTimeStampMs, threadId, recipientAppUserId, message, msgType, file, replyThreadFeatureData, metadata, location, contact, gify, mentions, forwardChatFeatureData, media, msgCorrelationId, customData)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **sendereRTCUserId** | **kotlin.String**| eRTC user Id of sender. eg. 5c56c9a2218aec4b4a8a976e |
 **senderTimeStampMs** | **java.math.BigDecimal**| epoch timestamp (in ms) of message creation generated on sender device |
 **threadId** | **kotlin.String**| Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is exclusive peer to recipientAppUserId. | [optional]
 **recipientAppUserId** | **kotlin.String**| App user Id of receiver. eg. abc@def.com. This is exclusive peer to threadId. | [optional]
 **message** | **kotlin.String**| message text. rg. &#39;hello&#39; | [optional]
 **msgType** | **kotlin.String**| message type. Required only for text/contact/location message. Other supported values are gif/sticker | [optional]
 **file** | **java.io.File**| Media chat file | [optional]
 **replyThreadFeatureData** | **kotlin.String**| JSON object of Details in-case it is a reply on another chat. eg. { \\\&quot;baseMsgUniqueId\\\&quot;: \\\&quot;5e3841dea49618da0b379480\\\&quot;, \\\&quot;replyMsgConfig\\\&quot;: 1 }. Please check replyThreadSchemaChatRequest in models | [optional]
 **metadata** | **kotlin.String**| JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } | [optional]
 **location** | **kotlin.String**| JSON object of location. check locationSchema below | [optional]
 **contact** | **kotlin.String**| JSON object of contact. check contactSchema below | [optional]
 **gify** | **kotlin.String**| gify url | [optional]
 **mentions** | **kotlin.String**| User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\&quot;type\\\&quot;:\\\&quot;user\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;5c56c9a2218aec4b4a8a976f\\\&quot;}, {\\\&quot;type\\\&quot;:\\\&quot;generic\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;channel\\\&quot;] | [optional]
 **forwardChatFeatureData** | **kotlin.String**| JSON object of Details in-case it is a forward chat. eg. { \\\&quot;originalMsgUniqueId\\\&quot;: \\\&quot;5e3841dea49618da0b379480\\\&quot;, \\\&quot;isForwarded\\\&quot;: true }. Please check forwardChatSchemaChatRequest in models | [optional]
 **media** | **kotlin.String**| This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\&quot;path\\\&quot; : \\\&quot;file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;,\\\&quot;name\\\&quot; : \\\&quot;WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\&quot;,\\\&quot;thumbnail\\\&quot; : \\\&quot;file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;}. Please check mediaSchema in models | [optional]
 **msgCorrelationId** | **kotlin.String**| Client generated unique identifier used to trace message delivery till receiver | [optional]
 **customData** | **kotlin.String**| JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } | [optional]

### Return type

[**Chat200Response**](Chat200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json


Load chat history

Load Chat history on any conversation thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val msgType : kotlin.String = msgType_example // kotlin.String | Msg type, stringified array Example [\"text\".\"gif\"]
val currentMsgId : kotlin.String = currentMsgId_example // kotlin.String | message Id. Example 5fabd417f9e67f996ce84140
val direction : kotlin.String = direction_example // kotlin.String | future/past
val dateFrom : kotlin.String = dateFrom_example // kotlin.String | ISO string of start date
val dateTo : kotlin.String = dateTo_example // kotlin.String | ISO string of end date
val pageSize : kotlin.String = pageSize_example // kotlin.String | page size for pagination
val baseMsgUniqueId : kotlin.String = baseMsgUniqueId_example // kotlin.String | Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5
val deep : kotlin.Boolean = true // kotlin.Boolean | When true it returns messages from threads and main window both

launch(Dispatchers.IO) {
    val result : ChatHistoryGet200Response = webService.chatHistoryGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, threadId, xNonce, msgType, currentMsgId, direction, dateFrom, dateTo, pageSize, baseMsgUniqueId, deep)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **threadId** | **kotlin.String**| Thread ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **msgType** | **kotlin.String**| Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] | [optional]
 **currentMsgId** | **kotlin.String**| message Id. Example 5fabd417f9e67f996ce84140 | [optional]
 **direction** | **kotlin.String**| future/past | [optional]
 **dateFrom** | **kotlin.String**| ISO string of start date | [optional]
 **dateTo** | **kotlin.String**| ISO string of end date | [optional]
 **pageSize** | **kotlin.String**| page size for pagination | [optional]
 **baseMsgUniqueId** | **kotlin.String**| Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 | [optional]
 **deep** | **kotlin.Boolean**| When true it returns messages from threads and main window both | [optional]

### Return type

[**ChatHistoryGet200Response**](ChatHistoryGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Chat API

Send multiple text chats

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : kotlin.collections.List<ChatRequestObj> =  // kotlin.collections.List<ChatRequestObj> | Chat multiple request
val context : kotlin.String = context_example // kotlin.String | type of sync i.e. offlineDispatch/forwardChat. default value is offlineDispatch.

launch(Dispatchers.IO) {
    val result : ChatMultiplePost200Response = webService.chatMultiplePost(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body, context)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**kotlin.collections.List&lt;ChatRequestObj&gt;**](ChatRequestObj.md)| Chat multiple request |
 **context** | **kotlin.String**| type of sync i.e. offlineDispatch/forwardChat. default value is offlineDispatch. | [optional]

### Return type

[**ChatMultiplePost200Response**](ChatMultiplePost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Chat Reaction API

Send chat reaction

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : ChatReactionRequest =  // ChatReactionRequest | Chat multiple request

launch(Dispatchers.IO) {
    val result : ChatReactionPost200Response = webService.chatReactionPost(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**ChatReactionRequest**](ChatReactionRequest.md)| Chat multiple request |

### Return type

[**ChatReactionPost200Response**](ChatReactionPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Chat Search API

Chat Search API

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : ChatSearchRequest =  // ChatSearchRequest | Chat multiple request
val skip : kotlin.String = skip_example // kotlin.String | skip value for pagination. i.e. index. default 0
val limit : kotlin.String = limit_example // kotlin.String | limit value for pagination. i.e. page-size. default 10

launch(Dispatchers.IO) {
    val result : ChatSearchPost200Response = webService.chatSearchPost(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body, skip, limit)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**ChatSearchRequest**](ChatSearchRequest.md)| Chat multiple request |
 **skip** | **kotlin.String**| skip value for pagination. i.e. index. default 0 | [optional]
 **limit** | **kotlin.String**| limit value for pagination. i.e. page-size. default 10 | [optional]

### Return type

[**ChatSearchPost200Response**](ChatSearchPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Load chat history

Clear chat history

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : ClearChatHistory200Response = webService.clearChatHistory(authorization, threadId, xRequestSignature, version, tenantId, eRTCUserId, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **threadId** | **kotlin.String**| Thread ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**ClearChatHistory200Response**](ClearChatHistory200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Delete Chat API

Delete particular chat

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : DeleteChatRequestObj =  // DeleteChatRequestObj | delete chat body

launch(Dispatchers.IO) {
    val result : DeleteChat200Response = webService.deleteChat(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**DeleteChatRequestObj**](DeleteChatRequestObj.md)| delete chat body |

### Return type

[**DeleteChat200Response**](DeleteChat200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


e2e Encrypted Chat API

Send e2e encrypted chat over a thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : E2eEncryptedChatRequestObj =  // E2eEncryptedChatRequestObj | e2e Encrypted Cha

launch(Dispatchers.IO) {
    val result : Chat200Response = webService.e2eEncryptionChat(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**E2eEncryptedChatRequestObj**](E2eEncryptedChatRequestObj.md)| e2e Encrypted Cha |

### Return type

[**Chat200Response**](Chat200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Edit Chat API

Edit particular chat

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : EditChatRequestObj =  // EditChatRequestObj | delete chat body

launch(Dispatchers.IO) {
    val result : EditChatPut200Response = webService.editChatPut(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| Source device ID |
 **body** | [**EditChatRequestObj**](EditChatRequestObj.md)| delete chat body |

### Return type

[**EditChatPut200Response**](EditChatPut200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Load chat history

Load Chat history on any conversation thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val follow : kotlin.Boolean = true // kotlin.Boolean | To get all threads user following, just send true
val starred : kotlin.Boolean = true // kotlin.Boolean | To get all starred messages, just send true
val msgType : kotlin.String = msgType_example // kotlin.String | Msg type, stringified array Example [\"text\".\"gif\"]
val currentMsgId : kotlin.String = currentMsgId_example // kotlin.String | message Id. Example 5fabd417f9e67f996ce84140
val direction : kotlin.String = direction_example // kotlin.String | future/past
val pageSize : kotlin.String = pageSize_example // kotlin.String | page size for pagination
val baseMsgUniqueId : kotlin.String = baseMsgUniqueId_example // kotlin.String | Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5
val deep : kotlin.Boolean = true // kotlin.Boolean | When true it returns messages from threads and main window both

launch(Dispatchers.IO) {
    val result : GetReplyThreads200Response = webService.getReplyThreads(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, threadId, follow, starred, msgType, currentMsgId, direction, pageSize, baseMsgUniqueId, deep)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **threadId** | **kotlin.String**| Thread ID | [optional]
 **follow** | **kotlin.Boolean**| To get all threads user following, just send true | [optional]
 **starred** | **kotlin.Boolean**| To get all starred messages, just send true | [optional]
 **msgType** | **kotlin.String**| Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] | [optional]
 **currentMsgId** | **kotlin.String**| message Id. Example 5fabd417f9e67f996ce84140 | [optional]
 **direction** | **kotlin.String**| future/past | [optional]
 **pageSize** | **kotlin.String**| page size for pagination | [optional]
 **baseMsgUniqueId** | **kotlin.String**| Base message id in-case reply thread restoration is happening. Example 6005dcbda026a25c2ce5f9b5 | [optional]
 **deep** | **kotlin.Boolean**| When true it returns messages from threads and main window both | [optional]

### Return type

[**GetReplyThreads200Response**](GetReplyThreads200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

