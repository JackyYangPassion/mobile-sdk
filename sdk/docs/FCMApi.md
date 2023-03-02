# FCMApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**fCMValidationPost**](FCMApi.md#fCMValidationPost) | **POST** fcmValidation | FCM Validation



FCM Validation

Endpoint to just validate FCM notification by App teams

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(FCMApi::class.java)
val fcMValidationInput : FCMValidationInput =  // FCMValidationInput | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : APIThread = webService.fCMValidationPost(fcMValidationInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fcMValidationInput** | [**FCMValidationInput**](FCMValidationInput.md)| Unique AppID of the user to get |

### Return type

[**APIThread**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

