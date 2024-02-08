package ai.botstacks.sdk.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage


@Composable
actual fun ImageRenderer(
    modifier: Modifier,
    contentDescription: String?,
    contentScale: ContentScale,
    url: String
) {
    AsyncImage(
        model = url,
        contentDescription = "shared image",
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}