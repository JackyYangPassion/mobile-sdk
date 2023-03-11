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
class Theme {
    var fonts by mutableStateOf(Fonts())
    var light: Colors by mutableStateOf(Colors(true))
    var dark: Colors by mutableStateOf(Colors(false))
    var imagePreviewSize by mutableStateOf(Size(width = 178f, height = 153f))
    var videoPreviewSize by mutableStateOf(Size(width = 248f, height = 153f))
    var messageAlignment by mutableStateOf(Alignment.Start)
    var senderAlignment by mutableStateOf(Alignment.End)
    var bubbleRadius by mutableStateOf(7.5f)
    var bubblePadding by mutableStateOf(PaddingValues(6.dp))
    var assets by mutableStateOf(Assets())
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

    fun fromOtherTheme(it: Theme) {
        this.light = it.light
        this.dark = it.dark
        this.imagePreviewSize = it.imagePreviewSize
        this.videoPreviewSize = it.videoPreviewSize
        this.messageAlignment = it.messageAlignment
        this.senderAlignment = it.senderAlignment
        this.bubbleRadius = it.bubbleRadius
        this.bubblePadding = it.bubblePadding
        this.assets = it.assets
    }
}
