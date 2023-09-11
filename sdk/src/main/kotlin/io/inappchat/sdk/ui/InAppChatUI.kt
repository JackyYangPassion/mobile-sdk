/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.screens.ChatRoute
import io.inappchat.sdk.ui.screens.CreateChat
import io.inappchat.sdk.ui.screens.FavoritesView
import io.inappchat.sdk.ui.screens.InviteView
import io.inappchat.sdk.ui.screens.NotificationSettingsView
import io.inappchat.sdk.ui.screens.ProfileView
import io.inappchat.sdk.ui.screens.SearchView
import io.inappchat.sdk.ui.screens.Tabs

fun InAppChatRoutes(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    onLogout: () -> Unit
) {
    InAppChat.shared.onLogout = onLogout
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