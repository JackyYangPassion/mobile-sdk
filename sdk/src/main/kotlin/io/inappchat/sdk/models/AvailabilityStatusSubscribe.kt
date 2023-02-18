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


import com.squareup.moshi.Json

/**
 * 
 *
 * @param eRTCUserId eRTC user Id
 * @param appUserId User ID i.e. abc@def.com
 * @param availabilityStatus availability status of user. i.e. online/away/invisible/dnd
 */


data class AvailabilityStatusSubscribe (

    /* eRTC user Id */
    @Json(name = "eRTCUserId")
    val eRTCUserId: kotlin.String? = null,

    /* User ID i.e. abc@def.com */
    @Json(name = "appUserId")
    val appUserId: kotlin.String? = null,

    /* availability status of user. i.e. online/away/invisible/dnd */
    @Json(name = "availabilityStatus")
    val availabilityStatus: kotlin.String? = null

)
