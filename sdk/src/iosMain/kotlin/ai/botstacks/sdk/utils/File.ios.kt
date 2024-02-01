package ai.botstacks.sdk.utils

import com.mohamedrejeb.calf.io.KmpFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFStringRef
import platform.CoreServices.UTTypeCopyPreferredTagWithClass
import platform.CoreServices.UTTypeCreatePreferredIdentifierForTag
import platform.CoreServices.kUTTagClassFilenameExtension
import platform.CoreServices.kUTTagClassMIMEType
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
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