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

import io.inappchat.sdk.models.APIMessage
import io.inappchat.sdk.models.APIUser
import io.inappchat.sdk.models.AvailabilityEvent
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.ChatReportEvent
import io.inappchat.sdk.models.ChatReportEventEvent
import io.inappchat.sdk.models.GroupUpdateEvent
import io.inappchat.sdk.models.GroupUpdateEventItem
import io.inappchat.sdk.models.MsgReadEvent
import io.inappchat.sdk.models.NewMessageEvent
import io.inappchat.sdk.models.ReactionEvent
import io.inappchat.sdk.models.TenantUpdateEvent
import io.inappchat.sdk.models.TypingEvent
import io.inappchat.sdk.models.UpdateMessageEvent
import io.inappchat.sdk.models.UserSelfUpdateEvent

import com.squareup.moshi.Json

/**
 * 
 *
 * @param eRTCUserId The user ID
 * @param eventList 
 * @param eventTriggeredByUser 
 * @param groupId The group receiving the typing status
 * @param threadId Thread ID of associated group
 * @param msgUniqueId The ID of the message
 * @param emojiCode Emoje code string
 * @param action Reaction actionType. It can be set/clear
 * @param totalCount Total count of particular reaction with emojiCode
 * @param updateType Type of update. 
 * @param deleteType in case of delete updateType, it specifies sub-type of delete such as self/everyone
 * @param typingStatusEvent Whether or not the user is typing
 * @param msgReadStatus The status of the message
 * @param availabilityStatus 
 * @param message 
 * @param tenantId 
 * @param chatReportId 
 * @param event 
 * @param appUserId The user receiving the typing status
 */


data class EventMessage (

    /* The user ID */
    @Json(name = "eRTCUserId")
    val eRTCUserId: kotlin.String,

    @Json(name = "eventList")
    val eventList: kotlin.collections.List<GroupUpdateEventItem>,

    @Json(name = "eventTriggeredByUser")
    val eventTriggeredByUser: APIUser,

    /* The group receiving the typing status */
    @Json(name = "groupId")
    val groupId: kotlin.String,

    /* Thread ID of associated group */
    @Json(name = "threadId")
    val threadId: kotlin.String,

    /* The ID of the message */
    @Json(name = "msgUniqueId")
    val msgUniqueId: kotlin.String,

    /* Emoje code string */
    @Json(name = "emojiCode")
    val emojiCode: kotlin.String,

    /* Reaction actionType. It can be set/clear */
    @Json(name = "action")
    val action: kotlin.String,

    /* Total count of particular reaction with emojiCode */
    @Json(name = "totalCount")
    val totalCount: java.math.BigDecimal,

    /* Type of update.  */
    @Json(name = "updateType")
    val updateType: EventMessage.UpdateType,

    /* in case of delete updateType, it specifies sub-type of delete such as self/everyone */
    @Json(name = "deleteType")
    val deleteType: EventMessage.DeleteType,

    /* Whether or not the user is typing */
    @Json(name = "typingStatusEvent")
    val typingStatusEvent: EventMessage.TypingStatusEvent,

    /* The status of the message */
    @Json(name = "msgReadStatus")
    val msgReadStatus: EventMessage.MsgReadStatus,

    @Json(name = "availabilityStatus")
    val availabilityStatus: AvailabilityStatus,

    @Json(name = "message")
    val message: APIMessage,

    @Json(name = "tenantId")
    val tenantId: kotlin.String,

    @Json(name = "chatReportId")
    val chatReportId: kotlin.String? = null,

    @Json(name = "event")
    val event: ChatReportEventEvent? = null,

    /* The user receiving the typing status */
    @Json(name = "appUserId")
    val appUserId: kotlin.String? = null

) {

    /**
     * Type of update. 
     *
     * Values: delete,edit
     */
    enum class UpdateType(val value: kotlin.String) {
        @Json(name = "delete") delete("delete"),
        @Json(name = "edit") edit("edit");
    }
    /**
     * in case of delete updateType, it specifies sub-type of delete such as self/everyone
     *
     * Values: self,everyone
     */
    enum class DeleteType(val value: kotlin.String) {
        @Json(name = "self") self("self"),
        @Json(name = "everyone") everyone("everyone");
    }
    /**
     * Whether or not the user is typing
     *
     * Values: on,off
     */
    enum class TypingStatusEvent(val value: kotlin.String) {
        @Json(name = "on") on("on"),
        @Json(name = "off") off("off");
    }
    /**
     * The status of the message
     *
     * Values: delivered,seen
     */
    enum class MsgReadStatus(val value: kotlin.String) {
        @Json(name = "delivered") delivered("delivered"),
        @Json(name = "seen") seen("seen");
    }
}
