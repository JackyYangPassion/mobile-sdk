/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.utils.timeAgo
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds


@Composable
fun ChatRow(chat: Chat, onClick: (Chat) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp, 12.dp)
            .height(84.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(colors.softBackground, CircleShape)
                .border(
                    2.dp,
                    if (chat.isUnread) colors.unread else colors.softBackground,
                    CircleShape
                )
        ) {
            Avatar(url = chat.displayImage, 46.0)
        }
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 14.dp)) {
            Row {
                Text(text = chat.displayName, iac = fonts.title3, color = colors.text, maxLines = 1)
                Space()
                if (chat.isGroup) {
                    PrivacyPill(chat._private)
                }
            }
            Row {
                Image(
                    painter = painterResource(id = R.drawable.check_circle_fill),
                    contentDescription = "message read",
                    modifier = Modifier.size(12.dp),
                    colorFilter = ColorFilter.tint(colors.primary)
                )
                Text(
                    text = chat.latest?.summary ?: "No messages yet",
                    iac = fonts.body,
                    maxLines = 2,
                    color = if (chat.isUnread) colors.text else colors.caption,
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
                    text = tsString(it.createdAt), iac = fonts.body, color = colors.caption
                )
            }
            if (chat.isUnread) {
                Badge(chat.unreadCount)
            }
        }
    }
}

fun tsString(i: Instant): String {
    if (i >= Clock.System.now().minus(60.seconds * 60 * 3)) {
        return i.timeAgo()
    } else if (i > (Clock.System.now().minus(1.days.inWholeSeconds.seconds))) {
        return i.toLocalDateTime(TimeZone.UTC)
            .toJavaLocalDateTime()
            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    } else {
        return i.timeAgo()
    }
}