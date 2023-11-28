/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

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
import ai.botstacks.sdk.R
import ai.botstacks.sdk.actions.send
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.utils.Fn
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genChat


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

    Column(modifier = Modifier.padding(start = 16.dp, end = 32.dp, top = 8.dp, bottom = 16.dp)) {
        Row {
            TextInput(
                text = text,
                onChange = { text = it },
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f),
                keyboardActions = KeyboardActions(onDone = { onSend() }),
                placeholder = "Send a message"
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
            if (!text.isNullOrBlank()) {
                Space(8f)
                IconButton(
                    onClick = onSend,
                    modifier = Modifier.circle(44.dp, colors.softBackground)
                ) {
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
}

@IPreviews
@Composable
fun MessageInputPreview() {
    BotStacksChatContext {
        MessageInput(chat = genChat(), null) {

        }
    }

}