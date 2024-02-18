package ai.botstacks.sdk.ui.components.internal

import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import co.touchlab.kermit.Logger
import com.yazantarifi.compose.library.MarkdownConfig
import com.yazantarifi.compose.library.MarkdownViewComposable

@Composable
actual fun MarkdownView(modifier: Modifier, content: String, isCurrentUser: Boolean) {
    val openUrl = LocalUriHandler.current
    MarkdownViewComposable(
        modifier = modifier,
        content = content,
        config = markdownConfig(isCurrentUser),
        onLinkClickListener = { link, type ->
            when (type) {
                MarkdownConfig.IMAGE_TYPE -> {} // Image Clicked
                MarkdownConfig.LINK_TYPE -> {
                    openUrl.openUri(link)
                } // Link Clicked
            }
        }
    )
}

@Stable
@Composable
fun markdownConfig(sender: Boolean) = MarkdownConfig(
    isLinksClickable = true,
    isImagesClickable = false,
    isScrollEnabled = false,
    colors = HashMap<String, Color>().apply {
        this[MarkdownConfig.CHECKBOX_COLOR] = Color.Black
        this[MarkdownConfig.LINKS_COLOR] = if (sender) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.primary
        this[MarkdownConfig.TEXT_COLOR] = if (sender) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.onMessage
        this[MarkdownConfig.HASH_TEXT_COLOR] = BotStacks.colorScheme.primary
        this[MarkdownConfig.CODE_BACKGROUND_COLOR] = Color.Gray
        this[MarkdownConfig.CODE_BLOCK_TEXT_COLOR] = Color.White
    }
)