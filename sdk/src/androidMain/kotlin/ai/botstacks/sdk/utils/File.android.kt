package ai.botstacks.sdk.utils


import android.webkit.MimeTypeMap
import com.mohamedrejeb.calf.io.KmpFile

actual fun KmpFile.contentType(): String? {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
}

actual fun KmpFile.size(): Long {
    return this.length()
}