/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

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
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.ui.views.Badge
import androidx.compose.ui.graphics.painter.Painter

sealed interface Tab {
    val route: String
    val icon: Painter
        @Composable get() = Drawables.Empty

    data object Home: Tab {
        override val route: String = "chats"
        override val icon: Painter
        @Composable get() = Drawables.ChatTextFilled
    }
    data object Channels: Tab {
        override val route: String = "channels"
        override val icon: Painter
            @Composable get() = Drawables.TelevisionFilled
    }
    data object Contacts: Tab {
        override val route: String = "contacts"
        override val icon: Painter
            @Composable get() = Drawables.AddressBookFilled
    }
    data object Settings: Tab {
        override val route: String = "settings"
        override val icon: Painter
            @Composable get() = Drawables.UserCircleFilled
    }

    companion object {
        val entries: List<Tab> = listOf(Home, Channels, Contacts, Settings)
    }
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
    var selectedTab by remember { mutableStateOf<Tab>(Tab.Home) }
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
                Tab.Home -> ChatsView(
                    openChat = openChat,
                    openReplies = openReplies,
                    openAllChannels = { openTab(Tab.Channels) },
                    openContacts = { openTab(Tab.Contacts) },
                    openSearch = openSearch,
                    openCompose = openCompose,
                    openProfile = openProfile,
                    scrollToTop = scrollToTop
                )

                Tab.Channels -> ChannelsView(
                    scrollToTop = scrollToTop,
                    search = openSearch,
                    openCreateChat = openCreateChat,
                    openChat = openChat,
                )

                Tab.Contacts -> ContactsView(
                    scrollToTop = scrollToTop, openProfile = openProfile
                )

                Tab.Settings -> MyProfile(
                    openProfile = { openProfile(User.current!!) },
                    openNotificationSettings = openNotificationSettings,
                    openFavorites = openFavorites
                )
            }
        }
        Row(modifier = Modifier
            .background(colorScheme.softBackground)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(57.dp)
        ) {
            for (tab in Tab.entries) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { openTab(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    if (tab == Tab.Home) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                painter = tab.icon,
                                contentDescription = tab.route,
                                tint = if (selectedTab == tab) colorScheme.primary else colorScheme.caption,
                                modifier = Modifier.size(45.dp)
                            )
                            val count = BotStacksChatStore.current.totalCount
                            if (count > 0) {
                                Badge(count = count)
                            }
                        }
                    } else {
                        Icon(
                            painter = tab.icon,
                            contentDescription = tab.route,
                            tint = if (selectedTab == tab) colorScheme.primary else colorScheme.caption,
                            modifier = Modifier.size(45.dp)
                        )
                    }

                }
            }
        }
    }
}
