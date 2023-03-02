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
 * the groups id
 *
 * @param new new value
 * @param previous Previous value. Applicable only for nameChanged
 */


data class GroupUpdatEventChangeDataGroupId (

    /* new value */
    @Json(name = "new")
    val new: kotlin.String,

    /* Previous value. Applicable only for nameChanged */
    @Json(name = "previous")
    val previous: kotlin.String? = null

)

