package ai.botstacks.sdk.internal.ui.components.location

import ai.botstacks.sdk.state.Location
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal expect fun MapPin(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    location: Location,
    userAvatar: String?,
)