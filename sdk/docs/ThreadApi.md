# ThreadApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createThread**](ThreadApi.md#createThread) | **POST** user/{uid}/thread | Thread Creation API
[**getGroupThread**](ThreadApi.md#getGroupThread) | **GET** group/{gid}/thread | 
[**getThread**](ThreadApi.md#getThread) | **GET** thread/{tid} | Thread Get API
[**getThreads**](ThreadApi.md#getThreads) | **GET** threads | Load thread history
[**updateThread**](ThreadApi.md#updateThread) | **PUT** thread/{tid} | Thread Update API



Thread Creation API

Get or Create Thread request before starting chat session with any user. This API is applicable for only one 2 one chat.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ThreadApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    val result : APIThread = webService.createThread(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

[**APIThread**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get a thread belonging to a group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ThreadApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    val result : APIThread = webService.getGroupThread(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

[**APIThread**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Thread Get API

Get any existing thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ThreadApi::class.java)
val tid : kotlin.String = tid_example // kotlin.String | The Thread ID

launch(Dispatchers.IO) {
    val result : APIThread = webService.getThread(tid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tid** | **kotlin.String**| The Thread ID |

### Return type

[**APIThread**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Load thread history

Load thread history

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ThreadApi::class.java)
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10
val threadType : kotlin.String = threadType_example // kotlin.String | threadType in-case specific type threads are needed. Don't provide this field if all threads to be returned in unified way.

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIThread> = webService.getThreads(skip, limit, threadType)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]
 **threadType** | **kotlin.String**| threadType in-case specific type threads are needed. Don&#39;t provide this field if all threads to be returned in unified way. | [optional] [enum: single, group]

### Return type

[**kotlin.collections.List&lt;APIThread&gt;**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Thread Update API

Update any existing thread

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ThreadApi::class.java)
val tid : kotlin.String = tid_example // kotlin.String | The Thread ID
val updateThreadInput : UpdateThreadInput =  // UpdateThreadInput | Thread settings

launch(Dispatchers.IO) {
    webService.updateThread(tid, updateThreadInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tid** | **kotlin.String**| The Thread ID |
 **updateThreadInput** | [**UpdateThreadInput**](UpdateThreadInput.md)| Thread settings |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

