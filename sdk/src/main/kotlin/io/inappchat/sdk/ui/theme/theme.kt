import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
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
    var isDark =
        if (preview) darkMode else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) InAppChat.appContext.resources.configuration.isNightModeActive else false

    val colors: Colors
        @Composable get() = if (isDark) dark else light

    val inverted: Colors
        @Composable get() = if (isDark) light else dark

    fun with(dark: Boolean): Theme {
        this.isDark = dark
        return this
    }

    companion object {
        val standard = Theme()
    }
}