package ai.botstacks.sdk.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

expect class ImageAssetIdentifier
expect class ImageAsset

@Composable
internal expect fun ImageAssetIdentifier.toImageAsset(): ImageAsset?

@Composable
internal expect fun painterImageAsset(asset: ImageAssetIdentifier): Painter

@Composable
internal expect fun painterImageAsset(asset: ImageAsset): Painter