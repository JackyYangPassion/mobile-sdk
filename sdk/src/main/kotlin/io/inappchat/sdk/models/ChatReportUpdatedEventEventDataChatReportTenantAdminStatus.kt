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
 * @param status 
 * @param createdAt 
 */


data class ChatReportUpdatedEventEventDataChatReportTenantAdminStatus (

    @Json(name = "status")
    val status: kotlin.String,

    @Json(name = "createdAt")
    val createdAt: java.math.BigDecimal

)
