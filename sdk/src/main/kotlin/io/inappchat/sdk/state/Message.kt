/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import io.inappchat.sdk.API
import io.inappchat.sdk.models.*
import io.inappchat.sdk.utils.localDateTime
import okhttp3.internal.immutableListOf
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@Stable
enum class AttachmentKind(val value: String) {
    image("image"),
    video("video"),
    file("file"),
    audio("audio")
}

@Stable
data class Attachment(val url: String, val kind: AttachmentKind, val type: String? = null)

@Stable
enum class Status(val value: String) {
    delivered("delivered"),
    seen("seen"),
    sent("sent"),
    sending("sending"),
    failed("failed")
}

@Stable
data class Message(
    val id: String,
    val createdAt: LocalDateTime,
    val userID: String,
    val parentID: String?,
    val threadID: String,
    var text: String?,
    var attachments: List<Attachment> = listOf(),
    var location: Location? = null,
    var contact: Contact? = null,
    var reactions: List<Reaction> = listOf(),
    var replyCount: Int = 0,
    var status: Status? = null,
    var favorite: Boolean = false,
    var currentReaction: String? = null,
) {
    constructor(msg: APIMessage) : this(
        msg.msgUniqueId,
        msg.createdAt.localDateTime(),
        msg.sendereRTCUserId,
        msg.replyThreadFeatureData?.baseMsgUniqueId,
        msg.threadId,
        msg.message ?: "",
        msg.attachment()?.let { immutableListOf(it) } ?: listOf()
    )
}

fun APIMessage.attachment(): Attachment? {
    if (msgType != null) {
        return when (msgType) {
            MessageType.audio -> Attachment(API.server + media!!.path!!, AttachmentKind.audio)
            MessageType.video -> Attachment(API.server + media!!.path!!, AttachmentKind.video)
            MessageType.image, MessageType.gif -> Attachment(
                if (msgType == MessageType.gif)
                    gify!!
                else
                    API.server + media!!.path!!,
                AttachmentKind.image
            )
            else -> null
        }
    }
    return null
}