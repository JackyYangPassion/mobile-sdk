/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextOverflow
import ai.botstacks.sdk.actions.markRead
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genChat
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
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
    DisposableEffect(key1 = chat.id, effect = {
        Chat.currentlyViewed = chat.id
        chat.markRead()
        onDispose {
            if (chat.id == Chat.currentlyViewed) {
                Chat.currentlyViewed = null
            }
            chat.markRead()
        }
    })
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
                    Header(icon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Avatar(
                                type = AvatarType.Channel(listOf(chat.displayImage)),
                            )
                            Column {
                                Text(
                                    text = chat.displayName,
                                    fontStyle = fonts.h2,
                                    color = colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                ChatCount(count = chat.members.size)
                            }
                        }
                    }, onBackClick = back, onMenuClick = { ctx.launch { menu.show() } })
                    MessageList(
                        chat = chat,
                        modifier = Modifier.weight(1f),
                        onPressUser = { openProfile(it) },
                        onLongPress = { messageForAction = it }
                    )
                    MessageInput(
                        modifier = Modifier.padding(dimens.grid.x4),
                        chat = chat,
                        replyingTo = message,
                        onMedia = { ctx.launch { media.show() } }
                    )
                }
            }
        }
    }
}

@IPreviews
@Composable
fun ChatChatPreview() {
    BotStacksChatContext {
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