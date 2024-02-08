package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.utils.ui.contentDescription
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import cocoapods.Gifu.GIFImageView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIImage
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
internal fun GifRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {

    val gifView: GIFImageView? = remember(url) {
        val data = NSData.create(NSURL.URLWithString(url)!!) ?: return@remember null
        val image = UIImage.imageWithData(data) ?: return@remember null
        GIFImageView(image).apply {
            val contentMode = when (contentScale) {
                ContentScale.None -> null
                ContentScale.Fit -> UIViewContentMode.UIViewContentModeScaleAspectFit
                ContentScale.Crop -> UIViewContentMode.UIViewContentModeCenter
                ContentScale.FillBounds -> UIViewContentMode.UIViewContentModeScaleToFill
                else -> null
            }
            if (contentMode != null) {
                this.contentMode = contentMode
            }
        }
    }

    UIKitView(
        modifier = modifier.contentDescription(contentDescription),
        factory = {
            val container = UIView().apply {
                if (gifView != null) {
                    addSubview(gifView)
                }
            }

            container
        },
        update = {
            gifView?.startAnimating()
        }
    )
}