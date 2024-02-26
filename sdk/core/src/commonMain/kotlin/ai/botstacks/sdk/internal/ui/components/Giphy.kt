package ai.botstacks.sdk.internal.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun GiphyModalSheet(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSelection: (String) -> Unit
)