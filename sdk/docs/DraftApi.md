# DraftApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getDrafts**](DraftApi.md#getDrafts) | **GET** draft | Draft API
[**updateDraft**](DraftApi.md#updateDraft) | **POST** draft | Draft API



Draft API

Send chat over a thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(DraftApi::class.java)
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val threadId : kotlin.String = threadId_example // kotlin.String | Get draft for a particular thread
val baseMsgUniqueId : kotlin.String = baseMsgUniqueId_example // kotlin.String | Get thread for a reply thread, this is base message id

launch(Dispatchers.IO) {
    val result : APIMessage = webService.getDrafts(deviceid, threadId, baseMsgUniqueId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceid** | **kotlin.String**| Source device ID |
 **threadId** | **kotlin.String**| Get draft for a particular thread | [optional]
 **baseMsgUniqueId** | **kotlin.String**| Get thread for a reply thread, this is base message id | [optional]

### Return type

[**APIMessage**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
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
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(DraftApi::class.java)
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val senderTimeStampMs : java.math.BigDecimal = 8.14 // java.math.BigDecimal | epoch timestamp (in ms) of message creation generated on sender device
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is mutually exclusive with recipientAppUserId.
val recipientAppUserId : kotlin.String = recipientAppUserId_example // kotlin.String | App user Id of receiver. eg. abc@def.com. This is mutually exclusive with threadId.
val message : kotlin.String = message_example // kotlin.String | message text. rg. 'hello'
val msgType : MessageType =  // MessageType | 
val file : java.io.File = BINARY_DATA_HERE // java.io.File | File share
val replyThreadFeatureData : Reply =  // Reply | 
val location : Location =  // Location | 
val contact : Contact =  // Contact | 
val gif : kotlin.String = gif_example // kotlin.String | gify url
val mentions : kotlin.collections.List<Mention> =  // kotlin.collections.List<Mention> | 
val forwardChatFeatureData : Forward =  // Forward | 
val media : Media =  // Media | 
val msgCorrelationId : kotlin.String = msgCorrelationId_example // kotlin.String | Client generated unique identifier used to trace message delivery till receiver
val encryptedChatList : kotlin.collections.List<EncryptedMessage> =  // kotlin.collections.List<EncryptedMessage> | List of user+device wise eencrypted chat objects.

launch(Dispatchers.IO) {
    val result : APIMessage = webService.updateDraft(deviceid, senderTimeStampMs, threadId, recipientAppUserId, message, msgType, file, replyThreadFeatureData, location, contact, gif, mentions, forwardChatFeatureData, media, msgCorrelationId, encryptedChatList)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceid** | **kotlin.String**| Source device ID |
 **senderTimeStampMs** | **java.math.BigDecimal**| epoch timestamp (in ms) of message creation generated on sender device |
 **threadId** | **kotlin.String**| Thread ID which represents a user or a group. eg. 5c56c9a2218aec4b4a8a976f. This is mutually exclusive with recipientAppUserId. | [optional]
 **recipientAppUserId** | **kotlin.String**| App user Id of receiver. eg. abc@def.com. This is mutually exclusive with threadId. | [optional]
 **message** | **kotlin.String**| message text. rg. &#39;hello&#39; | [optional]
 **msgType** | [**MessageType**](MessageType.md)|  | [optional] [enum: text, image, audio, video, gif, file, contact, location]
 **file** | **java.io.File**| File share | [optional]
 **replyThreadFeatureData** | [**Reply**](Reply.md)|  | [optional]
 **location** | [**Location**](Location.md)|  | [optional]
 **contact** | [**Contact**](Contact.md)|  | [optional]
 **gif** | **kotlin.String**| gify url | [optional]
 **mentions** | [**kotlin.collections.List&lt;Mention&gt;**](Mention.md)|  | [optional]
 **forwardChatFeatureData** | [**Forward**](Forward.md)|  | [optional]
 **media** | [**Media**](Media.md)|  | [optional]
 **msgCorrelationId** | **kotlin.String**| Client generated unique identifier used to trace message delivery till receiver | [optional]
 **encryptedChatList** | [**kotlin.collections.List&lt;EncryptedMessage&gt;**](EncryptedMessage.md)| List of user+device wise eencrypted chat objects. | [optional]

### Return type

[**APIMessage**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

