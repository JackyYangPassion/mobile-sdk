# AuthApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**auth0Login**](AuthApi.md#auth0Login) | **POST** auth/auth0/login | Verify User information
[**changePassword**](AuthApi.md#changePassword) | **POST** auth/change-password | Change Password
[**logout**](AuthApi.md#logout) | **POST** logout | Logout
[**logoutOtherDevices**](AuthApi.md#logoutOtherDevices) | **POST** logoutOtherDevices | Logout
[**nftLogin**](AuthApi.md#nftLogin) | **POST** auth/nft/login | signup and login with NFT
[**resetPassword**](AuthApi.md#resetPassword) | **POST** auth/reset-password | Forgot Password



Verify User information

verify user information, device information

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthApi::class.java)
val loginInput : LoginInput =  // LoginInput | 

launch(Dispatchers.IO) {
    val result : UserInfo = webService.auth0Login(loginInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loginInput** | [**LoginInput**](LoginInput.md)|  |

### Return type

[**UserInfo**](UserInfo.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Change Password

API to change user password

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthApi::class.java)
val loginPasswordInput : LoginPasswordInput =  // LoginPasswordInput | 

launch(Dispatchers.IO) {
    webService.changePassword(loginPasswordInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loginPasswordInput** | [**LoginPasswordInput**](LoginPasswordInput.md)|  |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


Logout

Logout

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(AuthApi::class.java)
val deviceid : kotlin.String = deviceid_example // kotlin.String | Source device ID

launch(Dispatchers.IO) {
    webService.logout(deviceid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **deviceid** | **kotlin.String**| Source device ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Logout

logoutOtherDevices

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(AuthApi::class.java)

launch(Dispatchers.IO) {
    webService.logoutOtherDevices()
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


signup and login with NFT

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthApi::class.java)
val nfTLoginInput : NFTLoginInput =  // NFTLoginInput | array of eRTCUserIds of invitees

launch(Dispatchers.IO) {
    val result : Auth = webService.nftLogin(nfTLoginInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **nfTLoginInput** | [**NFTLoginInput**](NFTLoginInput.md)| array of eRTCUserIds of invitees |

### Return type

[**Auth**](Auth.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


Forgot Password

On calling this API, password gets reset and new password gets delivered on email

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthApi::class.java)
val resetPasswordInput : ResetPasswordInput =  // ResetPasswordInput | 

launch(Dispatchers.IO) {
    webService.resetPassword(resetPasswordInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **resetPasswordInput** | [**ResetPasswordInput**](ResetPasswordInput.md)|  |

### Return type

null (empty response body)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

