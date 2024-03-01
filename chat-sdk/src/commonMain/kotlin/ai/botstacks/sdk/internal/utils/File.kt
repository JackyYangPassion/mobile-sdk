package ai.botstacks.sdk.internal.utils

import com.mohamedrejeb.calf.io.KmpFile

internal expect fun KmpFile.contentType(): String?
internal expect fun KmpFile.size(): Long

internal expect fun KmpFile.readBytes(): ByteArray
