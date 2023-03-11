/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.theme

import Colors
import Fonts
import Theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.core.view.ViewCompat

val IACTheme = staticCompositionLocalOf { Theme() }
val IACColors = staticCompositionLocalOf { Colors(true) }

@Composable
fun InAppChatTheme(
    theme: Theme = IACTheme.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val rememberedTheme = remember { theme }.apply { theme.fromOtherTheme(theme) }
    val colorScheme = when {
        darkTheme -> theme.dark
        else -> theme.light
    }
    CompositionLocalProvider(IACTheme provides rememberedTheme, IACColors provides colorScheme) {
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
}
