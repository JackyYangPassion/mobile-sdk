/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.actions.retry
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.Identifiable
import ai.botstacks.sdk.state.Pager
import ai.botstacks.sdk.state.SendingMessage
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.IAC.fonts


@Composable
fun MessageList(
    chat: Chat,
    modifier: Modifier,
    onPressUser: (User) -> Unit,
    onLongPress: (Message) -> Unit
) {
    PagerList(
        pager = chat as Pager<Identifiable>,
        prefix = chat.sending,
        modifier = modifier,
        scrollToTop = chat.sending.firstOrNull()?.id ?: chat.items.firstOrNull()?.id,
        invert = true,
        empty = { Text(text = "No messages yet.", iac = fonts.title3) }) {
        if (it is SendingMessage) {
            SendingMessageView(message = it)
        } else if (it is Message) {
            MessageView(message = it, onPressUser = onPressUser, onLongPress = onLongPress)
        }
    }
}

@Composable
fun SendingMessageView(message: SendingMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (message.failed) {
                    message.retry()
                }
            },
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Row(modifier = Modifier.fillMaxWidth(0.85f)) {
                MessageView(
                    message = message.msg,
                    onPressUser = {},
                    onLongPress = {},
                    onClick = {
                        if (message.failed) {
                            message.retry()
                        }
                    })
            }
            if (message.failed) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Retry sending message",
                    tint = Color.Red,
                    modifier = Modifier.size(30.dp)
                )
            } else {
                Spinner()
            }
        }
        if (message.failed) {
            Text(
                text = "The message failed to send. Tap to retry.",
                iac = fonts.body,
                color = Color.Red
            )
        }
    }
}