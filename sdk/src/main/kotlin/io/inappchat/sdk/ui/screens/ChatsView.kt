/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.*

@Composable
fun ChatsView(
  scrollToTop: Int = 0,
  openChat: (Room) -> Unit,
  openReplies: (Message) -> Unit,
  openAllChannels: () -> Unit,
  openContacts: () -> Unit,
  openSearch: () -> Unit,
  openCompose: () -> Unit,
  openProfile: (User) -> Unit
) {
  var list by remember {
    mutableStateOf(Chats.List.users)
  }

  val cta = when (list) {
    Chats.List.users -> CTA(icon = null, text = "Explore Channels", to = openAllChannels)
    else -> CTA(io.inappchat.sdk.R.drawable.paper_plane_tilt_fill, "Send a Message", openContacts)
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
  if (list == Chats.List.threads) {
    PagerList(
      pager = Chats.current.messages,
      header = header,
      scrollToTop = scrollToTop.toString(),
      empty = empty,
      divider = true
    ) {
      RepliesView(message = it, onPress = openReplies, onPressUser = openProfile)
    }
  } else {
    val pager = if (list == Chats.List.users) Chats.current.users else Chats.current.groups
    PagerList(
      pager = pager,
      header = header,
      empty = empty,
      scrollToTop = scrollToTop.toString(),
      divider = true
    ) {
      ThreadRow(thread = it, onClick = openChat)
    }
  }
}

@IPreviews
@Composable
fun EmptyUserChatsViewPreview() {
  Chats.current.users.hasMore = false
  Chats.current.groups.hasMore = false
  Chats.current.messages.hasMore = false
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
  Chats.current.users.items.addAll(random(10, { genUserRoom() }))
  Chats.current.groups.items.addAll(random(10, { genGroupRoom() }))
  Chats.current.messages.items.addAll(random(10, { genRepliesMessage() }))
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

