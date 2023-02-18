# DraftApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**draftUpdate**](DraftApi.md#draftUpdate) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/draft/ | Draft API
[**getDrafts**](DraftApi.md#getDrafts) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/draft/ | Draft API



Draft API

Send chat over a thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(DraftApi::class.java)
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
val metadata : kotlin.String = metadata_example // kotlin.String | JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\"abc\\\" : \\\"def\\\" }
val location : kotlin.String = location_example // kotlin.String | JSON object of location. check locationSchema below
val contact : kotlin.String = contact_example // kotlin.String | JSON object of contact. check contactSchema below
val gify : kotlin.String = gify_example // kotlin.String | gify url
val mentions : kotlin.String = mentions_example // kotlin.String | User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\"type\\\":\\\"user\\\",\\\"value\\\":\\\"5c56c9a2218aec4b4a8a976f\\\"}, {\\\"type\\\":\\\"generic\\\",\\\"value\\\":\\\"channel\\\"]
val media : kotlin.String = media_example // kotlin.String | This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\"path\\\" : \\\"file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\",\\\"name\\\" : \\\"WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\",\\\"thumbnail\\\" : \\\"file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\"}. Please check mediaSchema in models
val msgCorrelationId : kotlin.String = msgCorrelationId_example // kotlin.String | Client generated unique identifier used to trace message delivery till receiver
val customData : kotlin.String = customData_example // kotlin.String | JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\"abc\\\" : \\\"def\\\" }

launch(Dispatchers.IO) {
    val result : GetDrafts200Response = webService.draftUpdate(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, sendereRTCUserId, senderTimeStampMs, threadId, recipientAppUserId, message, msgType, file, metadata, location, contact, gify, mentions, media, msgCorrelationId, customData)
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
 **metadata** | **kotlin.String**| JSON object which can be used for client reference in request/response context. Server will not do any processing on this object. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } | [optional]
 **location** | **kotlin.String**| JSON object of location. check locationSchema below | [optional]
 **contact** | **kotlin.String**| JSON object of contact. check contactSchema below | [optional]
 **gify** | **kotlin.String**| gify url | [optional]
 **mentions** | **kotlin.String**| User mentions list. Please check mentionsSchemaChatRequest below. For example, [{\\\&quot;type\\\&quot;:\\\&quot;user\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;5c56c9a2218aec4b4a8a976f\\\&quot;}, {\\\&quot;type\\\&quot;:\\\&quot;generic\\\&quot;,\\\&quot;value\\\&quot;:\\\&quot;channel\\\&quot;] | [optional]
 **media** | **kotlin.String**| This is only allowed in forward messages. i.e. if forwardChatFeatureData exists. JSON object of Details in-case it is a forward chat. eg.{\\\&quot;path\\\&quot; : \\\&quot;file/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;,\\\&quot;name\\\&quot; : \\\&quot;WhatsApp Image 2020-08-25 at 2.36.35 PM.jpeg\\\&quot;,\\\&quot;thumbnail\\\&quot; : \\\&quot;file/imageThumbnail/chat/5eef7e13df287e0ebaac34b35eef97669c612ed1ca595314_1598610192772.jpeg\\\&quot;}. Please check mediaSchema in models | [optional]
 **msgCorrelationId** | **kotlin.String**| Client generated unique identifier used to trace message delivery till receiver | [optional]
 **customData** | **kotlin.String**| JSON object which can be used for customer specific data which is not supported in InAppChat chat model. eg. { \\\&quot;abc\\\&quot; : \\\&quot;def\\\&quot; } | [optional]

### Return type

[**GetDrafts200Response**](GetDrafts200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json


Draft API

Send chat over a thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(DraftApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val threadId : kotlin.String = threadId_example // kotlin.String | Get draft for a particular thread
val baseMsgUniqueId : kotlin.String = baseMsgUniqueId_example // kotlin.String | Get thread for a reply thread, this is base message id

launch(Dispatchers.IO) {
    val result : GetDrafts200Response = webService.getDrafts(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, threadId, baseMsgUniqueId)
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
 **threadId** | **kotlin.String**| Get draft for a particular thread | [optional]
 **baseMsgUniqueId** | **kotlin.String**| Get thread for a reply thread, this is base message id | [optional]

### Return type

[**GetDrafts200Response**](GetDrafts200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

