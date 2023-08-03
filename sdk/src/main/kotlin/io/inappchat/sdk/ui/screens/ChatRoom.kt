/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genChat

@Composable
fun ChatChat(
    chat: Chat,
    message: Message? = null,
    openProfile: (User) -> Unit,
    openInvite: (Chat) -> Unit,
    openReply: (Message) -> Unit,
    openEdit: (Chat) -> Unit,
    back: () -> Unit
) {
    var focusRequester = remember { FocusRequester() }
    var media by remember { mutableStateOf(false) }
    var menu by remember { mutableStateOf(false) }
    var messageForAction by remember {
        mutableStateOf<Message?>(null)
    }
    MediaActionSheet(open = media, chat = chat, dismiss = { menu = false }, inReplyTo = message) {
        MessageActionSheet(
            message = messageForAction,
            hide = { messageForAction = null },
            onReply = openReply
        ) {
            ChatDrawer(
                chat = chat,
                open = menu,
                hide = { menu = false },
                openEdit = openEdit,
                openInvite = openInvite,
                openProfile = openProfile,
                back = back
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Header(title = "", icon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Avatar(chat.displayImage, 35.0, chat != null)
                            Column {
                                Text(
                                    text = chat.displayName,
                                    iac = fonts.title2,
                                    color = colors.text,
                                    maxLines = 1
                                )
                                chat.let {
                                    ChatCount(count = it.members.size)
                                }
                            }
                        }
                    }, back = back, menu = { menu = true })
                    MessageList(
                        chat = chat,
                        modifier = Modifier.weight(1f),
                        onPressUser = { openProfile(it) },
                        onLongPress = { messageForAction = it })
                    MessageInput(
                        chat = chat,
                        replyingTo = message,
                        focusRequester = focusRequester
                    ) {
                        media = true
                    }
                }
            }
        }
    }
}

@IPreviews
@Composable
fun ChatChatPreview() {
    InAppChatContext {
        ChatChat(
            chat = genChat(),
            openProfile = {},
            openInvite = {},
            openReply = {},
            openEdit = {}
        ) {

        }
    }
}