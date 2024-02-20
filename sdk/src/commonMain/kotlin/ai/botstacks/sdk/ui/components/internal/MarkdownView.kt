package ai.botstacks.sdk.ui.components.internal

import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.markdownColor
import com.mikepenz.markdown.model.markdownPadding

@Composable
fun MarkdownView(modifier: Modifier = Modifier, content: String, isCurrentUser: Boolean) {
    val openUrl = LocalUriHandler.current

    // TODO: checkbox and hashtags?
    Markdown(
        modifier = modifier,
        typography = BotStacks.fonts.forMarkdown(),
        colors = markdownColor(
            text = if (isCurrentUser) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.onMessage,
            linkText = if (isCurrentUser) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.primary,
            codeBackground = Color.Gray,
            codeText = Color.White,
        ),
        padding = markdownPadding(block = 0.dp),
        content = content
    )
}

//@Stable
//@Composable
//fun markdownConfig(sender: Boolean) = MarkdownConfig(
//    isLinksClickable = true,
//    isImagesClickable = false,
//    isScrollEnabled = false,
//    colors = HashMap<String, Color>().apply {
//        this[MarkdownConfig.CHECKBOX_COLOR] = Color.Black
//        this[MarkdownConfig.LINKS_COLOR] = if (sender) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.primary
//        this[MarkdownConfig.TEXT_COLOR] = if (sender) BotStacks.colorScheme.onPrimary else BotStacks.colorScheme.onMessage
//        this[MarkdownConfig.HASH_TEXT_COLOR] = BotStacks.colorScheme.primary
//        this[MarkdownConfig.CODE_BACKGROUND_COLOR] = Color.Gray
//        this[MarkdownConfig.CODE_BLOCK_TEXT_COLOR] = Color.White
//    }
//)