/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import Theme
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.screens.*

@Composable
fun InAppChatUI(theme: Theme = Theme()) {
  val navController = rememberNavController()
  var tab by remember { mutableStateOf(Tab.home) }
  var scrollToTop by remember {
    mutableStateOf(0)
  }
  val openChat = { it: Room -> navController.navigate(it.path) }
  val openReplies = { it: Message ->
    navController.navigate(it.path)
  }
  val openProfile = { it: User -> navController.navigate(it.path) }
  val openInvite = { it: Group -> navController.navigate(it.invitePath) }
  val openEditGroup = { it: Group -> navController.navigate(it.editPath) }
  val tabs = @Composable {
    Tabs(
      selectedTab = tab,
      openChat = openChat,
      openReplies = openReplies,
      openProfile = { openProfile(it) },
      openCompose = {},
      openCreateGroup = { navController.navigate("group/new") },
      openSearch = { navController.navigate("search") },
      openFavorites = { navController.navigate("favorites") },
      openNotificationSettings = { navController.navigate("settings/notificications") },
      openTab = {
        if (it == tab) {
          scrollToTop += 1
        } else {
          tab = it
        }
      }
    )
  }
  val back = {
    navController.popBackStack()
    Unit
  }
  InAppChatContext(theme = theme) {
    NavHost(navController = navController, startDestination = "chats") {
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
          ProfileView(user = it, back = back, openChat = { navController.navigate(it.chatPath) })
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
          openEditGroup = openEditGroup,
          openReply = openReplies,
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
          openReply = openReplies,
          back = back
        )
      }
      composable(
        "group/{id}/edit",
        arguments = listOf(navArgument("id") { type = NavType.StringType })
      ) {
        CreateGroup(
          group = Group.get(it.arguments?.getString("id")!!),
          openInvite = openInvite,
          _back = back
        )
      }
      composable(
        "group/{id}/invite",
        arguments = listOf(navArgument("id") { type = NavType.StringType })
      ) {
        InviteView(
          group = Group.get(it.arguments?.getString("id")!!)!!,
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
          openReply = openReplies,
          back = back
        )
      }
      composable("groups/new") {
        CreateGroup(group = null, openInvite = openInvite, _back = back)
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
  }
}
