# GroupApi

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addParticipantsPost**](GroupApi.md#addParticipantsPost) | **POST** {version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/addParticipants | Add participants to group
[**createOrUpdateGroupPost**](GroupApi.md#createOrUpdateGroupPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group | Create or Update group
[**deleteGroupProfilePic**](GroupApi.md#deleteGroupProfilePic) | **DELETE** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/removeProfilePic | Remove group profile pic
[**getGroupByGroupIdGet**](GroupApi.md#getGroupByGroupIdGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId} | Get group by groupId
[**getGroupByThreadIdGet**](GroupApi.md#getGroupByThreadIdGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/group/{threadId}/groupByThreadId | Get group by threadId
[**makeDismissAdminPost**](GroupApi.md#makeDismissAdminPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/makeDismissAdmin/{action} | Make or Dismiss a group user as admin
[**removeParticipantsPost**](GroupApi.md#removeParticipantsPost) | **POST** {version}/tenants/{tenantId}/{eRTCUserId}/group/{groupId}/removeParticipants | Delete participants from group
[**restrictParticipants**](GroupApi.md#restrictParticipants) | **POST** {version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/restrictParticipants | Ban or mute users
[**unrestrictParticipants**](GroupApi.md#unrestrictParticipants) | **POST** {version}/tenants}/{tenantId}/{eRTCUserId}/group/{groupId}/unrestrictParticipants | UnBan or unmute users
[**userGroupsFilter**](GroupApi.md#userGroupsFilter) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/group | Get user groups
[**userGroupsGet**](GroupApi.md#userGroupsGet) | **GET** {version}/tenants/{tenantId}/{eRTCUserId}/group/userGroups | Get user groups



Add participants to group

Add participants to group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : AddRemoveParticipantsRequest =  // AddRemoveParticipantsRequest | 

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.addParticipantsPost(authorization, xRequestSignature, version, tenantId, eRTCUserId, groupId, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**AddRemoveParticipantsRequest**](AddRemoveParticipantsRequest.md)|  | [optional]

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Create or Update group

Create of Update group. For profilePic use multipart/formdata and in this case stringify participants list.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : GroupRequest =  // GroupRequest | 

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.createOrUpdateGroupPost(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**GroupRequest**](GroupRequest.md)|  | [optional]

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Remove group profile pic

Remove group profile pic

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    val result : DeleteGroupProfilePic200Response = webService.deleteGroupProfilePic(authorization, xRequestSignature, xNonce, version, tenantId, eRTCUserId, groupId)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |

### Return type

[**DeleteGroupProfilePic200Response**](DeleteGroupProfilePic200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get group by groupId

Get group by groupId

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.getGroupByGroupIdGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, groupId, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get group by threadId

Get group by threadId

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val threadId : kotlin.String = threadId_example // kotlin.String | Thread ID received in chat object
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.getGroupByThreadIdGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, threadId, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **threadId** | **kotlin.String**| Thread ID received in chat object |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Make or Dismiss a group user as admin

Make or Dismiss a group user as admin

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val action : kotlin.String = action_example // kotlin.String | Action: make/dimiss
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : MakeDismissAdminPostRequest =  // MakeDismissAdminPostRequest | 

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.makeDismissAdminPost(authorization, xRequestSignature, version, tenantId, eRTCUserId, groupId, action, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **action** | **kotlin.String**| Action: make/dimiss |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**MakeDismissAdminPostRequest**](MakeDismissAdminPostRequest.md)|  | [optional]

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Delete participants from group

Delete participants from group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val body : AddRemoveParticipantsRequest =  // AddRemoveParticipantsRequest | 

launch(Dispatchers.IO) {
    val result : CreateOrUpdateGroupPost200Response = webService.removeParticipantsPost(authorization, xRequestSignature, version, tenantId, eRTCUserId, groupId, xNonce, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **body** | [**AddRemoveParticipantsRequest**](AddRemoveParticipantsRequest.md)|  | [optional]

### Return type

[**CreateOrUpdateGroupPost200Response**](CreateOrUpdateGroupPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Ban or mute users

Ban or mute users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API key
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val body : RestrictParticipantsRequest =  // RestrictParticipantsRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.restrictParticipants(version, tenantId, eRTCUserId, groupId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API key |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **body** | [**RestrictParticipantsRequest**](RestrictParticipantsRequest.md)| Unique AppID of the user to get |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


UnBan or unmute users

UnBan or unmute users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val version : kotlin.String = version_example // kotlin.String | API key
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val groupId : kotlin.String = groupId_example // kotlin.String | Group ID
val body : UnrestrictParticipantsRequest =  // UnrestrictParticipantsRequest | Unique AppID of the user to get

launch(Dispatchers.IO) {
    val result : Thread200Response = webService.unrestrictParticipants(version, tenantId, eRTCUserId, groupId, body)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API key |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **groupId** | **kotlin.String**| Group ID |
 **body** | [**UnrestrictParticipantsRequest**](UnrestrictParticipantsRequest.md)| Unique AppID of the user to get |

### Return type

[**Thread200Response**](Thread200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get user groups

Filter All groups where user is participant or group is public

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp
val keyword : kotlin.String = keyword_example // kotlin.String | Get only public or private group
val groupType : kotlin.String = groupType_example // kotlin.String | Filter by group type
val joined : kotlin.String = joined_example // kotlin.String | Get only joined/not joined groups

launch(Dispatchers.IO) {
    val result : UserGroupsFilter200Response = webService.userGroupsFilter(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce, keyword, groupType, joined)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |
 **keyword** | **kotlin.String**| Get only public or private group | [optional]
 **groupType** | **kotlin.String**| Filter by group type | [optional] [enum: public, private]
 **joined** | **kotlin.String**| Get only joined/not joined groups | [optional] [enum: yes, no]

### Return type

[**UserGroupsFilter200Response**](UserGroupsFilter200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get user groups

Get All groups where user is participant

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(GroupApi::class.java)
val authorization : kotlin.String = authorization_example // kotlin.String | Authorization Token
val xRequestSignature : kotlin.String = xRequestSignature_example // kotlin.String | sha256 of <userServer apiKey>~<bundleId>~<epoch timeStamp>
val version : kotlin.String = version_example // kotlin.String | API version
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val eRTCUserId : kotlin.String = eRTCUserId_example // kotlin.String | eRTC user ID
val xNonce : kotlin.String = xNonce_example // kotlin.String | epoch timestamp

launch(Dispatchers.IO) {
    val result : UserGroupsFilter200Response = webService.userGroupsGet(authorization, xRequestSignature, version, tenantId, eRTCUserId, xNonce)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **authorization** | **kotlin.String**| Authorization Token |
 **xRequestSignature** | **kotlin.String**| sha256 of &lt;userServer apiKey&gt;~&lt;bundleId&gt;~&lt;epoch timeStamp&gt; |
 **version** | **kotlin.String**| API version |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **eRTCUserId** | **kotlin.String**| eRTC user ID |
 **xNonce** | **kotlin.String**| epoch timestamp |

### Return type

[**UserGroupsFilter200Response**](UserGroupsFilter200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

