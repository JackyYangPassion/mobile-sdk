
# LoginInput

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**loginType** | [**inline**](#LoginType) | Type of login  like email/mobile/sso | 
**appUserId** | **kotlin.String** | User ID based on loginType | 
**deviceId** | **kotlin.String** | Unique device id. For example, UDID for ios | 
**deviceType** | [**inline**](#DeviceType) | Type of device i.e. android or ios. Allowed valies android/ios |  [optional]
**fcmToken** | **kotlin.String** | FCM regsitration token. Optional. |  [optional]


<a name="LoginType"></a>
## Enum: loginType
Name | Value
---- | -----
loginType | email, mobile, sso


<a name="DeviceType"></a>
## Enum: deviceType
Name | Value
---- | -----
deviceType | android, ios



