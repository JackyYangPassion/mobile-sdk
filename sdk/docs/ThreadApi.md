# ThreadApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**thread**](ThreadApi.md#thread) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/thread/ | Thread Creation API
[**threadGet**](ThreadApi.md#threadGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/thread/{threadId} | Thread Get API
[**threadHistoryGet**](ThreadApi.md#threadHistoryGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/thread/history | Load thread history
[**threadHistoryGetV2**](ThreadApi.md#threadHistoryGetV2) | **GET** V2/tenants/{tenantId}/{eRTCUserId}/thread/history | Load thread history
[**threadUpdatePost**](ThreadApi.md#threadUpdatePost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/thread/{threadId} | Thread Update API



Thread Creation API

Get or Create Thread request before starting chat session with any user. This API is applicable for only one 2 one chat.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ThreadApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : ThreadRequest =  // ThreadRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.thread(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, body)
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
 **body** | [**ThreadRequest**](ThreadRequest.md)| Unique AppID of the user to get |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization



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
val webService = apiClient.createWebservice(ThreadApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.threadGet(version, tenantId, eRTCUserId, threadId, authorization, xRequestSignature, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **threadId** | **kotlin.String**| Thread ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization



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
val webService = apiClient.createWebservice(ThreadApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val skip : kotlin.String = skip_example // kotlin.String | skip value for pagination. i.e. index. default 0
val limit : kotlin.String = limit_example // kotlin.String | limit value for pagination. i.e. page-size. default 10
val threadType : kotlin.String = threadType_example // kotlin.String | threadType in-case specific type threads are needed. supported values single/group. Don't provide this field if all threads to be returned in unified way.

launch(Dispatchers.IO) {
    val result : ThreadHistoryGet200Response = webService.threadHistoryGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, skip, limit, threadType)
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
 **skip** | **kotlin.String**| skip value for pagination. i.e. index. default 0 | [optional]
 **limit** | **kotlin.String**| limit value for pagination. i.e. page-size. default 10 | [optional]
 **threadType** | **kotlin.String**| threadType in-case specific type threads are needed. supported values single/group. Don&#39;t provide this field if all threads to be returned in unified way. | [optional]

### Return type

[**ThreadHistoryGet200Response**](ThreadHistoryGet200Response.md)

### Authorization

No authorization required

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
val webService = apiClient.createWebservice(ThreadApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val skip : kotlin.String = skip_example // kotlin.String | skip value for pagination. i.e. index. default 0
val limit : kotlin.String = limit_example // kotlin.String | limit value for pagination. i.e. page-size. default 10
val threadType : kotlin.String = threadType_example // kotlin.String | threadType in-case specific type threads are needed. supported values single/group. Don't provide this field if all threads to be returned in unified way.

launch(Dispatchers.IO) {
    val result : ThreadHistoryGetV2200Response = webService.threadHistoryGetV2(authorization, xRequestSignature, tenantId, eRTCUserId, xNonce, skip, limit, threadType)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **skip** | **kotlin.String**| skip value for pagination. i.e. index. default 0 | [optional]
 **limit** | **kotlin.String**| limit value for pagination. i.e. page-size. default 10 | [optional]
 **threadType** | **kotlin.String**| threadType in-case specific type threads are needed. supported values single/group. Don&#39;t provide this field if all threads to be returned in unified way. | [optional]

### Return type

[**ThreadHistoryGetV2200Response**](ThreadHistoryGetV2200Response.md)

### Authorization

No authorization required

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
val webService = apiClient.createWebservice(ThreadApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : ThreadUpdateRequest =  // ThreadUpdateRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.threadUpdatePost(version, tenantId, eRTCUserId, threadId, authorization, xRequestSignature, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **threadId** | **kotlin.String**| Thread ID |
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**ThreadUpdateRequest**](ThreadUpdateRequest.md)| Unique AppID of the user to get |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

