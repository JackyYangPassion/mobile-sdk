# GroupInvteApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**versionTenantsTenantIdERTCUserIdGroupGroupIdInvitePost**](GroupInvteApi.md#versionTenantsTenantIdERTCUserIdGroupGroupIdInvitePost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invite | create group invitation
[**versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesAcceptPost**](GroupInvteApi.md#versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesAcceptPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invites/accept | accept group invitation
[**versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesDismissPost**](GroupInvteApi.md#versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesDismissPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/invites/dismiss | dismiss group invitation
[**versionTenantsTenantIdERTCUserIdGroupInvitesGet**](GroupInvteApi.md#versionTenantsTenantIdERTCUserIdGroupInvitesGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/group/invites | get group invitation



create group invitation

Invite new participant to group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupInvteApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant ID
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | user mongo ID
val groupId : kotlin.String = groupId_example // kotlin.String | group ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | array of eRTCUserIds of invitees

launch(Dispatchers.IO) {
    webService.versionTenantsTenantIdERTCUserIdGroupGroupIdInvitePost(version, tenantId, eRTCUserId, groupId, xRequestSignature, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant ID |
 **eRTCUserId** | **kotlin.String**| user mongo ID |
 **groupId** | **kotlin.String**| group ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| array of eRTCUserIds of invitees |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


accept group invitation

Accept group invitation

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupInvteApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant ID
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | user mongo ID
val groupId : kotlin.String = groupId_example // kotlin.String | group ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    webService.versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesAcceptPost(version, tenantId, eRTCUserId, groupId, xRequestSignature, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant ID |
 **eRTCUserId** | **kotlin.String**| user mongo ID |
 **groupId** | **kotlin.String**| group ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


dismiss group invitation

Dissmiss group invitation

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupInvteApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant ID
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | user mongo ID
val groupId : kotlin.String = groupId_example // kotlin.String | group ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    webService.versionTenantsTenantIdERTCUserIdGroupGroupIdInvitesDismissPost(version, tenantId, eRTCUserId, groupId, xRequestSignature, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant ID |
 **eRTCUserId** | **kotlin.String**| user mongo ID |
 **groupId** | **kotlin.String**| group ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


get group invitation

Get group invitations for user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupInvteApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant ID
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | user mongo ID
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <chatServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response = webService.versionTenantsTenantIdERTCUserIdGroupInvitesGet(version, tenantId, eRTCUserId, xRequestSignature, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant ID |
 **eRTCUserId** | **kotlin.String**| user mongo ID |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;chatServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response**](VersionTenantsTenantIdERTCUserIdGroupInvitesGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

