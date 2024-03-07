/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.internal.ui.components

import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.internal.utils.IPreviews
import ai.botstacks.sdk.internal.utils.annotated
import ai.botstacks.`chat-sdk`.generated.resources.Res
import dev.icerock.moko.resources.compose.painterResource


@Composable
internal fun ChatCount(count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(Res.images.users_fill),
            contentDescription = "members",
            modifier = Modifier.size(16.dp),
            tint = BotStacks.colorScheme.caption
        )
        Space()
        Text(
            text = count.toString().annotated(),
            fontStyle = BotStacks.fonts.caption1,
            color = BotStacks.colorScheme.caption
        )
    }
}

@IPreviews
@Composable
private fun ChatCountPreview() {
    BotStacksThemeEngine {
        Column {
            ChatCount(count = 1)
            ChatCount(count = 4)
            ChatCount(count = 10)
            ChatCount(count = 301)
            ChatCount(count = 2931)
        }
    }
}
