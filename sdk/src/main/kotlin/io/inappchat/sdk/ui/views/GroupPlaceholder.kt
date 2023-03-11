/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import android.graphics.ColorSpace
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import dev.jorgecastillo.androidcolorx.library.HSLColor
import dev.jorgecastillo.androidcolorx.library.asColorInt
import dev.jorgecastillo.androidcolorx.library.asHsl
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.theme.IAC
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

@Composable
fun GroupPlaceholder(modifier: Modifier? = null) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        IAC.colors.primary.adjustedHsl(-25),
                        IAC.colors.primary.adjustedHsl(25)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        Icon(
            painter = painterResource(id = IAC.theme.assets.group ?: R.drawable.users_three_fill),
            contentDescription = "Group icon",
            modifier = Modifier.size(50.dp)
        )
    }
}