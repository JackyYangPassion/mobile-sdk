/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import io.inappchat.sdk.API
import io.inappchat.sdk.fragment.FMessage
import io.inappchat.sdk.state.*
import io.inappchat.sdk.type.AttachmentInput
import io.inappchat.sdk.type.NotificationSetting
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.opbg
import io.inappchat.sdk.utils.uuid
import kotlinx.datetime.Clock

fun AttachmentInput.toAttachment() = FMessage.Attachment(
    id = id,
    url = url,
    data = data.getOrNull(),
    type = type,
    width = width.getOrNull(),
    height = height.getOrNull(),
    duration = duration.getOrNull(),
    latitude = latitude.getOrNull(),
    longitude = longitude.getOrNull(),
    address = address.getOrNull(),
    mime = mime.getOrNull()
)

fun Chat.send(
    inReplyTo: String?,
    text: String? = null,
    attachments: List<AttachmentInput>? = null,
    upload: Upload? = null
) {
    val atts = (attachments?.toMutableList() ?: mutableListOf())
    if (upload != null) {
        atts.add(upload.localAttachment())
    }
    val m = Message(
        id = uuid(),
        createdAt = Clock.System.now(),
        userID = User.current!!.id,
        parentID = inReplyTo,
        chatID = id,
        attachments = atts.map { it.toAttachment() }.toMutableStateList(),
    )
    m.text = text ?: ""
    val sendingMessage = SendingMessage(m, upload = upload, attachments = attachments ?: listOf())
    send(sendingMessage)
}

fun Chat.send(sendingMessage: SendingMessage) {
    if (!sending.contains(sendingMessage)) {
        sending.add(0, sendingMessage)
    }
    print("Sending Message")
    op({
        val sm = bg {
            val attachments = sendingMessage.attachments.toMutableList()
            if (sendingMessage.upload != null) {
                print("Awaiting upload")
                val upload = sendingMessage.upload.awaitAttachment()
                println("Got Upload " + upload.url)
                attachments.add(upload)
            }
            println("Sending")
            API.send(
                id,
                id = sendingMessage.msg.id,
                inReplyTo = sendingMessage.msg.parentID,
                text = sendingMessage.msg.text,
                attachments = attachments
            )
        }
        sm?.let {
            items.add(0, it)
            sending.remove(sendingMessage)
        } ?: {
            sendingMessage.failed = true
        }
    }) {
        sendingMessage.failed = true
    }
}


fun Chat.setNotifications(settings: NotificationSetting, isSync: Boolean) {
    val og = this.notification_setting
    this.notification_setting = settings
    if (isSync) {
        return
    }
    op({
        API.updateChatNotifications(id, settings)
    }) {
        this.notification_setting = og
    }
}

fun Chat.markRead() {
    unreadCount = 0
    opbg({
        API.markChatRead(id)
    })
}