/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.screens.ChatRoute
import ai.botstacks.sdk.ui.screens.CreateChat
import ai.botstacks.sdk.ui.screens.FavoritesView
import ai.botstacks.sdk.ui.screens.InviteView
import ai.botstacks.sdk.ui.screens.NotificationSettingsView
import ai.botstacks.sdk.ui.screens.ProfileView
import ai.botstacks.sdk.ui.screens.SearchView
import ai.botstacks.sdk.ui.screens.Tabs

fun BotStacksChatRoutes(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    onLogout: () -> Unit
) {
    BotStacksChat.shared.onLogout = onLogout
    val openChat = { it: Chat -> navController.navigate(it.path) }
    val openReplies = { it: Message ->
        navController.navigate(it.path)
    }
    val openProfile = { it: User -> navController.navigate(it.path) }
    val openInvite = { it: Chat -> navController.navigate(it.invitePath) }
    val openEditChat = { it: Chat -> navController.navigate(it.editPath) }

    val back = {
        navController.popBackStack()
        Unit
    }
    val tabs = @Composable {
        Tabs(
            openChat = openChat,
            openReplies = openReplies,
            openProfile = { openProfile(it) },
            openCompose = {},
            openCreateChat = { navController.navigate("chats/new") },
            openSearch = { navController.navigate("search") },
            openFavorites = { navController.navigate("favorites") },
            openNotificationSettings = { navController.navigate("settings/notifications") },
        )
    }
    val routes: NavGraphBuilder.() -> Unit = {
        composable("chats") {
            tabs()
        }
        composable("channels") { tabs() }
        composable("contacts") { tabs() }
        composable("settings") { tabs() }
        composable(
            "user/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            it.arguments?.getString("id")?.let { User.get(it) }?.let {
                ProfileView(
                    user = it,
                    back = back,
                    openChat = { navController.navigate(it.chatPath) })
            }
        }
        composable(
            "user/{id}/chat",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            ChatRoute(
                uid = it.arguments?.getString("id")!!,
                openProfile = openProfile,
                openInvite = openInvite,
                openEditChat = openEditChat,
                openReply = openReplies,
                back = back
            )
        }
        composable(
            "chat/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            ChatRoute(
                gid = it.arguments?.getString("id")!!,
                openProfile = openProfile,
                openInvite = openInvite,
                openEditChat = openEditChat,
                openReply = openReplies,
                back = back
            )
        }
        composable(
            "chat/{id}/edit",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            CreateChat(
                chat = Chat.get(it.arguments?.getString("id")!!),
                openInvite = openInvite,
                _back = back
            )
        }
        composable(
            "chat/{id}/invite",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            InviteView(
                chat = Chat.get(it.arguments?.getString("id")!!)!!,
                back = back,
                openChat = openChat
            )
        }
        composable(
            "message/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            ChatRoute(
                mid = it.arguments?.getString("id")!!,
                openProfile = openProfile,
                openInvite = openInvite,
                openEditChat = openEditChat,
                openReply = openReplies,
                back = back
            )
        }
        composable("chats/new") {
            CreateChat(chat = null, openInvite = openInvite, _back = back)
        }
        composable("favorites") {
            FavoritesView(
                back = back,
                openReplies = openReplies,
                openProfile = openProfile,
                scrollToTop = 0
            )
        }
        composable("settings/notifications") {
            NotificationSettingsView(back)
        }
        composable("search") {
            SearchView(back)
        }
    }

    routes.invoke(navGraphBuilder)
}