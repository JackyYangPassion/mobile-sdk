/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import Theme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.screens.Tabs

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
    NavHost(navController = navController, startDestination = "profile") {
        composable("chats") {
            tabs
        }
        composable("channels") { tabs }
        composable("threads") { tabs }
    }
}
