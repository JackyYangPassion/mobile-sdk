/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.theme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.*

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
        mutableStateOf(BotStacksChatStore.List.groups)
    }

    val cta = when (list) {
        BotStacksChatStore.List.dms -> CTA(icon = null, text = "Explore Channels", to = openAllChannels)
        else -> CTA(
            Drawables.PaperPlaneTiltFilled,
            "Send a Message",
            openContacts
        )
    }
    val header = @Composable {
        Column {
            Header(title = "Chats")
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
        if (list == BotStacksChatStore.List.dms) BotStacksChatStore.current.dms else BotStacksChatStore.current.groups
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
    BotStacksChatContext {
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
    BotStacksChatContext {
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

