# SearchApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**search**](SearchApi.md#search) | **POST** search | Unified search API



Unified search API

One stop for all search APIs, can be used to search files, messages or groups.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(SearchApi::class.java)
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID
val searchInput : SearchInput =  // SearchInput | Chat multiple request
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10

launch(Dispatchers.IO) {
    val result : SearchResults = webService.search(deviceid, searchInput, skip, limit)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceid** | **kotlin.String**| Source device ID |
 **searchInput** | [**SearchInput**](SearchInput.md)| Chat multiple request |
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]

### Return type

[**SearchResults**](SearchResults.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

