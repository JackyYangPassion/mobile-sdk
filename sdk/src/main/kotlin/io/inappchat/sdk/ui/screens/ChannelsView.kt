/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.Fn
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genG
import io.inappchat.sdk.utils.random

@Composable
fun ChannelsView(
    scrollToTop: Int,
    search: Fn,
    openChat: (Chat) -> Unit,
    openCreateChat: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        PagerList(
            pager = InAppChatStore.current.network,
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
        Header(title = "All Channels", search = search, add = openCreateChat)
    }
}

@IPreviews
@Composable
fun EmptyChannelsViewPreview() {
    InAppChatStore.current.network.hasMore = false
    InAppChatContext {
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
    InAppChatStore.current.network.hasMore = false
    InAppChatStore.current.network.items.addAll(random(20, { genG() }))
    InAppChatContext {
        ChannelsView(
            scrollToTop = 0,
            search = { /*TODO*/ },
            openCreateChat = { /*TODO*/ },
            openChat = {})
    }
}