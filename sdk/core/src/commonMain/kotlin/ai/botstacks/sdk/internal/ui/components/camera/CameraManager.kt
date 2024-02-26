package ai.botstacks.sdk.internal.ui.components.camera

import androidx.compose.runtime.Composable
import com.mohamedrejeb.calf.io.KmpFile


@Composable
internal expect fun rememberCameraManager(onResult: (KmpFile?) -> Unit): CameraManager

internal expect class CameraManager(
    onLaunch: () -> Unit
) {
    fun launch()
}