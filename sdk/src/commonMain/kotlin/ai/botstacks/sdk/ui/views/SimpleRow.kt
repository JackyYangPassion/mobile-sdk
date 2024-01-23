/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.fonts
import ai.botstacks.sdk.ui.resources.Drawables
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun SimpleRow(
    @DrawableRes icon: Int,
    text: String,
    iconPrimary: Boolean = false,
    destructive: Boolean = false,
    onClick: () -> Unit
) {
    SimpleRow(
        icon = painterResource(id = icon),
        text = text,
        iconPrimary = iconPrimary,
        destructive = destructive,
        onClick = onClick
    )
}

@Composable
fun SimpleRow(
    icon: Painter,
    text: String,
    iconPrimary: Boolean = false,
    destructive: Boolean = false,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = onClick)
                .height(75.dp), horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = icon,
                contentDescription = "icon",
                modifier = Modifier.size(40),
                colorFilter = ColorFilter.tint(if (destructive) colors.destructive else if (iconPrimary) colors.primary else colors.text)
            )
            Text(
                text = text,
                iac = fonts.title3.copy(weight = FontWeight.Bold),
                color = if (destructive) colors.destructive else colors.text
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = Drawables.CaretLeft,
                contentDescription = "more",
                colorFilter = ColorFilter.tint(if (destructive) colors.destructive else colors.caption),
                modifier = Modifier
                    .size(16.dp)
                    .rotate(180f)
            )
        }
        Divider(color = colors.softBackground)
    }
}