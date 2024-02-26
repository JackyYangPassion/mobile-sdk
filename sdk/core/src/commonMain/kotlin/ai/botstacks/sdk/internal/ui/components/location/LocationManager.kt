package ai.botstacks.sdk.internal.ui.components.location

import ai.botstacks.sdk.internal.state.Location
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberLocationManager(onResult: (Location?) -> Unit): LocationManager


internal expect class LocationManager(
    onLaunch: suspend () -> Unit
) {
    fun launch()
}