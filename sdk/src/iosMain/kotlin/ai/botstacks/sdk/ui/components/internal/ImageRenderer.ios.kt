package ai.botstacks.sdk.ui.components.internal

import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.CoreFoundation.CFDataCreate
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreImage.provideImageData
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSURL
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.NSURLRequest
import platform.Foundation.NSURLSession
import platform.Foundation.create
import platform.Foundation.dataTaskWithRequest
import platform.ImageIO.CGImageSourceCreateWithData
import platform.ImageIO.CGImageSourceGetCount
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import kotlin.coroutines.resume

@Composable
internal actual fun ImageRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {

    var isAnimated by remember { mutableStateOf<Boolean?>(null) }
    when (isAnimated) {
        true -> GifRenderer(
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            url = url
        )
        false -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier.heightIn(min = BotStacks.dimens.imagePreviewSize.height.dp)
            )
        }
        else -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    LaunchedEffect(url) {
        isAnimated = isAnimated(url).also {
            println("$url is animated=$it")
        }
    }
}

private fun UIImageView.loadImageFromUrl(url: String) {
    val request = NSURLRequest.requestWithURL(NSURL(string = url))
    NSURLSession.sharedSession.dataTaskWithRequest(
        request = request,
        completionHandler = { data, response, error ->
            if (error == null && response != null && data != null) {
                if ((response as NSHTTPURLResponse).statusCode == 200L) {
                    CoroutineScope(Dispatchers.Main).launch {
                        image = UIImage(data = data)
                    }
                }
            }
        }
    ).resume()

}

private suspend fun isAnimated(url: String): Boolean = suspendCancellableCoroutine { cont ->
    runCatching {
        val request = NSURLRequest.requestWithURL(NSURL(string = url),)
        NSURLSession.sharedSession.dataTaskWithRequest(
            request = request,
            completionHandler = { data, response, error ->
                if (error == null && response != null && data != null) {
                    if ((response as NSHTTPURLResponse).statusCode == 200L) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val dataRef = CFBridgingRetain(data) as? CFDataRef
                            val source = CGImageSourceCreateWithData(dataRef, null)
                            val count = CGImageSourceGetCount(source)
                            cont.resume(count > 1u)
                            return@launch
                        }
                    }
                }
            }
        ).resume()
    }.onFailure { cont.resume(false) }

}