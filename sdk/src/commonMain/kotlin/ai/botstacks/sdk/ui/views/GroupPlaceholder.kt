/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import dev.jorgecastillo.androidcolorx.library.asHsl
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.IPreviews
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import java.lang.Float.max
import java.lang.Float.min

fun Color.adjustedHsl(by: Int): Color {
    val hsl = this.toArgb().asHsl()
    var h2 = hsl.hue + by
    if (h2 > hsl.hue) {
        h2 = min(360f, h2)
    } else {
        h2 = max(0f, h2)
    }
    return Color.hsl(h2, hsl.saturation, hsl.lightness)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChatPlaceholder(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        BotStacks.colorScheme.primary.adjustedHsl(-25),
                        BotStacks.colorScheme.primary.adjustedHsl(25)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                ),
                shape = RectangleShape
            )
            .size(50)
    ) {
        Icon(
            painter =  BotStacks.assets.chat?.let { painterResource(it) } ?: painterResource(Res.Drawables.Filled.UsersThree),
            contentDescription = "Chat icon",
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}

@IPreviews
@Composable
fun ChatPlaceholderPreview() {
    ChatPlaceholder()
}