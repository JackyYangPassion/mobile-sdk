/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.Text
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.IPreviews
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ActionItem(text: String, icon: DrawableResource, divider: Boolean = false, action: () -> Unit) {
    ActionItem(text, painterResource(icon), divider, action)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ActionItem(text: String, icon: Painter, divider: Boolean = false, action: () -> Unit) {
    Column {
        ListItem(
            text = {
                Text(
                    text = text,
                    fontStyle = BotStacks.fonts.body2,
                    color = BotStacks.colorScheme.onBackground
                )
            },
            icon = {
                Icon(
                    painter = icon,
                    contentDescription = text,
                    tint = BotStacks.colorScheme.onBackground,
                    modifier = Modifier.size(25.dp)
                )
            },
            modifier = Modifier.clickable {
                action()
            }
        )
        if (divider) Divider(color = BotStacks.colorScheme.caption)
    }
}

@OptIn(ExperimentalResourceApi::class)
@IPreviews
@Composable
private fun ActionItemPreview() {
    BotStacksChatContext {
        Column {
            ActionItem(text = "Item", icon = Res.Drawables.Filled.AddressBook) {

            }
        }
    }
}