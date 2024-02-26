/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui

import ai.botstacks.sdk.ui.theme.Assets
import ai.botstacks.sdk.ui.theme.Colors
import ai.botstacks.sdk.ui.theme.DayNightColorScheme
import ai.botstacks.sdk.ui.theme.Dimensions
import ai.botstacks.sdk.ui.theme.Fonts
import ai.botstacks.sdk.ui.theme.LocalBotStacksAssets
import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import ai.botstacks.sdk.ui.theme.LocalBotStacksDimens
import ai.botstacks.sdk.ui.theme.LocalBotStacksFonts
import ai.botstacks.sdk.internal.ui.theme.LocalBotStacksShapes
import ai.botstacks.sdk.internal.ui.theme.Theme
import ai.botstacks.sdk.ui.theme.darkBotStacksColors
import ai.botstacks.sdk.ui.theme.lightBotStacksColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import ai.botstacks.sdk.ui.theme.botstacksFonts as defaultFonts


@Composable
fun BotStacksThemeEngine(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    lightColorScheme: Colors = lightBotStacksColors(),
    darkColorScheme: Colors = darkBotStacksColors(),
    shapes: Shapes = BotStacks.shapes,
    assets: Assets = BotStacks.assets,
    fonts: Fonts? = null,
    content: @Composable () -> Unit
) {
    // merge defaults with user provided fonts, if available
    val appFonts = defaultFonts().merge(fonts)

    Theme(
        assets = assets,
        isDark = useDarkTheme,
        colorScheme = DayNightColorScheme(lightColorScheme, darkColorScheme),
        fonts = appFonts,
        shapes = shapes,
    ) {
        content()
    }
}

object BotStacks {
    val assets: Assets
        @Composable
        @ReadOnlyComposable
        get() = LocalBotStacksAssets.current

    /**
     * Retrieves the current [Colors] at the call site's position in the hierarchy.
     */
    val colorScheme: Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalBotStacksColorScheme.current

    val dimens: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalBotStacksDimens.current

    /**
     * Retrieves the current [Fonts] at the call site's position in the hierarchy.
     */
    val fonts: Fonts
        @Composable
        @ReadOnlyComposable
        get() = LocalBotStacksFonts.current

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = LocalBotStacksShapes.current
}
