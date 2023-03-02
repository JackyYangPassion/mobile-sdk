
# UpdateMessageEvent

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**eRTCUserId** | **kotlin.String** | User unique Identifier | 
**msgUniqueId** | **kotlin.String** | Chat unique Identifier | 
**updateType** | [**inline**](#UpdateType) | Type of update.  | 
**deleteType** | [**inline**](#DeleteType) | in case of delete updateType, it specifies sub-type of delete such as self/everyone | 


<a name="UpdateType"></a>
## Enum: updateType
Name | Value
---- | -----
updateType | delete, edit


<a name="DeleteType"></a>
## Enum: deleteType
Name | Value
---- | -----
deleteType | self, everyone



