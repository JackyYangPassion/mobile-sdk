/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.IAC.theme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.Fn
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genG
import ai.botstacks.sdk.utils.random

@Composable
fun ChannelsView(
    scrollToTop: Int,
    search: Fn,
    openChat: (Chat) -> Unit,
    openCreateChat: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        PagerList(
            pager = BotStacksChatStore.current.network,
            topInset = HeaderHeight,
            bottomInset = 0.dp,
            scrollToTop = scrollToTop.toString(),
            empty = {
                EmptyListView(
                    config = theme.assets.emptyAllChannels,
                    cta = CTA(icon = null, text = "Create A Channel", to = openCreateChat)
                )
            }) { chat ->
            ChannelRow(chat = chat, onClick = {
                if (!chat._private || it.isMember) {
                    openChat(it)
                }
            })
        }
        Header(title = "All Channels", add = openCreateChat)
    }
}

@IPreviews
@Composable
fun EmptyChannelsViewPreview() {
    BotStacksChatStore.current.network.hasMore = false
    BotStacksChatContext {
        ChannelsView(
            scrollToTop = 0,
            search = { /*TODO*/ },
            openCreateChat = { /*TODO*/ },
            openChat = {})
    }
}

@IPreviews
@Composable
fun ChannelsViewPreview() {
    BotStacksChatStore.current.network.hasMore = false
    BotStacksChatStore.current.network.items.addAll(random(20, { genG() }))
    BotStacksChatContext {
        ChannelsView(
            scrollToTop = 0,
            search = { /*TODO*/ },
            openCreateChat = { /*TODO*/ },
            openChat = {})
    }
}