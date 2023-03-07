
# ChatStatus

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**returnCode** | [**inline**](#ReturnCode) | return code for e2e encrypted chat request.  It can be senderKeyValidityExpired (new key to be provided in keyList, also new device key if there)  / receiverKeyValidationError / senderNewDeviceKeyAvailable (new device key to be provided in keyList,  also same device key if validity expired) / success  | 
**retryRequired** | **kotlin.Boolean** |  | 
**keyList** | [**kotlin.collections.List&lt;ChatStatusKeyListInner&gt;**](ChatStatusKeyListInner.md) |  | 


<a name="ReturnCode"></a>
## Enum: returnCode
Name | Value
---- | -----
returnCode | senderKeyValidityExpired,, receiverKeyValidationError, senderNewDeviceKeyAvailable, success



