package ai.botstacks.sdk.navigation.screens

import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.screens.ChatRoute
import ai.botstacks.sdk.ui.screens.ChatsView
import ai.botstacks.sdk.ui.screens.CreateChannelScreen
import ai.botstacks.sdk.ui.screens.FavoritesView
import ai.botstacks.sdk.ui.screens.ProfileView
import ai.botstacks.sdk.ui.screens.SelectChannelUsersScreen
import ai.botstacks.sdk.ui.views.CreateChannelState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.uniqueScreenKey

data object ChatListScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current
        ChatsView(
            openChat = { navigator.push(ChatScreen(it.id)) },
            onCreateChannel = { navigator.push(CreateChannelScreen) },
            editProfile = { navigator.push(EditProfileScreen) },
            openFavorites = { navigator.push(FavoriteMessagesScreen) },
            onConfirmedLogout = { BotStacksChat.logout() }
        )
    }
}

data class ChatScreen(val chatId: String) : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current
        ChatRoute(
            gid = chatId,
            back = { navigator.pop() },
            openEditChat = {},
            openInvite = {},
            openProfile = { navigator.push(UserDetailsScreen(it)) },
            openReply = {}
        )
    }
}

data object CreateChannelScreen : Screen {
    override val key = uniqueScreenKey

    val state = CreateChannelState()

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current

        CreateChannelScreen(
            state = state,
            onBackClicked = { navigator.pop() },
            onChannelCreated = {
                navigator.replaceAll(listOf(ChatListScreen, ChatScreen(it)))
            },
            onSelectUsers = { navigator.push(SelectUsersScreen(state)) }
        )
    }
}

private data class SelectUsersScreen(val state: CreateChannelState) : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current

        SelectChannelUsersScreen(
            state = state,
            onBackClicked = { navigator.pop() }
        )
    }
}

data object EditProfileScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current
        ProfileView(user = User.current!!, onBackClicked = { navigator.pop() })
    }
}

data class UserDetailsScreen(val user: User) : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current
        ProfileView(user = user, onBackClicked = { navigator.pop() })
    }
}

data object FavoriteMessagesScreen : Screen {
    override val key = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalPlatformNavigator.current

        FavoritesView(
            back = { navigator.pop() },
            openReplies = { },
            openProfile = { navigator.push(UserDetailsScreen(it)) },
            scrollToTop = 0
        )
    }
}


