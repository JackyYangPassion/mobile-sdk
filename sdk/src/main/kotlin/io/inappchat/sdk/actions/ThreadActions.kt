/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import io.inappchat.sdk.API
import io.inappchat.sdk.models.Contact
import io.inappchat.sdk.models.Location
import io.inappchat.sdk.models.NotificationSettings
import io.inappchat.sdk.state.*
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.uuid
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.time.LocalDateTime

fun Room.send(
    inReplyTo: String?,
    text: String? = null,
    attachment: Attachment? = null,
    gif: String? = null,
    location: Location? = null,
    contact: Contact? = null,
    mediaType: MediaType = "application/octet-stream".toMediaType()
) {
    val m = Message(
        id = uuid(),
        createdAt = LocalDateTime.now(),
        userID = User.current!!.id,
        parentID = inReplyTo,
        threadID = id,
        attachment = attachment ?: gif?.let { Attachment(gif, AttachmentKind.image) },
        location = location,
        contact = contact
    )
    m.text = text ?: ""
    sending.add(0, m)
    op({
        val sm = bg { API.send(id, inReplyTo, text, attachment, gif, location, contact, mediaType) }
        items.add(0, sm)
        sending.remove(m)
    }) {
        sending.remove(m)
        failed.add(m)
    }


}

fun Room.setNotifications(settings: NotificationSettings.AllowFrom, isSync: Boolean) {
    val og = this.notification
    this.notification = settings
    if (isSync) {
        return
    }
    op({
        API.updateThreadNotifications(id, settings)
    }) {
        this.notification = og
    }
}