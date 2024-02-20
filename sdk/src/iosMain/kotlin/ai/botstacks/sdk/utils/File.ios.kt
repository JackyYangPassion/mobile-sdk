@file:Suppress("CAST_NEVER_SUCCEEDS")

package ai.botstacks.sdk.utils

import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.name
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.yield
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.pathExtension
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual fun KmpFile.contentType(): String? {
    val pathExtension = this.pathExtension().orEmpty()
    cfRetain(pathExtension) {
        val x = CFBridgingRelease(it) as? CFStringRef
        val uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, x, null)
        if (uti != null) {
            val mimetypeRef = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)
            val result =  CFBridgingRelease(mimetypeRef) as? NSString ?: return null
            return result as String
        }
        return "application/octet-stream"
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun KmpFile.size(): Long {
    return runCatching {
        val resources = resourceValuesForKeys(listOf(NSURLFileSizeKey), null)
        resources?.get(NSURLFileSizeKey) as? Long
    }.getOrNull() ?: -1L
}

private fun NSURL.readData(): NSData {
    var result: NSData? = null
    while (result == null) {
        val data = NSData.dataWithContentsOfURL(this)
        if (data != null)
            result = data
    }

    return result
}

actual fun KmpFile.readBytes(): ByteArray =
    with(readData()) {
        ByteArray(length.toInt()).apply {
            usePinned {
                memcpy(it.addressOf(0), bytes, length)
            }
        }
    }

actual fun guessRemoteFilename(url: String): String? {
    return NSURL.URLWithString(url)?.name
}

internal inline fun <T> cfRetain(value: Any?, block: MemScope.(CFTypeRef?) -> T): T = memScoped {
    val cfValue = CFBridgingRetain(value)
    return try {
        block(cfValue)
    } finally {
        CFBridgingRelease(cfValue)
    }
}