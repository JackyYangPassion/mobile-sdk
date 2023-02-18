# ChatReportApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createChatReportPost**](ChatReportApi.md#createChatReportPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports | Create Chat Report
[**deleteChatReportDelete**](ChatReportApi.md#deleteChatReportDelete) | **DELETE** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId} | Delete Chat Report
[**getChatReportGet**](ChatReportApi.md#getChatReportGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId} | Get Chat Report Details
[**getChatReportList**](ChatReportApi.md#getChatReportList) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports | Get Chat Report List
[**updateChatReportActionPut**](ChatReportApi.md#updateChatReportActionPut) | **PUT** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId}/allowIgnore/{action} | Update Chat Report Action
[**updateChatReportPut**](ChatReportApi.md#updateChatReportPut) | **PUT** {version}/tenants/{tenantId}/{eRTCUserId}/chatReports/{chatReportId} | Update Chat Report



Create Chat Report

Create Chat Report.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : ChatReportCreateRequest =  // ChatReportCreateRequest | 

launch(Dispatchers.IO) {
    val result : CreateChatReportPost200Response = webService.createChatReportPost(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, body)
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
 **body** | [**ChatReportCreateRequest**](ChatReportCreateRequest.md)|  | [optional]

### Return type

[**CreateChatReportPost200Response**](CreateChatReportPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Delete Chat Report

Delete Chat Report.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : DeleteChatReportDelete200Response = webService.deleteChatReportDelete(authorization, xRequestSignature, version, tenantId, eRTCUserId, chatReportId, xNonce)
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
 **chatReportId** | **kotlin.String**| chat Report ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**DeleteChatReportDelete200Response**](DeleteChatReportDelete200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get Chat Report Details

Get Chat Report Details.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : GetChatReportGet200Response = webService.getChatReportGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, chatReportId, xNonce)
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
 **chatReportId** | **kotlin.String**| chat Report ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**GetChatReportGet200Response**](GetChatReportGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get Chat Report List

Get Chat Report List.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val skip : kotlin.String = skip_example // kotlin.String | skip value for pagination. i.e. index. default 0
val limit : kotlin.String = limit_example // kotlin.String | limit value for pagination. i.e. page-size. default 10
val threadId : kotlin.String = threadId_example // kotlin.String | thread ID to filter chat Reports
val category : kotlin.String = category_example // kotlin.String | chat report category to filter chat Reports(Possible values : abuse,spam,other,inappropriate)
val status : kotlin.String = status_example // kotlin.String | chat report status to filter chat Reports(Possible values : new, reportConsidered, reportIgnored)
val msgType : kotlin.String = msgType_example // kotlin.String | chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify)

launch(Dispatchers.IO) {
    val result : GetChatReportList200Response = webService.getChatReportList(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, skip, limit, threadId, category, status, msgType)
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
 **threadId** | **kotlin.String**| thread ID to filter chat Reports | [optional]
 **category** | **kotlin.String**| chat report category to filter chat Reports(Possible values : abuse,spam,other,inappropriate) | [optional]
 **status** | **kotlin.String**| chat report status to filter chat Reports(Possible values : new, reportConsidered, reportIgnored) | [optional]
 **msgType** | **kotlin.String**| chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify) | [optional]

### Return type

[**GetChatReportList200Response**](GetChatReportList200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update Chat Report Action

Update Chat Report Action.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID
val action : kotlin.String = action_example // kotlin.String | action for report (possible values : reportConsidered,reportedIgnored)
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : UpdateChatReportActionPut200Response = webService.updateChatReportActionPut(authorization, xRequestSignature, version, tenantId, eRTCUserId, chatReportId, action, xNonce)
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
 **chatReportId** | **kotlin.String**| chat Report ID |
 **action** | **kotlin.String**| action for report (possible values : reportConsidered,reportedIgnored) |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**UpdateChatReportActionPut200Response**](UpdateChatReportActionPut200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update Chat Report

Update Chat Report.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : ChatReportUpdateRequest =  // ChatReportUpdateRequest | 

launch(Dispatchers.IO) {
    val result : CreateChatReportPost200Response = webService.updateChatReportPut(authorization, xRequestSignature, version, tenantId, eRTCUserId, chatReportId, xNonce, body)
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
 **chatReportId** | **kotlin.String**| chat Report ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**ChatReportUpdateRequest**](ChatReportUpdateRequest.md)|  | [optional]

### Return type

[**CreateChatReportPost200Response**](CreateChatReportPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

