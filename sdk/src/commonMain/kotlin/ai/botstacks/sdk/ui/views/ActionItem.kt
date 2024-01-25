/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.utils.IPreviews
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ActionItem(text: String, @DrawableRes icon: Int, divider: Boolean = false, action: () -> Unit) {
    ActionItem(text, painterResource(id = icon), divider, action)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionItem(text: String, icon: Painter, divider: Boolean = false, action: () -> Unit) {
    Column {
        ListItem(
            text = {
                Text(
                    text = text,
                    fontStyle = BotStacks.fonts.body2,
                    color = BotStacks.colorScheme.text
                )
            },
            icon = {
                Icon(
                    painter = icon,
                    contentDescription = text,
                    tint = BotStacks.colorScheme.text,
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

@IPreviews
@Composable
fun ActionItemPreview() {
    BotStacksChatContext {
        Column {
            ActionItem(text = "Item", icon = Drawables.AddressBookFilled) {

            }
        }
    }
}