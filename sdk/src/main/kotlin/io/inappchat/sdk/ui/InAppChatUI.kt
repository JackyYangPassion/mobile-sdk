/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.inappchat.sdk.state.*
import io.inappchat.sdk.ui.screens.*
import io.inappchat.sdk.ui.theme.InAppChatTheme

@Composable
fun InAppChatUI() {
    val navController = rememberNavController()
    val openChat = { it: Room -> navController.navigate(it.path) }
    val openReplies = { it: Message ->
        navController.navigate(it.path)
    }
    val openChannels = { navController.navigate("channels") }
    val openContacts = { navController.navigate("contacts") }
    val openSettings = { navController.navigate("settings") }
    val openProfile = { it: User -> navController.navigate(it.path) }
    val openUserChat = { it: User -> navController.navigate(it.chatPath) }
    val openInvite = { it: Group -> navController.navigate(it.invitePath) }
    val openEditGroup = { it: Group -> navController.navigate(it.editPath) }
    val openGroupChat = { it: Group -> navController.navigate(it.path) }
    val tabs = Tabs(
        openChat = openChat,
        openReplies = openReplies,
        openChannels = openChannels,
        openContacts = openContacts,
        openSettings = openSettings,
        openProfile = { openProfile(User.current!!) },
        openChats = { navController.navigate("chats") },
        openCurrentChannels = { navController.navigate("channels") },
        openThreads = { navController.navigate("threads") },
        list = Chats.List.forRoute(navController.currentDestination?.route)
    )
    val back = {
        navController.popBackStack()
        Unit
    }
    InAppChatTheme {
        NavHost(navController = navController, startDestination = "chats") {
            composable("chats") {
                tabs
            }
            composable("channels") { tabs }
            composable("threads") { tabs }
            composable(
                "user/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                UserView(it.arguments?.getString("id")!!, openChat = openUserChat, back = back)
            }
            composable(
                "user/{id}/chat",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                ChatRoute(
                    uid = it.arguments?.getString("id")!!,
                    openProfile = openProfile,
                    openInvite = openInvite,
                    openEditGroup = openEditGroup,
                    back = back
                )
            }
            composable(
                "group/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                ChatRoute(
                    gid = it.arguments?.getString("id")!!,
                    openProfile = openProfile,
                    openInvite = openInvite,
                    openEditGroup = openEditGroup,
                    back = back
                )
            }
            composable(
                "group/{id}/edit",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                CreateGroup(
                    group = Group.get(it.arguments?.getString("id")!!),
                    openChat = openGroupChat,
                    _back = back
                )
            }
            composable(
                "group/{id}/invite",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                InviteView(
                    group = Group.get(it.arguments?.getString("id")!!),
                    openGroup = {
                        navController.navigate(it.path) {
                            navController.popBackStack()
                            navController.popBackStack()
                        }
                    },
                    back = back
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
                    openEditGroup = openEditGroup,
                    back = back
                )
            }
            composable("groups/new") {
                CreateGroup(group = null, openChat = openGroupChat, _back = back)
            }
            composable("favorites") {
                FavoritesView(
                    back = back
                )
            }
            composable("settings/notifications") {
                NotificationSettingsView(back)
            }
            composable("search") {
                SearchView(back)
            }
        }
    }
}
