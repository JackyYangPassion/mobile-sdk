package ai.botstacks.sdk.utils

import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.name
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFStringRef
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSURL
import platform.Foundation.NSURL.Companion.resourceValuesForKeys
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.pathExtension

@OptIn(ExperimentalForeignApi::class)
actual fun KmpFile.contentType(): String? {
    val pathExtension = this.pathExtension().orEmpty()
    val stringPtr = CFBridgingRetain(pathExtension)
    val x = CFBridgingRelease(stringPtr) as CFStringRef
    val uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, x, null)
    if (uti != null) {
        val mimetypeRef = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)
        val mimeType = CFBridgingRelease(mimetypeRef) as String

        return mimeType
    }
    return "application/octet-stream"
}

@OptIn(ExperimentalForeignApi::class)
actual fun KmpFile.size(): Long {
    return runCatching {
        val resources = resourceValuesForKeys(listOf(NSURLFileSizeKey), null)
        resources?.get(NSURLFileSizeKey) as? Long
    }.getOrNull() ?: -1L
}

actual fun guessRemoteFilename(url: String): String? {
    return NSURL.URLWithString(url)?.name
}