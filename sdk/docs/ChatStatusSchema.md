
# ChatStatusSchema

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**returnCode** | **kotlin.String** | return code for e2e encrypted chat request. It can be senderKeyValidityExpired (new key to be provided in keyList, also new device key if there) / receiverKeyValidationError / senderNewDeviceKeyAvailable (new device key to be provided in keyList, also same device key if validity expired) / success |  [optional]
**retryRequired** | **kotlin.Boolean** | Boolean parameter which indicates if same chat needs to be re-sent after resolving issues based on returnCode |  [optional]
**keyList** | [**kotlin.collections.List&lt;E2eKeyObjWithReturnCodee&gt;**](E2eKeyObjWithReturnCodee.md) | list of key details based on returnCode. Details of this list depends on returnCode. |  [optional]



