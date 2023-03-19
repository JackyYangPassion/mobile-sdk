/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.actions.react
import io.inappchat.sdk.actions.toggleFavorite
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.genTextMessage
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MessageActionSheet(
  message: Message?,
  onReply: (Message) -> Unit,
  content: @Composable () -> Unit
) {
  val state = rememberModalBottomSheetState(
    ModalBottomSheetValue.Expanded, skipHalfExpanded = true
  )
  val scope = rememberCoroutineScope()
  LaunchedEffect(key1 = message, block = {
    if (message != null) {
      state.show()
    } else {
      state.hide()
    }
  })
  val hide = { scope.launch { state.hide() } }
  val annotatedString = (message?.text ?: "").annotated()
  val clipboardManager = LocalClipboardManager.current
  val copy = {
    clipboardManager.setText(annotatedString)
    hide()
  }
  ModalBottomSheetLayout(
    sheetState = state,
    sheetBackgroundColor = colors.background,
    sheetContentColor = colors.text,
    scrimColor = colors.caption,
    sheetContent = {
      Space(8f)
      EmojiBar(
        current = message?.currentReaction,
        onEmoji = {
          message?.react(it)
          hide()
        }
      )
      ListItem(
        text = { Text(text = "Reply in Thread", iac = fonts.headline, color = colors.text) },
        icon = {
          Icon(
            painter = painterResource(id = io.inappchat.sdk.R.drawable.chat_dots),
            contentDescription = "Reply in Thread",
            tint = colors.text,
            modifier = Modifier.size(25.dp)
          )
        },
        modifier = Modifier.clickable {
          message?.let(onReply)
          hide()
        }
      )
      ListItem(
        text = {
          Text(
            text = if (message?.favorite == true) "Save to Favorites" else "Remove from Favorites",
            iac = fonts.headline,
            color = colors.text
          )
        },
        icon = {
          Icon(
            painter = painterResource(id = io.inappchat.sdk.R.drawable.star_fill),
            contentDescription = "favorite message",
            tint = colors.text,
            modifier = Modifier.size(25.dp)
          )
        },
        modifier = Modifier.clickable {
          message?.toggleFavorite()
          hide()
        }
      )
      ListItem(
        text = {
          Text(
            text = "Copy message text",
            iac = fonts.headline,
            color = colors.text
          )
        },
        icon = {
          Icon(
            painter = painterResource(id = io.inappchat.sdk.R.drawable.copy),
            contentDescription = "Copy message text",
            tint = colors.text,
            modifier = Modifier.size(25.dp)
          )
        },
        modifier = Modifier.clickable {
          copy()
        }
      )
    },
    content = content
  )
}

@IPreviews
@Composable
fun MessageActionSheetPreview() {
  InAppChatContext {
    var message by remember {
      mutableStateOf<Message?>(genTextMessage())
    }
    MessageActionSheet(message = message, onReply = {}) {
      Button(onClick = { message = genTextMessage() }) {
        Text(text = "Open Sheet", iac = fonts.headline)
      }
    }
  }
}