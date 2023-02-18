
# UpdateUserRequest

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**deviceId** | **kotlin.String** | Unique device id. For example, UDID for ios |  [optional]
**deviceType** | **kotlin.String** | Type of device i.e. android or ios. Allowed valies android/ios |  [optional]
**fcmToken** | **kotlin.String** | FCM regsitration token. Optional. |  [optional]
**fcmVersion** | **kotlin.String** | FCM Version. Optional. default value is f1 |  [optional]
**availabilityStatus** | **kotlin.String** | availability status to be over-riden on top of default behaviour. i.e. auto/away/invisible/dnd |  [optional]
**notificationSettings** | [**NotificationSettings**](NotificationSettings.md) |  |  [optional]



