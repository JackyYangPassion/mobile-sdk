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
import io.inappchat.sdk.models.MessageStatus
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.utils.timeAgo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.days


@Composable
fun ThreadRow(thread: Room, onClick: (Room) -> Unit) {
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
          if (thread.isUnread) colors.unread else colors.softBackground,
          CircleShape
        )
    ) {
      Avatar(url = thread.image, 46.0)
    }
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.padding(start = 14.dp)) {
      Row {
        Text(text = thread.name, iac = fonts.title3, color = colors.text, maxLines = 1)
        Space()
        thread.group?.let { PrivacyPill(it._private) }
      }
      Row {
        if (thread.latest?.status == MessageStatus.seen) {
          Image(
            painter = painterResource(id = R.drawable.check_circle_fill),
            contentDescription = "message read",
            modifier = Modifier.size(12.dp),
            colorFilter = ColorFilter.tint(colors.primary)
          )
        }
        Text(
          text = thread.latest?.summary ?: "No messages yet",
          iac = fonts.body,
          maxLines = 2,
          color = if (thread.isUnread) colors.text else colors.caption,
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
      thread.latest?.let {
        Text(
          text = tsString(it.createdAt), iac = fonts.body, color = colors.caption
        )
      }
      if (thread.isUnread) {
        Badge(thread.unreadCount)
      }
    }
  }
}

fun tsString(i: Instant): String {
  if (i >= Instant.now().minusSeconds(60 * 60 * 3)) {
    return i.timeAgo()
  } else if (i.isAfter(Instant.now().minusSeconds(1.days.inWholeSeconds))) {
    return i.atZone(ZoneId.systemDefault())
      .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
  } else {
    return i.timeAgo()
  }
}