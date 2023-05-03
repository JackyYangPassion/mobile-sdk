# AuthApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**changePassword**](AuthApi.md#changePassword) | **POST** auth/change-password | Change Password
[**login**](AuthApi.md#login) | **POST** auth/login | Login to InAppChat as a user on your Application.
[**logout**](AuthApi.md#logout) | **POST** logout | Logout
[**logoutOtherDevices**](AuthApi.md#logoutOtherDevices) | **POST** logoutOtherDevices | Logout
[**nftLogin**](AuthApi.md#nftLogin) | **POST** auth/nft/login | signup and login with NFT
[**resetPassword**](AuthApi.md#resetPassword) | **POST** auth/reset-password | Forgot Password



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


Login to InAppChat as a user on your Application.

Login to InAppChat as a user on your Application. This will updated the User&#39;s profile info in InAppChat. InAppChat will verify the authenticity of the credentials provided by calling the configured backend function of your server.

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
    val result : Auth = webService.login(loginInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **loginInput** | [**LoginInput**](LoginInput.md)|  |

### Return type

[**Auth**](Auth.md)

### Authorization



### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


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

launch(Dispatchers.IO) {
    webService.logout()
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

