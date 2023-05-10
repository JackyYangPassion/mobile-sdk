/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.inappchat.sdk.ui.theme.Colors
import io.inappchat.sdk.ui.theme.Fonts
import io.inappchat.sdk.ui.theme.Theme

val IACTheme = staticCompositionLocalOf { Theme() }

@Composable
fun InAppChatContext(
    theme: Theme = IACTheme.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val rememberedTheme = remember { theme.with(darkTheme) }.apply { fromOtherTheme(theme) }

    CompositionLocalProvider(
        IACTheme provides rememberedTheme.with(darkTheme),
    ) {
        content()
    }
}

object IAC {
    /**
     * Retrieves the current [Theme] at the call site's position in the hierarchy.
     */
    val theme: Theme
        @Composable
        @ReadOnlyComposable
        get() = IACTheme.current

    /**
     * Retrieves the current [Colors] at the call site's position in the hierarchy.
     */
    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = theme.colors

    /**
     * Retrieves the current [Fonts] at the call site's position in the hierarchy.
     */
    val fonts: Fonts
        @Composable
        @ReadOnlyComposable
        get() = theme.fonts

}
