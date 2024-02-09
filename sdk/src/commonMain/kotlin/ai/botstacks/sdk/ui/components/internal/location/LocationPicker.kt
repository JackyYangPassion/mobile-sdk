package ai.botstacks.sdk.ui.components.internal.location

import ai.botstacks.sdk.state.Location
import androidx.compose.runtime.Composable

@Composable
expect fun LocationPicker(onLocation: (Location) -> Unit, onCancel: () -> Unit)