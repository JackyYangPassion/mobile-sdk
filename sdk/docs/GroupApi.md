# GroupApi

All URIs are relative to *https://chat.inappchat.io/v3*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptGroupInvite**](GroupApi.md#acceptGroupInvite) | **POST** group/{gid}/invites/accept | accept group invitation
[**addParticipant**](GroupApi.md#addParticipant) | **PUT** group/{gid}/participants/{uid} | Add participant to group
[**createGroup**](GroupApi.md#createGroup) | **POST** groups | Create or Update group
[**deleteGroup**](GroupApi.md#deleteGroup) | **DELETE** group/{gid} | 
[**dismissGroupInvite**](GroupApi.md#dismissGroupInvite) | **POST** group/{gid}/invites/dismiss | dismiss group invitation
[**getGroup**](GroupApi.md#getGroup) | **GET** group/{gid} | Get group by groupId
[**getGroups**](GroupApi.md#getGroups) | **GET** groups | Get user groups
[**getInvites**](GroupApi.md#getInvites) | **GET** group/invites | get group invitation
[**groupAddAdmin**](GroupApi.md#groupAddAdmin) | **PUT** group/{gid}/admin/{uid} | Make a user an admin
[**groupDismissAdmin**](GroupApi.md#groupDismissAdmin) | **DELETE** group/{gid}/admin/{uid} | Dismiss a group admin
[**inviteUser**](GroupApi.md#inviteUser) | **POST** group/{gid}/invite | create group invitation
[**moderateGroup**](GroupApi.md#moderateGroup) | **POST** group/{gid}/moderate | 
[**removeGroupImage**](GroupApi.md#removeGroupImage) | **DELETE** group/{gid}/image | Remove group profile pic
[**removeParticipant**](GroupApi.md#removeParticipant) | **DELETE** group/{gid}/participants/{uid} | Remove participant from group
[**updateGroup**](GroupApi.md#updateGroup) | **PUT** group/{gid} | 



accept group invitation

Accept group invitation

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    val result : APIThread = webService.acceptGroupInvite(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

[**APIThread**](APIThread.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Add participant to group

Add participant to group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    webService.addParticipant(gid, uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Create or Update group

Create a group. For profilePic use multipart/formdata and in this case stringify participants list.

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val name : kotlin.String = name_example // kotlin.String | Group Name
val participants : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | List of participants
val groupType : kotlin.String = groupType_example // kotlin.String | Type of group. for example privte/public. only private is supported as of now.
val description : kotlin.String = description_example // kotlin.String | Description of group. Optional. Min length 2.
val profilePic : java.io.File = BINARY_DATA_HERE // java.io.File | The image for the group

launch(Dispatchers.IO) {
    val result : APIGroup = webService.createGroup(name, participants, groupType, description, profilePic)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **kotlin.String**| Group Name |
 **participants** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| List of participants |
 **groupType** | **kotlin.String**| Type of group. for example privte/public. only private is supported as of now. | [optional] [enum: public, private]
 **description** | **kotlin.String**| Description of group. Optional. Min length 2. | [optional]
 **profilePic** | **java.io.File**| The image for the group | [optional]

### Return type

[**APIGroup**](APIGroup.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json




Delete a group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    webService.deleteGroup(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

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
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    webService.dismissGroupInvite(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Get group by groupId

Get group by groupId

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    val result : APIGroup = webService.getGroup(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

[**APIGroup**](APIGroup.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Get user groups

Get groups where user is participant or group is public

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val limit : kotlin.Int = 56 // kotlin.Int | limit value for pagination. i.e. page-size. default 10
val skip : kotlin.Int = 56 // kotlin.Int | skip value for pagination. i.e. index. default 0
val groupType : kotlin.String = groupType_example // kotlin.String | Filter by group type
val joined : kotlin.String = joined_example // kotlin.String | Get only joined/not joined groups

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<APIGroup> = webService.getGroups(limit, skip, groupType, joined)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **limit** | **kotlin.Int**| limit value for pagination. i.e. page-size. default 10 | [optional] [default to 20]
 **skip** | **kotlin.Int**| skip value for pagination. i.e. index. default 0 | [optional] [default to 0]
 **groupType** | **kotlin.String**| Filter by group type | [optional] [enum: public, private]
 **joined** | **kotlin.String**| Get only joined/not joined groups | [optional] [enum: yes, no]

### Return type

[**kotlin.collections.List&lt;APIGroup&gt;**](APIGroup.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


get group invitation

Get group invitations for user

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Invite> = webService.getInvites()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Invite&gt;**](Invite.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


Make a user an admin

Make a user an admin

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    webService.groupAddAdmin(uid, gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |
 **gid** | **kotlin.String**| Group ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Dismiss a group admin

Dismiss a group admin

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val uid : kotlin.String = uid_example // kotlin.String | the user's id
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    webService.groupDismissAdmin(uid, gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **uid** | **kotlin.String**| the user&#39;s id |
 **gid** | **kotlin.String**| Group ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


create group invitation

Invite new participant to group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID
val requestBody : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | array of user ids to invite

launch(Dispatchers.IO) {
    webService.inviteUser(gid, requestBody)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |
 **requestBody** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| array of user ids to invite |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined




Moderate a group. Ban or mute users

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID
val moderateGroupInput : ModerateGroupInput =  // ModerateGroupInput | Unique AppID of the user to get

launch(Dispatchers.IO) {
    webService.moderateGroup(gid, moderateGroupInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |
 **moderateGroupInput** | [**ModerateGroupInput**](ModerateGroupInput.md)| Unique AppID of the user to get |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


Remove group profile pic

Remove group profile pic

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID

launch(Dispatchers.IO) {
    webService.removeGroupImage(gid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Remove participant from group

Remove participant from group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID
val uid : kotlin.String = uid_example // kotlin.String | the user's id

launch(Dispatchers.IO) {
    webService.removeParticipant(gid, uid)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |
 **uid** | **kotlin.String**| the user&#39;s id |

### Return type

null (empty response body)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




Update a group

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
apiClient.setBearerToken("TOKEN")
val webService = apiClient.createWebservice(GroupApi::class.java)
val gid : kotlin.String = gid_example // kotlin.String | Group ID
val updateGroupInput : UpdateGroupInput =  // UpdateGroupInput | 

launch(Dispatchers.IO) {
    val result : APIGroup = webService.updateGroup(gid, updateGroupInput)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gid** | **kotlin.String**| Group ID |
 **updateGroupInput** | [**UpdateGroupInput**](UpdateGroupInput.md)|  |

### Return type

[**APIGroup**](APIGroup.md)

### Authorization


Configure BearerAuth:
    ApiClient().setBearerToken("TOKEN")

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

