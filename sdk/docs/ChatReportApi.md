# ChatReportApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**approveChatReport**](ChatReportApi.md#approveChatReport) | **PUT** reports/{chatReportId}/approve | 
[**createChatReport**](ChatReportApi.md#createChatReport) | **POST** message/{mid}/report | 
[**deleteChatReportDelete**](ChatReportApi.md#deleteChatReportDelete) | **DELETE** reports/{chatReportId} | Delete Chat Report
[**getChatReport**](ChatReportApi.md#getChatReport) | **GET** reports/{chatReportId} | Get Chat Report Details
[**getChatReportList**](ChatReportApi.md#getChatReportList) | **GET** reports | Get Chat Report List
[**ignoreChatReport**](ChatReportApi.md#ignoreChatReport) | **PUT** reports/{chatReportId}/ignore | 
[**updateChatReport**](ChatReportApi.md#updateChatReport) | **PUT** reports/{chatReportId} | 





Approve Chat Report Action.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID

launch(Dispatchers.IO) {
    webService.approveChatReport(chatReportId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatReportId** | **kotlin.String**| chat Report ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Create Chat Report.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val mid : kotlin.String = mid_example // kotlin.String | The message ID
val createChatReport : CreateChatReport =  // CreateChatReport | 

launch(Dispatchers.IO) {
    val result : Report = webService.createChatReport(mid, createChatReport)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **mid** | **kotlin.String**| The message ID |
 **createChatReport** | [**CreateChatReport**](CreateChatReport.md)|  |

### Return type

[**Report**](Report.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
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
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID

launch(Dispatchers.IO) {
    webService.deleteChatReportDelete(chatReportId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatReportId** | **kotlin.String**| chat Report ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Get Chat Report Details

Get Chat Report Details.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID

launch(Dispatchers.IO) {
    val result : Report = webService.getChatReport(chatReportId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatReportId** | **kotlin.String**| chat Report ID |

### Return type

[**Report**](Report.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

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
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10
val threadId : kotlin.String = threadId_example // kotlin.String | thread ID to filter chat Reports
val category : ReportCategory =  // ReportCategory | 
val status : ReportStatus =  // ReportStatus | 
val msgType : kotlin.String = msgType_example // kotlin.String | chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Report> = webService.getChatReportList(uid, skip, limit, threadId, category, status, msgType)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]
 **threadId** | **kotlin.String**| thread ID to filter chat Reports | [optional]
 **category** | [**ReportCategory**](.md)|  | [optional] [enum: abuse, spam, other, inappropriate]
 **status** | [**ReportStatus**](.md)|  | [optional] [enum: new, reportConsidered, reportIgnored]
 **msgType** | **kotlin.String**| chat report msgType to filter chat Reports(Possible values : text, image, audio, video, file, gif, location, contact, sticker, gify) | [optional]

### Return type

[**kotlin.collections.List&lt;Report&gt;**](Report.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Ignore Chat Report Action.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID

launch(Dispatchers.IO) {
    webService.ignoreChatReport(chatReportId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatReportId** | **kotlin.String**| chat Report ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Update Chat Report.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(ChatReportApi::class.java)
val chatReportId : kotlin.String = chatReportId_example // kotlin.String | chat Report ID
val createChatReport : CreateChatReport =  // CreateChatReport | 

launch(Dispatchers.IO) {
    webService.updateChatReport(chatReportId, createChatReport)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **chatReportId** | **kotlin.String**| chat Report ID |
 **createChatReport** | [**CreateChatReport**](CreateChatReport.md)|  |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

