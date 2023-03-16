/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.utils.IPreviews

@Composable
fun MessageView(message: Message, onPressUser: (User) -> Unit) {
  if (message.user.blocked) {
    return
  }
  val align = if (message.user.isCurrent) theme.senderAlignment else theme.messageAlignment
  Column(horizontalAlignment = align) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

    }
  }
}

@IPreviews
@Composable
fun MessageViewPreview() {

}