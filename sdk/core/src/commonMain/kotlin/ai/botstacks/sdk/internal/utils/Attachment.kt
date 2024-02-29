package ai.botstacks.sdk.internal.utils

import ai.botstacks.sdk.internal.state.Location
import ai.botstacks.sdk.state.AttachmentType
import ai.botstacks.sdk.state.MessageAttachment

internal fun MessageAttachment.vcard() = null
//    if (type == AttachmentType.vcard) parseVcard(data) else null

internal fun MessageAttachment.location() =
    if (type == AttachmentType.Location) Location(latitude, longitude, address) else null

internal fun MessageAttachment.file() = null
//    if (type == AttachmentType.file) File(data = data.orEmpty(), mimeString = mime.orEmpty()) else null