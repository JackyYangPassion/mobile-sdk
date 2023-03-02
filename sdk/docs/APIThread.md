
# APIThread

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**threadId** | **kotlin.String** | Thread ID | 
**threadType** | [**inline**](#ThreadType) | Type of thread - single/group | 
**createdAt** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Therad object creation time | 
**participants** | [**kotlin.collections.List&lt;APIUser&gt;**](APIUser.md) | array of read timestamps | 
**user** | [**APIUser**](APIUser.md) |  |  [optional]
**group** | [**APIGroup**](APIGroup.md) |  |  [optional]
**lastMessage** | [**APIMessage**](APIMessage.md) |  |  [optional]
**e2eEncryptionKeys** | [**kotlin.collections.List&lt;EncryptionKey&gt;**](EncryptionKey.md) |  |  [optional]


<a name="ThreadType"></a>
## Enum: threadType
Name | Value
---- | -----
threadType | string, group



