/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.inappchat.sdk.API
import io.inappchat.sdk.models.*
import io.inappchat.sdk.utils.*
import okhttp3.internal.immutableListOf
import java.net.URLEncoder
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
data class Message(
    val id: String,
    val createdAt: LocalDateTime,
    val userID: String,
    val parentID: String?,
    val threadID: String,
    val attachment: Attachment? = null,
    val reactions: SnapshotStateList<Reaction> = mutableStateListOf(),
    val location: Location? = null,
    val contact: Contact? = null,
) {
    var text by mutableStateOf("")
    var markdown by mutableStateOf("")
    var replyCount by mutableStateOf(0)
    var status by mutableStateOf<MessageStatus?>(null)
    var favorite by mutableStateOf(false)
    var currentReaction by mutableStateOf<String?>(null)
    var parent by mutableStateOf<Message?>(null)

    val replies by lazy { RepliesPager(this) }
    val user = User.fetched(userID)

    constructor(msg: APIMessage) : this(
        msg.msgUniqueId,
        msg.createdAt.localDateTime(),
        msg.sendereRTCUserId,
        msg.replyThreadFeatureData?.baseMsgUniqueId,
        msg.threadId,
        msg.attachment(),
        msg.reactions?.toMutableStateList() ?: mutableStateListOf<Reaction>(),
        msg.location,
        msg.contact
    ) {
        update(msg)
    }

    init {
        Chats.current.cache.messages[id] = this
        parentID?.let { parentId ->
            Message.get(parentId)?.let { parent = it } ?: op({
                parent = bg { API.getMessage(parentId) }
            })
        }
    }

    fun update(msg: APIMessage) {
        if (this.text != (msg.message ?: "")) {
            this.text = msg.message ?: ""
            this.markdown = linkLinks(linkPhones(linkMentions(this.text)))
        }
        this.replyCount = msg.replyMsgCount ?: 0
        this.status = msg.status
        this.favorite = msg.isStarred ?: this.favorite
        msg.reactions?.let {
            this.reactions.removeAll { true }
            this.reactions.addAll(it)
        }
        this.currentReaction =
            msg.reactions?.find { it.users.contains(User.current!!.id) }?.emojiCode
    }

    val msg: String
        get() {
            attachment?.let {
                when (it.kind) {
                    AttachmentKind.image -> "[Image] ${it.url}"
                    AttachmentKind.video -> "[Video] ${it.url}"
                    AttachmentKind.audio -> "[Audio] ${it.url}"
                    AttachmentKind.file -> "[File] ${it.url}"
                }
            } ?: location?.let {
                "[Location] ${location.address ?: "${location.latitude ?: 0.0},${location.longitude ?: 0.0}"}"
            } ?: contact?.let {
                return "[Contact] ${contact.name}"
            }
            return text
        }

    val summary: String
        get() =
            "${user.username ?: ""}: $msg"


    var reacting by mutableStateOf(false)
    var favoriting by mutableStateOf(false)
    var editingText by mutableStateOf(false)

    companion object {
        fun get(id: String): Message? {
            return Chats.current.cache.messages[id]
        }

        fun get(apiMessage: APIMessage): Message {
            val m = get(apiMessage.msgUniqueId)
            if (m != null) {
                m.update(apiMessage)
                return m
            }
            return Message(apiMessage)
        }
    }
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

fun Contact.markdown(): String = "$name\n" +
        (numbers?.map {
            "[${
                if (it.type.orEmpty().isEmpty()) "" else "${it.type}: "
            }${it.number}](tel:${it.number})\n"
        } ?: "") + (emails?.map {
    "[${
        if (it.type.orEmpty().isEmpty()) "" else "${it.type}: "
    }${it.email}](tel:${it.email})\n"
} ?: "")

fun Location.link() =
    "https://www.google.com/maps/search/?api=1&query=" + URLEncoder.encode(
        address ?: "${latitude!!},${longitude!!}", "utf-8"
    )

fun Location.markdown() = "[Location${address?.let { ": $it" } ?: ""}](${link()})"