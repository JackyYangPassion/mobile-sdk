import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.ui.theme.Assets


val preview = true
val darkMode = false

@Stable
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
) {
    var isDark by mutableStateOf(false)

    @Stable
    val colors: Colors
        get() = if (isDark) dark else light

    @Stable
    val inverted: Colors
        get() = if (isDark) light else dark

    @Stable
    fun with(dark: Boolean): Theme {
        this.isDark = dark
        return this
    }

    @Stable
    companion object {
        val standard = Theme()
        var current by mutableStateOf(standard)
    }
}