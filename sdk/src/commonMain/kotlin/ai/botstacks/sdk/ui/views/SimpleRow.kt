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
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.resources.Res
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SimpleRow(
    icon: DrawableResource,
    text: String,
    iconPrimary: Boolean = false,
    destructive: Boolean = false,
    onClick: () -> Unit
) {
    SimpleRow(
        icon = painterResource(icon),
        text = text,
        iconPrimary = iconPrimary,
        destructive = destructive,
        onClick = onClick
    )
}


@OptIn(ExperimentalResourceApi::class)
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
                colorFilter = ColorFilter.tint(if (destructive) colorScheme.error else if (iconPrimary) colorScheme.primary else colorScheme.onBackground)
            )
            Text(
                text = text,
                fontStyle = fonts.h3.copy(weight = FontWeight.Bold),
                color = if (destructive) colorScheme.error else colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(Res.Drawables.Outlined.CaretLeft),
                contentDescription = "more",
                colorFilter = ColorFilter.tint(if (destructive) colorScheme.error else colorScheme.caption),
                modifier = Modifier
                    .size(16.dp)
                    .rotate(180f)
            )
        }
        Divider(color = colorScheme.border)
    }
}