/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genChatChat

@Composable
fun ChatChat(
    room: Chat,
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
    MediaActionSheet(open = media, room = room, dismiss = { menu = false }, inReplyTo = message) {
        MessageActionSheet(
            message = messageForAction,
            hide = { messageForAction = null },
            onReply = openReply
        ) {
            ChatDrawer(
                chat = room.chat,
                open = menu,
                hide = { menu = false },
                openEdit = openEdit,
                openInvite = openInvite,
                openProfile = openProfile,
                back = back
            ) {
                Header(title = "", icon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Avatar(room.image, 35.0, room.chat != null)
                        Column {
                            Text(
                                text = room.name,
                                iac = fonts.title2,
                                color = colors.text,
                                maxLines = 1
                            )
                            room.chat?.let {
                                ChatCount(count = it.participants.size)
                            }
                        }
                    }
                }, back = back, menu = { menu = true })
                MessageList(room = room, onLongPress = { messageForAction = it })
                MessageInput(room = room, replyingTo = message, focusRequester = focusRequester) {
                    media = true
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
            room = genChatChat(),
            openProfile = {},
            openInvite = {},
            openReply = {},
            openEdit = {}
        ) {

        }
    }
}