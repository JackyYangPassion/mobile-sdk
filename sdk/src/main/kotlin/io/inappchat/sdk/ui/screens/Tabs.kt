/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
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
    openChat: (Chat) -> Unit,
    openReplies: (Message) -> Unit,
    openProfile: (User) -> Unit,
    openCreateChat: () -> Unit,
    openCompose: () -> Unit,
    openSearch: () -> Unit,
    openFavorites: () -> Unit,
    openNotificationSettings: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(Tab.home) }
    var scrollToTop by remember {
        mutableStateOf(0)
    }
    val openTab = { tab: Tab ->
        if (selectedTab == tab)
            scrollToTop += 1
        else
            selectedTab = tab
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1.0f)) {
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
                    openCreateChat = openCreateChat,
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
        Row(modifier = Modifier
            .height(57.dp)
            .background(colors.softBackground)) {
            for (tab in Tab.values()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { openTab(tab) },
                    contentAlignment = Alignment.Center
                ) {
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
