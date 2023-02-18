# ChatSettingApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getChatSettings**](ChatSettingApi.md#getChatSettings) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/chatSettings | Get chat settings that contains profanity and domain filters



Get chat settings that contains profanity and domain filters

Get profanity and domain filter.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatSettingApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 60a4fc8103a6f047ca02a1df
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : GetChatSettings200Response = webService.getChatSettings(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 60a4fc8103a6f047ca02a1df |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**GetChatSettings200Response**](GetChatSettings200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

