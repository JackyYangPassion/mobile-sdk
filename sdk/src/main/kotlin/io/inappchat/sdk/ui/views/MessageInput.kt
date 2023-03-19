/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.actions.send
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.utils.Fn
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genT


@Composable
fun MessageInput(
  room: Room,
  replyingTo: Message? = null,
  focusRequester: FocusRequester,
  onMedia: Fn
) {
  var text by remember {
    mutableStateOf("")
  }

  Column(modifier = Modifier.padding(16.dp, 8.dp)) {
    Row {
      Row(
        modifier = Modifier
          .weight(1f)
          .defaultMinSize(minHeight = 44.dp)
          .background(colors.softBackground, RoundedCornerShape(22.dp))
          .padding(12.dp)
      ) {
        Box(modifier = Modifier.weight(1f)) {
          BasicTextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle.Default.copy(
              color = colors.text,
              fontFamily = fonts.body.family,
              fontSize = fonts.body.size,
              fontWeight = fonts.body.weight
            ),
            maxLines = 4,
            modifier = Modifier.focusRequester(focusRequester)
          )
          if (text.isBlank()) {
            Text(text = "Send a chat", iac = fonts.body, color = colors.caption)
          }
        }
        IconButton(onClick = onMedia, modifier = Modifier.size(20.dp)) {
          Icon(
            painter = painterResource(id = R.drawable.paperclip_fill),
            contentDescription = "send attachment",
            modifier = Modifier.size(20.dp),
            tint = colors.caption
          )
        }
        Space(8f)
      }
      Space(8f)
      IconButton(onClick = {
        if (!text.isBlank()) {
          room.send(replyingTo?.id, text)
          text = ""
        }
      }, modifier = Modifier.circle(44.dp, colors.softBackground)) {
        Icon(
          painter = painterResource(id = R.drawable.paper_plane_tilt_fill),
          contentDescription = "send message",
          modifier = Modifier.size(22.dp),
          tint = colors.primary
        )
      }
    }
  }
}

@IPreviews
@Composable
fun MessageInputPreview() {
  MessageInput(room = genT(), null, {})
}