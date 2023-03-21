/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors

enum class Tab(val route: String, @DrawableRes val icon: Int) {
  home("chats", R.drawable.chat_text_fill),
  channels("channels", R.drawable.television_fill),
  contacts("contacts", R.drawable.address_book_fill),
  settings("settings", R.drawable.user_circle_fill)
}

@Composable
fun Tabs(
  openChat: (Room) -> Unit,
  openReplies: (Message) -> Unit,
  openTab: (Tab) -> Unit,
  openProfile: (User) -> Unit,
  openCreateGroup: () -> Unit,
  openCompose: () -> Unit,
  openSearch: () -> Unit,
  openFavorites: () -> Unit,
  openNotificationSettings: () -> Unit,
  scrollToTop: Int = 0,
  selectedTab: Tab
) {
  Column(modifier = Modifier.fillMaxSize()) {
    when (selectedTab) {
      Tab.home -> ChatsView(
        openChat = openChat,
        openReplies = openReplies,
        openAllChannels = { openTab(Tab.channels) },
        openContacts = { openTab(Tab.contacts) },
        openSearch = openSearch,
        openCompose = openCompose,
        openProfile = openProfile,
        scrollToTop = scrollToTop
      )
      Tab.channels -> ChannelsView(
        scrollToTop = scrollToTop,
        search = openSearch,
        openCreateGroup = openCreateGroup,
        openChat = openChat,
      )
      Tab.contacts -> ContactsView(
        scrollToTop = scrollToTop, openProfile = openProfile
      )
      Tab.settings -> MyProfile(
        openProfile = { openProfile(User.current!!) },
        openNotificationSettings = openNotificationSettings,
        openFavorites = openFavorites
      )
    }
    Row(modifier = Modifier.height(57.dp)) {
      for (tab in Tab.values()) {
        Box(modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
          .clickable { openTab(tab) }) {
          Icon(
            painter = painterResource(id = tab.icon),
            contentDescription = tab.route,
            tint = if (selectedTab == tab) colors.primary else colors.caption,
            modifier = Modifier.size(45.dp)
          )
        }
      }
    }
  }
}
