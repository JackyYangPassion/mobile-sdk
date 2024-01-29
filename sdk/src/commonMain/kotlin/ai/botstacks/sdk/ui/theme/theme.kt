package ai.botstacks.sdk.ui.theme
/*
 * Copyright (c) 2023.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.yazantarifi.compose.library.MarkdownConfig

@Composable
internal fun Theme(
    assets: Assets,
    isDark: Boolean,
    colorScheme: DayNightColorScheme,
    dimens: Dimens,
    fonts: Fonts,
    shapes: Shapes,
    content: @Composable () -> Unit,
) {
    val _colorsScheme = remember(isDark, colorScheme) {
        colorScheme.colors(isDark)
    }

    @Stable
    fun markdownConfig(sender: Boolean) = MarkdownConfig(
        isLinksClickable = true,
        isImagesClickable = false,
        isScrollEnabled = false,
        colors = HashMap<String, Color>().apply {
            this[MarkdownConfig.CHECKBOX_COLOR] = Color.Black
            this[MarkdownConfig.LINKS_COLOR] = _colorsScheme.primary
            this[MarkdownConfig.TEXT_COLOR] = if (sender) _colorsScheme.senderText else _colorsScheme.bubbleText
            this[MarkdownConfig.HASH_TEXT_COLOR] = _colorsScheme.primary
            this[MarkdownConfig.CODE_BACKGROUND_COLOR] = Color.Gray
            this[MarkdownConfig.CODE_BLOCK_TEXT_COLOR] = Color.White
        }
    )

    CompositionLocalProvider(
        LocalBotStacksAssets provides assets,
        LocalBotStacksDayNightColorScheme provides colorScheme,
        LocalBotStacksColorScheme provides _colorsScheme,
        LocalBotStacksDimens provides dimens,
        LocalBotStacksFonts provides fonts,
        LocalBotStacksMarkdownConfig provides { sender -> markdownConfig(sender) },
        LocalBotStacksShapes provides shapes,
    ) {
        Box(
            modifier = Modifier
                .background(colorScheme.colors(isDark).background),
        ) {
            CompositionLocalProvider(LocalContentColor provides colorScheme.colors(isDark).text) {
                content()
            }
        }
    }
}

internal val LocalBotStacksMarkdownConfig = staticCompositionLocalOf { { sender: Boolean -> MarkdownConfig() } }
