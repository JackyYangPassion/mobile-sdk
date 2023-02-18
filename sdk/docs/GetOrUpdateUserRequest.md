
# GetOrUpdateUserRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**appUserId** | **kotlin.String** | User ID i.e. abc@def.com. Required. version&#x3D;1:N |  [optional]
**deviceId** | **kotlin.String** | Unique device id. For example, UDID for ios. version&#x3D;1:N |  [optional]
**deviceType** | **kotlin.String** | Type of device i.e. android or ios. Allowed valies android/ios. version&#x3D;1:N |  [optional]
**fcmToken** | **kotlin.String** | FCM regsitration token. Optional. version&#x3D;1:N |  [optional]
**fcmVersion** | **kotlin.String** | FCM Version. Optional. default value is f1. version&#x3D;1:N |  [optional]
**publicKey** | **kotlin.String** | public key for end to end encryption. version&#x3D;1:N |  [optional]
**muteSetting** | **kotlin.String** | Mute setting parameter. supported values - none / all / allbutmentions. version&#x3D;1:N |  [optional]
**authPayload** | [**UserAuthPayload**](UserAuthPayload.md) |  |  [optional]



