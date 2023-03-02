
# UpdateUserInput

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**deviceId** | **kotlin.String** | Unique device id. For example, UDID for ios |  [optional]
**deviceType** | [**inline**](#DeviceType) | Type of device |  [optional]
**fcmToken** | **kotlin.String** | FCM regsitration token. Optional. |  [optional]
**fcmVersion** | **kotlin.String** | FCM Version. Optional. default value is f1 |  [optional]
**availabilityStatus** | [**AvailabilityStatus**](AvailabilityStatus.md) |  |  [optional]
**notificationSettings** | [**NotificationSettings**](NotificationSettings.md) |  |  [optional]
**displayName** | **kotlin.String** | Display name for user |  [optional]
**username** | **kotlin.String** | username. min length of 5 char |  [optional]
**phoneNumber** | **kotlin.String** | e164 format phone number |  [optional]


<a name="DeviceType"></a>
## Enum: deviceType
Name | Value
---- | -----
deviceType | android, ios



