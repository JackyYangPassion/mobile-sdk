/*
 * Copyright (c) 2023.
 */

@file:OptIn(ExperimentalFoundationApi::class)

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.AlertActionStyle
import ai.botstacks.sdk.ui.components.BotStacksAlertDialog
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.forEachTextValue
import androidx.compose.material.AlertDialog
import com.mohamedrejeb.calf.ui.dialog.AdaptiveAlertDialog

@Composable
fun ChatsView(
    scrollToTop: Int = 0,
    openChat: (Chat) -> Unit,
    openCompose: () -> Unit,
    editProfile: () -> Unit,
    openFavorites: () -> Unit,
    onConfirmedLogout: () -> Unit,
) {
    var logoutRequested by remember {
        mutableStateOf(false)
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    val headerState = rememberHeaderState(
        isSearchVisible = true,
    )

    val header = @Composable {
        Column {
            Header(
                icon = { HeaderDefaults.Logo() },
                state = headerState,
                onCompose = {},
                onBackClick = {
                    headerState.searchActive = false
                }.takeIf { headerState.searchActive },
                menu = {
                    label(onClick = openFavorites) {
                        Text(text = "Favorite Messages", fontStyle = BotStacks.fonts.button2)
                    }
                    label(onClick = editProfile) {
                        Text(text = "Edit Profile", fontStyle = BotStacks.fonts.button2)
                    }
                    label(onClick = { logoutRequested = true }) {
                        Text(
                            text = "Logout",
                            fontStyle = BotStacks.fonts.button2,
                            color = BotStacks.colorScheme.error
                        )
                    }
                }
            )
        }
    }

    AnimatedVisibility(logoutRequested) {
        BotStacksAlertDialog(
            title = "Log out",
            message = "Are you sure you want to log out?",
            buttons = {
                button(
                    onClick = { logoutRequested = false },
                    title = "Cancel",
                    style = AlertActionStyle.Cancel
                )
                button(
                    onClick = {
                        logoutRequested = false
                        onConfirmedLogout()
                    },
                    title = "Log out",
                    style = AlertActionStyle.Destructive
                )
            },
            onDismissRequest = { logoutRequested = false }
        )
    }


    LaunchedEffect(Unit) {
        headerState.searchQuery.forEachTextValue {
            searchQuery = it.toString()
        }
    }


    val items = BotStacksChatStore.current.chats
        .filter { chat ->
            if (searchQuery.isEmpty()) return@filter true
            else chat.items.any {
                it.msg.lowercase()
                    .contains(searchQuery.lowercase()) || it.user.displayNameFb.lowercase()
                    .contains(searchQuery.lowercase())
            } || chat.name.orEmpty().lowercase().contains(searchQuery.lowercase())
        }

    IACList(
        items = items,
        header = header,
        empty = @Composable { },
        scrollToTop = scrollToTop.toString(),
        divider = true
    ) {
        ChatRow(chat = it, onClick = openChat)
    }
}

@IPreviews
@Composable
fun EmptyUserChatsViewPreview() {
    BotStacksChatContext {
        ChatsView(
            openChat = {},
            openCompose = { /*TODO*/ },
            editProfile = {},
            openFavorites = {},
            onConfirmedLogout = {}
        )
    }
}

@IPreviews
@Composable
fun ChatsViewPreview() {
//    Chats.current.users.items.addAll(random(10, { genUserChat() }))
//    Chats.current.chats.items.addAll(random(10, { genChatChat() }))
//    Chats.current.messages.items.addAll(random(10, { genRepliesMessage() }))
    BotStacksChatContext {
        ChatsView(
            openChat = {},
            openCompose = { /*TODO*/ },
            editProfile = {},
            openFavorites = {},
            onConfirmedLogout = {}
        )
    }
}

