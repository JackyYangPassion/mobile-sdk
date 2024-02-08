package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFDataRef
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.create
import platform.ImageIO.CGImageSourceCreateWithData
import platform.ImageIO.CGImageSourceGetCount

@Composable
actual fun ImageRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {

    val isAnimated = isAnimated(url)
    if (isAnimated) {
        GifRenderer(
            modifier = Modifier
                .width(BotStacks.dimens.imagePreviewSize.width.dp)
                .height(BotStacks.dimens.imagePreviewSize.height.dp)
                .clip(RoundedCornerShape(15.dp))
                .then(modifier),
            contentDescription = contentDescription,
            contentScale = contentScale,
            url = url
        )
    } else {
        AsyncImage(
            model = url,
            contentDescription = "shared image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(BotStacks.dimens.imagePreviewSize.width.dp)
                .height(BotStacks.dimens.imagePreviewSize.height.dp)
                .clip(RoundedCornerShape(15.dp))
                .then(modifier)
        )
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun isAnimated(url: String): Boolean {
    runCatching {
        val data = NSData.create(URLWithString(url)!!) ?: return false

        val dataRef = CFBridgingRetain(data) as? CFDataRef ?: return false
        val source = CGImageSourceCreateWithData(dataRef, null) ?: return false

        val count = CGImageSourceGetCount(source)
        return count > 1u
    }.getOrNull() ?: return false

}