
# UserResponse

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**eRTCUserId** | **kotlin.String** | ERTCID of User |  [optional]
**appUserId** | **kotlin.String** | User ID i.e. abc@def.com |  [optional]
**tenantId** | **kotlin.String** | Tenant ID |  [optional]
**name** | **kotlin.String** | Name of user |  [optional]
**e2eEncryptionKeys** | [**kotlin.collections.List&lt;E2eKeyObj&gt;**](E2eKeyObj.md) | List of e2e public keys of user on different devices. ONLY APPLICABLE IF E2E EENCRYPTION IS ENABLED FOR TENANT |  [optional]
**token** | [**TokenSchema**](TokenSchema.md) |  |  [optional]



