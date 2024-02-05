/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.relativeTimeString
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChatRow(chat: Chat, onClick: (Chat) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp, 12.dp)
            .height(84.dp)
            .clickable { onClick(chat) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(colorScheme.surface, CircleShape)
                .border(
                    2.dp,
                    if (chat.isUnread) colorScheme.primary else colorScheme.surface,
                    CircleShape
                )
        ) {
            Avatar(url = chat.displayImage)
        }
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 14.dp)) {
            Row {
                Text(text = chat.displayName, fontStyle = fonts.h3, color = colorScheme.onBackground, maxLines = 1)
                Space()
                if (chat.isGroup) {
                    PrivacyPill(chat._private)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!chat.isUnread)
                    Image(
                        painter = painterResource(Res.Drawables.Filled.CheckCircle),
                        contentDescription = "message read",
                        modifier = Modifier.size(12.dp),
                        colorFilter = ColorFilter.tint(colorScheme.primary)
                    )
                Text(
                    text = chat.latest?.summary ?: "No messages yet",
                    fontStyle = fonts.body1,
                    maxLines = 2,
                    color = if (chat.isUnread) colorScheme.onBackground else colorScheme.caption,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minWidth = 18.dp)
        )
        Column(horizontalAlignment = Alignment.End) {
            chat.latest?.let {
                Text(
                    text = it.createdAt.relativeTimeString(), fontStyle = fonts.body1, color = colorScheme.caption
                )
            }
            if (chat.isUnread) {
                Badge(chat.unreadCount)
            }
        }
    }
}

//fun tsString(i: Instant): String {
//    if (i >= Clock.System.now().minus(60.seconds * 60 * 3)) {
//        return i.relativeTimeString()
//    } else if (i > (Clock.System.now().minus(1.days.inWholeSeconds.seconds))) {
//        return i.toLocalDateTime(TimeZone.UTC)
//            .toJavaLocalDateTime()
//            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
//    } else {
//        return i.timeAgo()
//    }
//}