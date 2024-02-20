package ai.botstacks.sdk.ui.components.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import platform.CoreFoundation.CFDataRef
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.create
import platform.ImageIO.CGImageSourceCreateWithData
import platform.ImageIO.CGImageSourceGetCount

@Composable
internal actual fun ImageRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {

    val isAnimated = isAnimated(url)
    if (isAnimated) {
        GifRenderer(
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            url = url
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = "shared image",
            contentScale = contentScale,
            modifier = modifier
        )
    }
}

private fun isAnimated(url: String): Boolean {
    runCatching {
        val data = NSData.create(URLWithString(url)!!) ?: return false

        val dataRef = CFBridgingRetain(data) as? CFDataRef ?: return false
        val source = CGImageSourceCreateWithData(dataRef, null) ?: return false

        val count = CGImageSourceGetCount(source)
        return count > 1u
    }.getOrNull() ?: return false

}