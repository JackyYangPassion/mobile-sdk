package ai.botstacks.sdk.ui.components.internal

import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.utils.launch
import ai.botstacks.sdk.utils.ui.contentDescription
import ai.botstacks.sdk.utils.ui.gifImageWithURL
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.useContents
import platform.UIKit.UIImage
import platform.UIKit.UIImageView

@Composable
internal fun GifRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {
    val gifView = remember(url) {
        UIImageView().apply {
            launch {
                image = UIImage.gifImageWithURL(url)
            }
        }
    }

    Box(
        modifier = modifier
            .contentDescription(contentDescription),
    ) {
        val min = BotStacks.dimens.imagePreviewSize.height.dp
        var height by remember { mutableStateOf(min) }
        UIKitView(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            factory = { gifView },
            onResize = { view, size ->
                size.useContents {
                    val h = this.size.height.dp
                    if (h > height) {
                        height = h
                    }
                }
                view.layer.setFrame(size)
            },
            update = { it.startAnimating() }
        )
    }
}
