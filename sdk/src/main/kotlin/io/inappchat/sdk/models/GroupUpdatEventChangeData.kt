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

import io.inappchat.sdk.models.GroupUpdatEventChangeDataGroupId
import io.inappchat.sdk.models.GroupUpdatEventChangeDataGroupType
import io.inappchat.sdk.models.GroupUpdatEventChangeDataName

import com.squareup.moshi.Json

/**
 * 
 *
 * @param groupId 
 * @param name 
 * @param description 
 * @param profilePic 
 * @param groupType 
 */


data class GroupUpdatEventChangeData (

    @Json(name = "groupId")
    val groupId: GroupUpdatEventChangeDataGroupId? = null,

    @Json(name = "name")
    val name: GroupUpdatEventChangeDataName? = null,

    @Json(name = "description")
    val description: GroupUpdatEventChangeDataName? = null,

    @Json(name = "profilePic")
    val profilePic: GroupUpdatEventChangeDataName? = null,

    @Json(name = "groupType")
    val groupType: GroupUpdatEventChangeDataGroupType? = null

)

