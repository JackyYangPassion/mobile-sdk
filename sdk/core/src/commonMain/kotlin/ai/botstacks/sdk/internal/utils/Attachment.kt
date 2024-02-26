package ai.botstacks.sdk.internal.utils

import ai.botstacks.sdk.fragment.FMessage
import ai.botstacks.sdk.state.File
import ai.botstacks.sdk.state.Location
import ai.botstacks.sdk.type.AttachmentType

internal fun FMessage.Attachment.vcard() =
    if (type == AttachmentType.vcard) parseVcard(data) else null

internal fun FMessage.Attachment.location() =
    if (type == AttachmentType.location) Location(latitude, longitude, address) else null

internal fun FMessage.Attachment.file() =
    if (type == AttachmentType.file) File(data = data.orEmpty(), mimeString = mime.orEmpty()) else null