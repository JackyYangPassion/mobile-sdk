# G2Api

All URIs are relative to *https://virtserver.swaggerhub.com/RBN/Socket-Server/1.0.0*

Method | HTTP request | Description
------------- | ------------- | -------------
[**nFTLoginPOST**](G2Api.md#nFTLoginPOST) | **POST** {version}/tenants/{tenantId}/nft-login | Posion POG NFT based Login



Posion POG NFT based Login

This api verify auth0 token and add user to dtabase

### Example
```kotlin
// Import classes:
//import io.inappchat.sdk.*
//import io.inappchat.sdk.infrastructure.*
//import io.inappchat.sdk.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(G2Api::class.java)
val version : kotlin.String = version_example // kotlin.String | API versiion
val tenantId : kotlin.String = tenantId_example // kotlin.String | Tenant Id. Example 5f61c2c3fee2af1f303a16d7
val authorization : kotlin.String = authorization_example // kotlin.String | Auth0 id_token

launch(Dispatchers.IO) {
    val result : NFTLoginPOST200Response = webService.nFTLoginPOST(version, tenantId, authorization)
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **version** | **kotlin.String**| API versiion |
 **tenantId** | **kotlin.String**| Tenant Id. Example 5f61c2c3fee2af1f303a16d7 |
 **authorization** | **kotlin.String**| Auth0 id_token |

### Return type

[**NFTLoginPOST200Response**](NFTLoginPOST200Response.md)

### Authorization



### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

