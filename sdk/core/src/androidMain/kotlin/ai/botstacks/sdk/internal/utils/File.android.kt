package ai.botstacks.sdk.internal.utils


import android.webkit.MimeTypeMap
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.readByteArray

internal actual fun KmpFile.contentType(): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
}

internal actual fun KmpFile.size(): Long {
    return this.length()
}

internal actual fun KmpFile.readBytes(): ByteArray = readByteArray()