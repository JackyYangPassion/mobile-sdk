
# ThreadResponse

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**recipientAppUserId** | **kotlin.String** | App user Id of receiver |  [optional]
**threadId** | **kotlin.String** | Thread ID |  [optional]
**threadType** | **kotlin.String** | Type of thread - single/group |  [optional]
**tenantId** | **kotlin.String** | Tenant ID |  [optional]
**createdAt** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Therad object creation time |  [optional]
**participants** | [**kotlin.collections.List&lt;ThreadMemberSchema&gt;**](ThreadMemberSchema.md) | array of read timestamps |  [optional]
**e2eEncryptionKeys** | [**kotlin.collections.List&lt;E2eKeyObj&gt;**](E2eKeyObj.md) | List of e2e public keys of user on different devices. ONLY APPLICABLE IF E2E EENCRYPTION IS ENABLED FOR TENANT |  [optional]



