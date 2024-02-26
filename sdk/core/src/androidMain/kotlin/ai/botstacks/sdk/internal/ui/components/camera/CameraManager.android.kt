package ai.botstacks.sdk.internal.ui.components.camera

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mohamedrejeb.calf.io.KmpFile
import java.io.File

@Composable
internal actual fun rememberCameraManager(onResult: (KmpFile?) -> Unit): CameraManager {
    val context = LocalContext.current
    val file = remember {
        File(context.cacheDir, System.currentTimeMillis().toString() + ".jpg")
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult(file)
            } else {
                onResult(null)
            }
        }
    )

    return remember(cameraLauncher) {
        CameraManager(
            onLaunch = {
                val uri = ComposeFileProvider.getImageUri(context, file)
                cameraLauncher.launch(uri)
            }
        )
    }
}

internal actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}