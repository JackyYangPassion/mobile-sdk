/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui

import ai.botstacks.sdk.ui.theme.Assets
import ai.botstacks.sdk.ui.theme.BotStacksColorPalette
import ai.botstacks.sdk.ui.theme.Colors
import ai.botstacks.sdk.ui.theme.DayNightColorScheme
import ai.botstacks.sdk.ui.theme.Dimensions
import ai.botstacks.sdk.ui.theme.Fonts
import ai.botstacks.sdk.ui.theme.LocalBotStacksAssets
import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import ai.botstacks.sdk.ui.theme.LocalBotStacksDimens
import ai.botstacks.sdk.ui.theme.LocalBotStacksFonts
import ai.botstacks.sdk.ui.theme.LocalBotStacksShapes
import ai.botstacks.sdk.ui.theme.Theme
import ai.botstacks.sdk.ui.theme.darkColors
import ai.botstacks.sdk.ui.theme.lightColors
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import ai.botstacks.sdk.ui.theme.fonts as defaultFonts


@Composable
fun BotStacksChatContext(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    lightColorScheme: Colors = lightColors(),
    darkColorScheme: Colors = darkColors(),
    shapes: Shapes = BotStacks.shapes,
    assets: Assets = BotStacks.assets,
    fonts: Fonts? = null,
    content: @Composable () -> Unit
) {
    val ctx = LocalContext.current
    DisposableEffect(key1 = true, effect = {
        val original = Coil.imageLoader(ctx)
        Coil.setImageLoader(
            ImageLoader.Builder(ctx)
                .components {
                    if (Build.VERSION.SDK_INT >= 28) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        )
        onDispose {
            Coil.setImageLoader(original)
        }
    })

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
