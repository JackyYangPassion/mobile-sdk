package ai.botstacks.sdk.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

class Dimens {
    var imagePreviewSize by mutableStateOf(Size(width = 178f, height = 152f))
    var videoPreviewSize by mutableStateOf(Size(width = 178f, height = 152f))
    var messageAlignment by mutableStateOf(Alignment.Start)
    var senderAlignment by mutableStateOf(Alignment.End)
    var bubbleRadius by mutableFloatStateOf(7.5f)
    var bubblePadding by mutableStateOf(PaddingValues(6.dp))
}

internal val LocalBotStacksDimens = staticCompositionLocalOf { Dimens() }
