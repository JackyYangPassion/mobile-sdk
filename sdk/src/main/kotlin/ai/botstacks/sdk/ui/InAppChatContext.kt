/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import ai.botstacks.sdk.ui.theme.Colors
import ai.botstacks.sdk.ui.theme.Fonts
import ai.botstacks.sdk.ui.theme.Theme

val IACTheme = staticCompositionLocalOf { Theme() }

@Composable
fun BotStacksChatContext(
    theme: Theme = IACTheme.current,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val rememberedTheme = remember { theme.with(darkTheme) }.apply { fromOtherTheme(theme) }
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
