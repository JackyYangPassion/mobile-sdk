/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.*

@Composable
fun ChatsView(
    scrollToTop: Int = 0,
    openChat: (Chat) -> Unit,
    openReplies: (Message) -> Unit,
    openAllChannels: () -> Unit,
    openContacts: () -> Unit,
    openSearch: () -> Unit,
    openCompose: () -> Unit,
    openProfile: (User) -> Unit
) {
    var list by remember {
        mutableStateOf(InAppChatStore.List.groups)
    }

    val cta = when (list) {
        InAppChatStore.List.dms -> CTA(icon = null, text = "Explore Channels", to = openAllChannels)
        else -> CTA(
            io.inappchat.sdk.R.drawable.paper_plane_tilt_fill,
            "Send a Message",
            openContacts
        )
    }
    val header = @Composable {
        Column {
            Header(title = "Messages", compose = openCompose, search = openSearch)
            ChatTabs(list = list, onSelect = { list = it })
        }
    }
    val empty = @Composable {
        EmptyListView(config = theme.assets.list(list), cta = cta)
    }
//        PagerList(
//            pager = Chats.current.messages,
//            header = header,
//            scrollToTop = scrollToTop.toString(),
//            empty = empty,
//            divider = true
//        ) {
//            RepliesView(message = it, onPress = openReplies, onPressUser = openProfile)
//        }
    val items =
        if (list == InAppChatStore.List.dms) InAppChatStore.current.dms else InAppChatStore.current.groups
    IACList(
        items = items,
        header = header,
        empty = empty,
        scrollToTop = scrollToTop.toString(),
        divider = true
    ) {
        ChatRow(chat = it, onClick = openChat)
    }
}

@IPreviews
@Composable
fun EmptyUserChatsViewPreview() {
    InAppChatContext {
        ChatsView(
            openChat = {},
            openReplies = {},
            openAllChannels = { /*TODO*/ },
            openContacts = { /*TODO*/ },
            openSearch = { /*TODO*/ },
            openCompose = { /*TODO*/ },
            openProfile = {}
        )
    }
}

@IPreviews
@Composable
fun ChatsViewPreview() {
//    Chats.current.users.items.addAll(random(10, { genUserChat() }))
//    Chats.current.chats.items.addAll(random(10, { genChatChat() }))
//    Chats.current.messages.items.addAll(random(10, { genRepliesMessage() }))
    InAppChatContext {
        ChatsView(
            openChat = {},
            openReplies = {},
            openAllChannels = { /*TODO*/ },
            openContacts = { /*TODO*/ },
            openSearch = { /*TODO*/ },
            openCompose = { /*TODO*/ },
            openProfile = {}
        )
    }
}

