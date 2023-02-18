# SearchApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**unifiedSearch**](SearchApi.md#unifiedSearch) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/search | Unified search API



Unified search API

One stop for all search APIs, can be used to search files, messages or groups.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SearchApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val body : UnifiedSearchRequest =  // UnifiedSearchRequest | Chat multiple request
val skip : kotlin.String = skip_example // kotlin.String | skip value for pagination. i.e. index. default 0
val limit : kotlin.String = limit_example // kotlin.String | limit value for pagination. i.e. page-size. default 10

launch(Dispatchers.IO) {
    val result : UnifiedSearch200Response = webService.unifiedSearch(version, tenantId, eRTCUserId, authorization, xRequestSignature, xNonce, deviceid, body, skip, limit)
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
 **body** | [**UnifiedSearchRequest**](UnifiedSearchRequest.md)| Chat multiple request |
 **skip** | **kotlin.String**| skip value for pagination. i.e. index. default 0 | [optional]
 **limit** | **kotlin.String**| limit value for pagination. i.e. page-size. default 10 | [optional]

### Return type

[**UnifiedSearch200Response**](UnifiedSearch200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

