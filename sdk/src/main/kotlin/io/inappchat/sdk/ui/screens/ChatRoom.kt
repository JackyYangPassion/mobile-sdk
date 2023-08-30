/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextOverflow
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genChat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
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
    val ctx = rememberCoroutineScope()
    var focusRequester = remember { FocusRequester() }
    var messageForAction by remember {
        mutableStateOf<Message?>(null)
    }
    val media = androidx.compose.material.rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    val menu = androidx.compose.material.rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    MediaActionSheet(
        state = media,
        chat = chat,
        dismiss = { ctx.launch { menu.hide() } },
        inReplyTo = message
    ) {
        MessageActionSheet(
            message = messageForAction,
            hide = { messageForAction = null },
            onReply = openReply
        ) {
            ChatDrawer(
                chat = chat,
                state = menu,
                hide = { ctx.launch { menu.hide() } },
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
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                chat.let {
                                    ChatCount(count = it.members.size)
                                }
                            }
                        }
                    }, back = back, menu = { ctx.launch { menu.show() } })
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
                        ctx.launch { media.show() }
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