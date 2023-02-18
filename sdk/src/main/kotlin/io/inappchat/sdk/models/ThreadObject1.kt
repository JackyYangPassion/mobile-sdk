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

import io.inappchat.sdk.models.GroupHighLevelDetails
import io.inappchat.sdk.models.ThreadMemberInHistorySchema
import io.inappchat.sdk.models.UserInThreadHistorySchema

import com.squareup.moshi.Json

/**
 * 
 *
 * @param threadId Thread ID
 * @param threadType Type of thread - single/group
 * @param tenantId Tenant ID
 * @param createdAt Therad object creation time
 * @param participants array of read timestamps
 * @param user 
 * @param group 
 */


data class ThreadObject1 (

    /* Thread ID */
    @Json(name = "threadId")
    val threadId: kotlin.String? = null,

    /* Type of thread - single/group */
    @Json(name = "threadType")
    val threadType: kotlin.String? = null,

    /* Tenant ID */
    @Json(name = "tenantId")
    val tenantId: kotlin.String? = null,

    /* Therad object creation time */
    @Json(name = "createdAt")
    val createdAt: java.math.BigDecimal? = null,

    /* array of read timestamps */
    @Json(name = "participants")
    val participants: kotlin.collections.List<ThreadMemberInHistorySchema>? = null,

    @Json(name = "user")
    val user: UserInThreadHistorySchema? = null,

    @Json(name = "group")
    val group: GroupHighLevelDetails? = null

)
