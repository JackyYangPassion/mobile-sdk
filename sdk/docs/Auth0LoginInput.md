
# Auth0LoginInput

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accessToken** | **kotlin.String** | The auth0 access token | 
**email** | **kotlin.String** | User ID based on loginType | 
**deviceId** | **kotlin.String** | Unique device id. For example, UDID for ios | 
**picture** | **kotlin.String** | A profile picture URL |  [optional]
**name** | **kotlin.String** | A display name |  [optional]
**nickname** | **kotlin.String** | A nickname |  [optional]
**deviceType** | [**inline**](#DeviceType) | Type of device i.e. android or ios. Allowed valies android/ios |  [optional]
**fcmToken** | **kotlin.String** | FCM regsitration token. Optional. |  [optional]
**apnsToken** | **kotlin.String** | the device APNS token |  [optional]


<a name="DeviceType"></a>
## Enum: deviceType
Name | Value
---- | -----
deviceType | android, ios



