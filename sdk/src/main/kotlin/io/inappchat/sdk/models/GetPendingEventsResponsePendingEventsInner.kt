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

import io.inappchat.sdk.models.GetPendingEventsResponsePendingEventsInnerData

import com.squareup.moshi.Json

/**
 * Pending event. Treat event entry similar to FCM event itself
 *
 * @param `data` 
 */


data class GetPendingEventsResponsePendingEventsInner (

    @Json(name = "data")
    val `data`: GetPendingEventsResponsePendingEventsInnerData? = null

)
