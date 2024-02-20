@file:Suppress("CAST_NEVER_SUCCEEDS")

package ai.botstacks.sdk.utils

import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.name
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.yield
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreGraphics.CGRectMake
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSURLFileSizeKey
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.pathExtension
import platform.Foundation.writeToURL
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImageOrientation
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

actual suspend fun KmpFile.storeTemporarily(): KmpFile {
    return UIImage.imageWithData(this.readData())
        ?.let { image ->
            if (image.imageOrientation == UIImageOrientation.UIImageOrientationUp) {
                image
            } else {
                UIGraphicsBeginImageContextWithOptions(
                    image.size,
                    false,
                    image.scale
                )
                val rect = CGRectMake(
                    x = 0.0,
                    y = 0.0,
                    width = image.size.useContents { this.width },
                    height = image.size.useContents { this.height }
                )
                image.drawInRect(rect)
                val normalisedImage = UIGraphicsGetImageFromCurrentImageContext()
                UIGraphicsEndImageContext()
                normalisedImage
            }
        }
        ?.let { image ->
            // Use NSFileManager to write the image data into a file in the cache directory. We can as well use the
            // temp directory here.
            NSFileManager.defaultManager.URLForDirectory(
                NSCachesDirectory,
                NSUserDomainMask,
                null,
                true,
                null
            )?.let {
                val fileName = "temp_image"
                val url = NSURL.fileURLWithPath("$fileName.jpg", it)
                val jpeg = UIImageJPEGRepresentation(image, 0.5)
                val hasWritten = jpeg?.writeToURL(url, true)
                if (hasWritten == true) {
                    url
                } else {
                    this
                }
            }
        } ?: this
}

internal inline fun <T> cfRetain(value: Any?, block: MemScope.(CFTypeRef?) -> T): T = memScoped {
    val cfValue = CFBridgingRetain(value)
    return try {
        block(cfValue)
    } finally {
        CFBridgingRelease(cfValue)
    }
}