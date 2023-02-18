import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp


data class Theme(
    val fonts: Fonts = Fonts(),
    val light: Colors = Colors(true),
    val dark: Colors = Colors(false),
    val imagePreviewSize: Size = Size(width = 178f, height = 153f),
    val videoPreviewSize: Size = Size(width = 248f, height = 153f),
    val messageAlignment: Alignment.Horizontal = Alignment.Start,
    val senderAlignment: Alignment.Horizontal = Alignment.End,
    val bubbleRadius: Float = 7.5f,
    val bubblePadding: PaddingValues = PaddingValues(6.dp),
    val assets: Assets = Assets()
)