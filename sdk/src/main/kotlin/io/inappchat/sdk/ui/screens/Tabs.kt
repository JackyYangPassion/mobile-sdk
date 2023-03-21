/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User

enum class Tab(@DrawableRes val icon: Int) {
  home(R.drawable.chat_text_fill),
  channels(R.drawable.television_fill),
  contacts(R.drawable.address_book_fill),
  settings(R.drawable.user_circle_fill)
}

@Composable
fun Tabs(
  openChat: (Room) -> Unit,
  openReplies: (Message) -> Unit,
  openAllChannels: () -> Unit,
  openContacts: () -> Unit,
  openProfile: (User) -> Unit,
  openCreateGroup: () -> Unit,
  openCompose: () -> Unit,
  openSearch: () -> Unit,
  openFavorites: () -> Unit,
  openNotificationSettings: () -> Unit,
  scrollToTop: Int = 0,
  tab: Tab
) {
  Column(modifier = Modifier.fillMaxSize()) {
    when (tab) {
      Tab.home -> ChatsView(
        openChat = openChat,
        openReplies = openReplies,
        openAllChannels = openAllChannels,
        openContacts = openContacts,
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
  }
}
