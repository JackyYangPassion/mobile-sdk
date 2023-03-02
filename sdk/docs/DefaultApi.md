# DefaultApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getUserMessages**](DefaultApi.md#getUserMessages) | **GET** user/{uid}/messages | 
[**stub**](DefaultApi.md#stub) | **GET** stub | 





### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(DefaultApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10
val msgType : MessageType =  // MessageType | Filters message by
val direction : kotlin.String = direction_example // kotlin.String | future/past

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIMessage> = webService.getUserMessages(uid, skip, limit, msgType, direction)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]
 **msgType** | [**MessageType**](.md)| Filters message by | [optional] [enum: text, image, audio, video, gif, file, contact, location]
 **direction** | **kotlin.String**| future/past | [optional] [default to past] [enum: future, past]

### Return type

[**kotlin.collections.List&lt;APIMessage&gt;**](APIMessage.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




This api does not exist. This is only to generate types that wouldnt have otherwise been generated

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(DefaultApi::class.java)

launch(Dispatchers.IO) {
    val result : Stub = webService.stub()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Stub**](Stub.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

