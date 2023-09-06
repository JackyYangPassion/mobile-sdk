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
import io.inappchat.sdk.utils.uuid
import kotlinx.datetime.Clock

fun Chat.send(
    inReplyTo: String?,
    text: String? = null,
    attachments: List<AttachmentInput>? = null,
    upload: Upload? = null
) {
    val m = Message(
        id = uuid(),
        createdAt = Clock.System.now(),
        userID = User.current!!.id,
        parentID = inReplyTo,
        chatID = id,
        attachments = attachments?.map {
            FMessage.Attachment(
                id = it.id,
                url = it.url,
                data = it.data.getOrNull(),
                type = it.type,
                width = it.width.getOrNull(),
                height = it.height.getOrNull(),
                duration = it.duration.getOrNull(),
                latitude = it.latitude.getOrNull(),
                longitude = it.longitude.getOrNull(),
                address = it.address.getOrNull(),
                mime = it.mime.getOrNull()
            )
        }?.toMutableStateList() ?: mutableStateListOf(),
    )
    m.text = text ?: ""
    m.sending = true
    val sendingMessage = SendingMessage(m, upload)
    sending.add(0, sendingMessage)
    op({
        val sm = bg { API.send(id, inReplyTo, text, attachments) }
        sm?.let {
            items.add(0, it)
            sending.remove(sendingMessage)
        } ?: {
            m.failed = true
        }
    }) {
        m.failed = true
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