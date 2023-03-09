# ChatApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteChatHistory**](ChatApi.md#deleteChatHistory) | **DELETE** thread/{tid}/messages | Load chat history
[**deleteMessage**](ChatApi.md#deleteMessage) | **DELETE** message/{mid} | Delete Message API
[**getFavorites**](ChatApi.md#getFavorites) | **GET** favorites | 
[**getMessage**](ChatApi.md#getMessage) | **GET** message/{mid} | 
[**getMessages**](ChatApi.md#getMessages) | **GET** thread/{tid}/messages | Load chat history
[**getReplies**](ChatApi.md#getReplies) | **GET** message/{mid}/replies | 
[**getReplyThreads**](ChatApi.md#getReplyThreads) | **GET** reply-threads | List reply threads
[**react**](ChatApi.md#react) | **PUT** message/{mid}/reactions/{emoji} | Chat Reaction API
[**sendMessage**](ChatApi.md#sendMessage) | **POST** message | Send a chat message
[**unreact**](ChatApi.md#unreact) | **DELETE** message/{mid}/reactions/{emoji} | 
[**updateMessage**](ChatApi.md#updateMessage) | **PUT** message/{mid} | Edit Message API



Load chat history

Clear chat history

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val tid : kotlin.String = tid_example // kotlin.String | The Thread ID

launch(Dispatchers.IO) {
    webService.deleteChatHistory(tid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tid** | **kotlin.String**| The Thread ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Delete Message API

Delete particular message

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID

launch(Dispatchers.IO) {
    webService.deleteMessage(mid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Get users favorite messages

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIMessage> = webService.getFavorites(skip, limit)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]

### Return type

[**kotlin.collections.List&lt;APIMessage&gt;**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get a single message

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID

launch(Dispatchers.IO) {
    val result : APIMessage = webService.getMessage(mid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |

### Return type

[**APIMessage**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Load chat history

List messages in any chat

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val tid : kotlin.String = tid_example // kotlin.String | The Thread ID
val msgType : kotlin.String = msgType_example // kotlin.String | Msg type, stringified array Example [\"text\".\"gif\"]
val currentMsgId : kotlin.String = currentMsgId_example // kotlin.String | THe message ID to paginate after or before
val direction : kotlin.String = direction_example // kotlin.String | future/past
val dateFrom : kotlin.String = dateFrom_example // kotlin.String | ISO string of start date
val dateTo : kotlin.String = dateTo_example // kotlin.String | ISO string of end date
val pageSize : kotlin.Int = 56 // kotlin.Int | page size for pagination
val inReplyTo : kotlin.String = inReplyTo_example // kotlin.String | The ID of the message to list replies for
val deep : kotlin.Boolean = true // kotlin.Boolean | When true it returns messages from threads and main window both

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIMessage> = webService.getMessages(tid, msgType, currentMsgId, direction, dateFrom, dateTo, pageSize, inReplyTo, deep)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tid** | **kotlin.String**| The Thread ID |
 **msgType** | **kotlin.String**| Msg type, stringified array Example [\&quot;text\&quot;.\&quot;gif\&quot;] | [optional]
 **currentMsgId** | **kotlin.String**| THe message ID to paginate after or before | [optional]
 **direction** | **kotlin.String**| future/past | [optional] [default to past] [enum: future, past]
 **dateFrom** | **kotlin.String**| ISO string of start date | [optional]
 **dateTo** | **kotlin.String**| ISO string of end date | [optional]
 **pageSize** | **kotlin.Int**| page size for pagination | [optional]
 **inReplyTo** | **kotlin.String**| The ID of the message to list replies for | [optional]
 **deep** | **kotlin.Boolean**| When true it returns messages from threads and main window both | [optional]

### Return type

[**kotlin.collections.List&lt;APIMessage&gt;**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get replies to a message

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIMessage> = webService.getReplies(mid, skip, limit)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]

### Return type

[**kotlin.collections.List&lt;APIMessage&gt;**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


List reply threads

List messages with reply threads

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val follow : kotlin.Boolean = true // kotlin.Boolean | To get all threads user following, just send true
val starred : kotlin.Boolean = true // kotlin.Boolean | To get all starred messages, just send true
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val direction : kotlin.String = direction_example // kotlin.String | future/past
val deep : kotlin.Boolean = true // kotlin.Boolean | When true it returns messages from threads and main window both

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIMessage> = webService.getReplyThreads(threadId, follow, starred, limit, skip, direction, deep)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **threadId** | **kotlin.String**| Thread ID | [optional]
 **follow** | **kotlin.Boolean**| To get all threads user following, just send true | [optional]
 **starred** | **kotlin.Boolean**| To get all starred messages, just send true | [optional]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **direction** | **kotlin.String**| future/past | [optional] [default to past] [enum: future, past]
 **deep** | **kotlin.Boolean**| When true it returns messages from threads and main window both | [optional] [default to true]

### Return type

[**kotlin.collections.List&lt;APIMessage&gt;**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Chat Reaction API

Send message reaction

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID
val emoji : kotlin.String = emoji_example // kotlin.String | The emoji to react with

launch(Dispatchers.IO) {
    webService.react(mid, emoji)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |
 **emoji** | **kotlin.String**| The emoji to react with |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Send a chat message

Send a chat message

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
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
    val result : MessageResponse = webService.sendMessage(senderTimeStampMs, threadId, recipientAppUserId, message, msgType, file, replyThreadFeatureData, location, contact, gif, mentions, forwardChatFeatureData, media, msgCorrelationId, encryptedChatList)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
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

[**MessageResponse**](MessageResponse.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json




Remove a message reaction

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID
val emoji : kotlin.String = emoji_example // kotlin.String | The emoji to react with

launch(Dispatchers.IO) {
    webService.unreact(mid, emoji)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |
 **emoji** | **kotlin.String**| The emoji to react with |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Edit Message API

Edit particular message

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID
val updateMessageInput : UpdateMessageInput =  // UpdateMessageInput | edit chat body

launch(Dispatchers.IO) {
    webService.updateMessage(mid, updateMessageInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |
 **updateMessageInput** | [**UpdateMessageInput**](UpdateMessageInput.md)| edit chat body |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

