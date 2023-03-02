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
 * @param allowFrom Allow from values. It can be all/mentions/none. When it is include in thread response, can have value just for requesting used.
 * @param validTill When setting should reset
 * @param validTillDisplayValue To help frontend preselect value
 */


data class NotificationSettings (

    /* Allow from values. It can be all/mentions/none. When it is include in thread response, can have value just for requesting used. */
    @Json(name = "allowFrom")
    val allowFrom: NotificationSettings.AllowFrom,

    /* When setting should reset */
    @Json(name = "validTill")
    val validTill: kotlin.String? = null,

    /* To help frontend preselect value */
    @Json(name = "validTillDisplayValue")
    val validTillDisplayValue: kotlin.String? = null

) {

    /**
     * Allow from values. It can be all/mentions/none. When it is include in thread response, can have value just for requesting used.
     *
     * Values: all,mentions,none
     */
    enum class AllowFrom(val value: kotlin.String) {
        @Json(name = "all") all("all"),
        @Json(name = "mentions") mentions("mentions"),
        @Json(name = "none") none("none");
    }
}

