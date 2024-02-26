package ai.botstacks.sdk.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

/**
 * Image asset identifier. This is the drawable resource ID on Android.
 */
actual typealias ImageAssetIdentifier = Int

/**
 * Image asset type. [ImageVector] for Android
 */
actual typealias ImageAsset = ImageVector


@Composable
internal actual fun painterImageAsset(asset: ImageAssetIdentifier): Painter {
    return painterResource(id = asset)
}

@Composable
internal actual fun painterImageAsset(asset: ImageAsset): Painter {
    return rememberVectorPainter(image = asset)
}

@Composable
internal actual fun ImageAssetIdentifier.toImageAsset(): ImageAsset? {
    return ImageVector.vectorResource(id = this)
}

