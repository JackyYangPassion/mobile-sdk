/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package io.inappchat.sdk.models

import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.NotificationSettings

import com.squareup.moshi.Json

/**
 * 
 *
 * @param fcmToken FCM regsitration token. Optional.
 * @param fcmVersion FCM Version. Optional. default value is f1
 * @param apnsToken The APN push token
 * @param availabilityStatus 
 * @param notificationSettings 
 * @param displayName Display name for user
 * @param username username. min length of 5 char
 * @param phoneNumber e164 format phone number
 */


data class UpdateUserInput (

    /* FCM regsitration token. Optional. */
    @Json(name = "fcmToken")
    val fcmToken: kotlin.String? = null,

    /* FCM Version. Optional. default value is f1 */
    @Json(name = "fcmVersion")
    val fcmVersion: kotlin.String? = null,

    /* The APN push token */
    @Json(name = "apnsToken")
    val apnsToken: kotlin.String? = null,

    @Json(name = "availabilityStatus")
    val availabilityStatus: AvailabilityStatus? = null,

    @Json(name = "notificationSettings")
    val notificationSettings: NotificationSettings? = null,

    /* Display name for user */
    @Json(name = "displayName")
    val displayName: kotlin.String? = null,

    /* username. min length of 5 char */
    @Json(name = "username")
    val username: kotlin.String? = null,

    /* e164 format phone number */
    @Json(name = "phoneNumber")
    val phoneNumber: kotlin.String? = null

)

