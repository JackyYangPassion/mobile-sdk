# ChatSettingApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getSettings**](ChatSettingApi.md#getSettings) | **GET** settings | Get chat settings that contains profanity and domain filters



Get chat settings that contains profanity and domain filters

Get profanity and domain filter.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatSettingApi::class.java)

launch(Dispatchers.IO) {
    val result : ChatSettings = webService.getSettings()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ChatSettings**](ChatSettings.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

