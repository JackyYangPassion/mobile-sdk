/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.actions.react
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.*

@Composable
fun MessageView(message: Message, onPressUser: (User) -> Unit) {
  if (message.user.blocked) {
    return
  }
  val current = message.user.isCurrent
  val align = if (current) theme.senderAlignment else theme.messageAlignment



  Column(horizontalAlignment = align, modifier = Modifier.fillMaxWidth()) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(5.dp),
      verticalAlignment = Alignment.Top,
      modifier = Modifier
        .padding(10.dp, 5.dp)
        .fillMaxWidth(0.8f)
    ) {
      if (align == Alignment.Start) {
        Spinner(loading = message.sending && current)
        Favorite(message.favorite)
        Avvy(message.user.avatar) { onPressUser(message.user) }
        Content(msg = message, modifier = Modifier.weight(1f))
      } else {
        Content(msg = message, modifier = Modifier.weight(1f))
        Avvy(message.user.avatar) { onPressUser(message.user) }
        Favorite(message.favorite)
        Spinner(loading = message.sending && current)
      }
    }
  }
}

@IPreviews
@Composable
fun MessageViewPreview() {
  InAppChatContext {
    Column {
      MessageView(message = genTextMessage(genU()), onPressUser = {})
      MessageView(message = genTextMessage(genCurrentUser()), onPressUser = {})
    }
  }
}

@Composable
fun ReplyCount(count: Int) {
  if (count > 0) {
    Text(
      text = "$count replies",
      iac = fonts.body,
      color = colors.primary,
      modifier = Modifier.padding(4.dp)
    )
  }
}

@Composable
fun Favorite(favorite: Boolean) {
  if (favorite) {
    Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {
      Icon(
        painter = painterResource(id = io.inappchat.sdk.R.drawable.star_fill),
        contentDescription = "favorite",
        tint = colors.primary,
        modifier = Modifier.size(20),
      )
    }
  }
}

@Composable
fun Spinner(loading: Boolean) {
  if (loading) {
    CircularProgressIndicator(color = colors.primary, modifier = Modifier.size(20))
  }
}

@Composable
fun Avvy(url: String?, onClick: Fn) {
  Pressable(onClick = onClick) {
    Avatar(url)
  }
}

@Composable
fun Reactions(msg: Message, modifier: Modifier = Modifier) {
  for (reactions in msg.reactions.chunked(5)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
      for (reaction in reactions) {
        ClickableText(
          text = "${reaction.emojiCode} ${reaction.count}",
          onClick = { msg.react(reaction.emojiCode) },
          iac = fonts.body,
          color = colors.text,
          modifier = Modifier
            .radius(36.dp)
            .background(colors.bubble)
            .border(
              2.dp,
              if (msg.currentReaction == reaction.emojiCode) colors.primary else Color.Transparent,
              RoundedCornerShape(36.dp)
            )
            .padding(theme.bubblePadding)
        )
      }
    }
  }
}

@Composable
fun Content(msg: Message, modifier: Modifier = Modifier) {
  Column(
    horizontalAlignment = ift(
      msg.user.isCurrent,
      theme.senderAlignment,
      theme.messageAlignment
    ), verticalArrangement = Arrangement.spacedBy(4.dp),
    modifier = modifier
  ) {
    MessageTop(msg)
    MessageContent(message = msg)
    Reactions(msg)
    ReplyCount(count = msg.replyCount)
  }
}

@Composable
fun MessageTop(msg: Message) {
  Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = msg.user.username,
      iac = fonts.username,
      color = ift(msg.user.isCurrent, colors.senderUsername, colors.username),
      modifier = Modifier.requiredSizeIn(maxWidth = 120.dp),
      maxLines = 1
    )
    Text(text = msg.createdAt.timeAgo(), iac = fonts.timestamp, color = colors.timestamp)
  }
}