# UserApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**blockUser**](UserApi.md#blockUser) | **PUT** blocks/{uid} | Block a user
[**deleteUserAvatar**](UserApi.md#deleteUserAvatar) | **DELETE** users/{uid}/avatar | 
[**getBlockedUsers**](UserApi.md#getBlockedUsers) | **GET** blocks | Get blocked users
[**getPendingEvents**](UserApi.md#getPendingEvents) | **GET** events | 
[**getUser**](UserApi.md#getUser) | **GET** user/{uid} | 
[**getUsers**](UserApi.md#getUsers) | **GET** users | 
[**me**](UserApi.md#me) | **GET** me | 
[**refreshAuthToken**](UserApi.md#refreshAuthToken) | **GET** token/refresh | Refresh auth token
[**resetBadgeCount**](UserApi.md#resetBadgeCount) | **GET** resetBadgeCount | reset notification badge count
[**syncContacts**](UserApi.md#syncContacts) | **POST** contacts/sync | Sync Contacts
[**unblockUser**](UserApi.md#unblockUser) | **DELETE** blocks/{uid} | Unblock a user
[**updateMe**](UserApi.md#updateMe) | **POST** me | 



Block a user

Block a user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    webService.blockUser(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Remove user profile pic

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    webService.deleteUserAvatar(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Get blocked users

Get blocked users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIUser> = webService.getBlockedUsers()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;APIUser&gt;**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get pending events for particular device

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Event> = webService.getPendingEvents()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Event&gt;**](Event.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get a user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    val result : APIUser = webService.getUser(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

[**APIUser**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




List users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val lastId : kotlin.String = lastId_example // kotlin.String | To be used for Pagination
val lastCallTime : kotlin.Int = 56 // kotlin.Int | epoch time value for time based sunc. Do not pass this param itself for retrieving all data.
val updateType : kotlin.String = updateType_example // kotlin.String | type of sync i.e. addUpdated/inactive/deleted. Default value is addUpdated

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIUser> = webService.getUsers(lastId, lastCallTime, updateType)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **lastId** | **kotlin.String**| To be used for Pagination | [optional]
 **lastCallTime** | **kotlin.Int**| epoch time value for time based sunc. Do not pass this param itself for retrieving all data. | [optional]
 **updateType** | **kotlin.String**| type of sync i.e. addUpdated/inactive/deleted. Default value is addUpdated | [optional]

### Return type

[**kotlin.collections.List&lt;APIUser&gt;**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json




Get current user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)

launch(Dispatchers.IO) {
    val result : APIUser = webService.me()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**APIUser**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Refresh auth token

Refresh auth token

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    val result : Token = webService.refreshAuthToken(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

[**Token**](Token.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


reset notification badge count

reset badge count

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)

launch(Dispatchers.IO) {
    webService.resetBadgeCount()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Sync Contacts

Sync contacts

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val syncContactsInput : SyncContactsInput =  // SyncContactsInput | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIUser> = webService.syncContacts(syncContactsInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **syncContactsInput** | [**SyncContactsInput**](SyncContactsInput.md)|  | [optional]

### Return type

[**kotlin.collections.List&lt;APIUser&gt;**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Unblock a user

Unblock a user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    webService.unblockUser(uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Update current user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(UserApi::class.java)
val updateUserInput : UpdateUserInput =  // UpdateUserInput | User properties to update with

launch(Dispatchers.IO) {
    val result : APIUser = webService.updateMe(updateUserInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **updateUserInput** | [**UpdateUserInput**](UpdateUserInput.md)| User properties to update with |

### Return type

[**APIUser**](APIUser.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

