/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import Colors
import Fonts
import Theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import com.halilibo.richtext.ui.RichTextStyle
import richText

val IACTheme = staticCompositionLocalOf { Theme() }
val IACRichText = staticCompositionLocalOf { Theme().richText() }

@Composable
fun InAppChatContext(
    theme: Theme = IACTheme.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val rememberedTheme = remember { theme }.apply { theme.fromOtherTheme(theme) }

    LaunchedEffect(key1 = darkTheme, block = {
        theme.isDark = darkTheme
    })
    CompositionLocalProvider(
        IACTheme provides rememberedTheme,
        IACRichText provides rememberedTheme.richText()
    ) {
        content()
    }
}

object IAC {
    /**
     * Retrieves the current [ColorScheme] at the call site's position in the hierarchy.
     */
    val theme: Theme
        @Composable
        @ReadOnlyComposable
        get() = IACTheme.current

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = theme.colors

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val fonts: Fonts
        @Composable
        @ReadOnlyComposable
        get() = theme.fonts

    val richTextStyle: RichTextStyle
        @Composable
        @ReadOnlyComposable
        get() = IACRichText.current
}
