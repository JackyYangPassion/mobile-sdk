# FCMApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**fCMValidationPost**](FCMApi.md#fCMValidationPost) | **POST** {version}/tenants/{tenantId}/fcmValidation | FCM Validation



FCM Validation

Endpoint to just validate FCM notification by App teams

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FCMApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API key
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val body : FcmValidationRequest =  // FcmValidationRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.fCMValidationPost(version, tenantId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API key |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **body** | [**FcmValidationRequest**](FcmValidationRequest.md)| Unique AppID of the user to get |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

