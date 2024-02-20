package ai.botstacks.sdk.utils


import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import co.touchlab.kermit.Logger
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.readByteArray

actual fun KmpFile.contentType(): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
}

actual fun KmpFile.size(): Long {
    return this.length()
}

actual fun KmpFile.readBytes(): ByteArray = readByteArray()

actual suspend fun KmpFile.storeTemporarily(): KmpFile = this

actual fun guessRemoteFilename(url: String): String? {
    Logger.d("guessing filename for $url")
    return URLUtil.guessFileName(url, null, null)
}