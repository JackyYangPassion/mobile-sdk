/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.actions.send
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.Fn
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genChat


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageInput(
    chat: Chat,
    replyingTo: Message? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onMedia: Fn
) {
    var text by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val onSend = {
        if (!text.isBlank()) {
            chat.send(replyingTo?.id, text)
            keyboardController?.hide()
            text = ""
        }
    }

    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)) {
        Row {
            TextInput(
                text = text,
                onChange = { text = it },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f),
                keyboardActions = KeyboardActions(onDone = { onSend() })
            ) {
                IconButton(onClick = onMedia, modifier = Modifier.size(20.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.paperclip_fill),
                        contentDescription = "send attachment",
                        modifier = Modifier.size(20.dp),
                        tint = colors.caption
                    )
                }
            }
            Space(8f)
            IconButton(onClick = onSend, modifier = Modifier.circle(44.dp, colors.softBackground)) {
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
    InAppChatContext {
        MessageInput(chat = genChat(), null) {

        }
    }

}