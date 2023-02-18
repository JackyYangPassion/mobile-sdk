# ERTCUserApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**blockUnblockUserPost**](ERTCUserApi.md#blockUnblockUserPost) | **POST** {version}/tenants/{tenantId}/user/{eRTCUserId}/blockUnblock/{action} | Update user by eRTC userId
[**blockedUsersGet**](ERTCUserApi.md#blockedUsersGet) | **GET** {version}/tenants/{tenantId}/user/{eRTCUserId}/blockedUsers | Get blocked users
[**getChatUserDetailsPost**](ERTCUserApi.md#getChatUserDetailsPost) | **POST** {version}/tenants/{tenantId}/user/{eRTCUserId}/chatUserDetails | Get specific details of other chatUsers
[**getOrUpdateUserByAppId**](ERTCUserApi.md#getOrUpdateUserByAppId) | **POST** {version}/tenants/{tenantId}/user/ | Get or Update user by appUserId
[**logoutOtherDevices**](ERTCUserApi.md#logoutOtherDevices) | **POST** {version}/tenants/{tenantId}/user/{eRTCUserId}/logoutOtherDevices | Logout
[**logoutUser**](ERTCUserApi.md#logoutUser) | **POST** {version}/tenants/{tenantId}/user/{eRTCUserId}/logout | Logout
[**pendingEventsGet**](ERTCUserApi.md#pendingEventsGet) | **GET** {version}/tenants/{tenantId}/user/{eRTCUserId}/pendingEvents | Get pending events for particular device
[**refreshAuthToken**](ERTCUserApi.md#refreshAuthToken) | **GET** {version}/tenants/{tenantId}/user/{eRTCUserId}/refreshToken | Refresh auth token
[**resetBadgeCount**](ERTCUserApi.md#resetBadgeCount) | **GET** {version}/tenants/{tenantId}/user/{eRTCUserId}/resetBadgeCount | reset notification badge count
[**updateUserByeRTCUserId**](ERTCUserApi.md#updateUserByeRTCUserId) | **POST** {version}/tenants/{tenantId}/user/{eRTCUserId} | Update user by eRTC userId



Update user by eRTC userId

Get a user by eRTC userId

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenanat Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID of caller. Example '5cbdc711c25983101b1b4198'.
val action : kotlin.String = action_example // kotlin.String | Action. For example block/unblock.
val body : BlockUnblockUserRequest =  // BlockUnblockUserRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : BlockUnblockUserPost200Response = webService.blockUnblockUserPost(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, action, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenanat Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID of caller. Example &#39;5cbdc711c25983101b1b4198&#39;. |
 **action** | **kotlin.String**| Action. For example block/unblock. |
 **body** | [**BlockUnblockUserRequest**](BlockUnblockUserRequest.md)| Unique AppID of the user to get |

### Return type

[**BlockUnblockUserPost200Response**](BlockUnblockUserPost200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get blocked users

Get blocked users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val deviceid : kotlin.String = deviceid_example // kotlin.String | device123
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'

launch(Dispatchers.IO) {
    val result : BlockedUsersGet200Response = webService.blockedUsersGet(authorization, xRequestSignature, xNonce, deviceid, version, tenantId, eRTCUserId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **deviceid** | **kotlin.String**| device123 |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |

### Return type

[**BlockedUsersGet200Response**](BlockedUsersGet200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get specific details of other chatUsers

Get specific details of other chatUsers

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'
val body : ChatUserDetailsRequest =  // ChatUserDetailsRequest | list of appUserIds of chatUsers

launch(Dispatchers.IO) {
    val result : GetChatUserDetailsPost200Response = webService.getChatUserDetailsPost(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |
 **body** | [**ChatUserDetailsRequest**](ChatUserDetailsRequest.md)| list of appUserIds of chatUsers |

### Return type

[**GetChatUserDetailsPost200Response**](GetChatUserDetailsPost200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get or Update user by appUserId

Get a user by APP Unique ID

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : GetOrUpdateUserRequest =  // GetOrUpdateUserRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : GetOrUpdateUserByAppId200Response = webService.getOrUpdateUserByAppId(version, tenantId, xRequestSignature, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**GetOrUpdateUserRequest**](GetOrUpdateUserRequest.md)| Unique AppID of the user to get |

### Return type

[**GetOrUpdateUserByAppId200Response**](GetOrUpdateUserByAppId200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Logout

logoutOtherDevices

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'
val body : LogoutUserRequest =  // LogoutUserRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : LogoutOtherDevices200Response = webService.logoutOtherDevices(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |
 **body** | [**LogoutUserRequest**](LogoutUserRequest.md)| Unique AppID of the user to get |

### Return type

[**LogoutOtherDevices200Response**](LogoutOtherDevices200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Logout

LogoutUser

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'
val body : LogoutUserRequest =  // LogoutUserRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : LogoutUser200Response = webService.logoutUser(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |
 **body** | [**LogoutUserRequest**](LogoutUserRequest.md)| Unique AppID of the user to get |

### Return type

[**LogoutUser200Response**](LogoutUser200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get pending events for particular device

Get blocked users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'
val deviceId : kotlin.String = deviceId_example // kotlin.String | source device Id

launch(Dispatchers.IO) {
    val result : PendingEventsGet200Response = webService.pendingEventsGet(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, deviceId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |
 **deviceId** | **kotlin.String**| source device Id | [optional]

### Return type

[**PendingEventsGet200Response**](PendingEventsGet200Response.md)

### Authorization



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
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'

launch(Dispatchers.IO) {
    val result : RefreshAuthToken200Response = webService.refreshAuthToken(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |

### Return type

[**RefreshAuthToken200Response**](RefreshAuthToken200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


reset notification badge count

reset Bagde count

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'

launch(Dispatchers.IO) {
    val result : ResetBadgeCount200Response = webService.resetBadgeCount(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |

### Return type

[**ResetBadgeCount200Response**](ResetBadgeCount200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Update user by eRTC userId

Get a user by eRTC userId

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ERTCUserApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID. Example '5cbdc711c25983101b1b4198'
val body : UpdateUserRequest =  // UpdateUserRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : GetOrUpdateUserByAppId200Response = webService.updateUserByeRTCUserId(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID. Example &#39;5cbdc711c25983101b1b4198&#39; |
 **body** | [**UpdateUserRequest**](UpdateUserRequest.md)| Unique AppID of the user to get |

### Return type

[**GetOrUpdateUserByAppId200Response**](GetOrUpdateUserByAppId200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

