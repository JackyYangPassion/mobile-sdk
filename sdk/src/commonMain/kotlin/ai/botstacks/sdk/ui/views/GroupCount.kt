/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks.theme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated

class CountProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int> = sequenceOf(1, 4, 10, 301, 2931)
}

@Composable
fun ChatCount(count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = Drawables.UsersFilled,
            contentDescription = "members",
            modifier = Modifier.size(16.dp),
            tint = theme.colors.caption
        )
        Space()
        Text(
            text = count.toString().annotated(),
            iac = theme.fonts.caption,
            color = theme.colors.caption
        )
    }
}

@IPreviews
@Composable
fun ChatpCountPreview() {
    BotStacksChatContext {
        Column {
            ChatCount(count = 1)
            ChatCount(count = 4)
            ChatCount(count = 10)
            ChatCount(count = 301)
            ChatCount(count = 2931)
        }
    }
}
