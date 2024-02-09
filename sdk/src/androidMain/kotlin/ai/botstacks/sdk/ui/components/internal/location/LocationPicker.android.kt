package ai.botstacks.sdk.ui.components.internal.location

import ai.botstacks.sdk.state.Location
import ai.botstacks.sdk.utils.contains
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun LocationPicker(onLocation: (Location) -> Unit, onCancel: () -> Unit) {
    val state = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val have = state.allPermissionsGranted || state.permissions.contains { it.status.isGranted }
    val activity = LocalContext.current as Activity
    LaunchedEffect(have) {
        if (have) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            val cancellationToken = CancellationTokenSource()
            val loc = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationToken.token
            ).await()

            Logger.d { "$loc" }

            if (loc != null) {
                onLocation(
                    Location(latitude = loc.latitude, longitude = loc.longitude)
                )
            } else {
                onCancel()
            }
        } else {
            state.launchMultiplePermissionRequest()
        }
    }
}