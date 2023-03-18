/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.Fn
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genG
import io.inappchat.sdk.utils.random

@Composable
fun ChannelsView(
  scrollToTop: Long,
  search: Fn,
  createGroup: Fn,
  openChat: (Group) -> Unit,
  openCreateGroup: () -> Unit
) {
  val listState = rememberLazyListState()
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
    PagerList(pager = Chats.current.network, topInset = HeaderHeight, bottomInset = 0.dp, empty = {
      EmptyListView(
        config = theme.assets.emptyAllChannels,
        cta = CTA(icon = null, text = "Create A Channel", to = openCreateGroup)
      )
    }) { group ->
      ChannelRow(group = group, onClick = {
        if (it.isMember) openChat(it)
      })
    }
    Header(title = "All Channels", search = search, add = createGroup)
  }
  LaunchedEffect(key1 = scrollToTop, block = {
    if (scrollToTop > 0) {
      listState.animateScrollToItem(0)
    }
  })
}

@IPreviews
@Composable
fun EmptyChannelsViewPreview() {
  Chats.current.network.hasMore = false
  InAppChatContext {
    ChannelsView(
      scrollToTop = 0,
      search = { /*TODO*/ },
      createGroup = { /*TODO*/ },
      openChat = {}) {

    }
  }
}

@IPreviews
@Composable
fun ChannelsViewPreview() {
  Chats.current.network.hasMore = false
  Chats.current.network.items.addAll(random(20, { genG() }))
  InAppChatContext {
    ChannelsView(
      scrollToTop = 0,
      search = { /*TODO*/ },
      createGroup = { /*TODO*/ },
      openChat = {}) {

    }
  }
}