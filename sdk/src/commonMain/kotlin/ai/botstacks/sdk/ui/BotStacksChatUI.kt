/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui


import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.navigation.LocalPlatformNavigator
import androidx.compose.runtime.Composable

@Composable
fun BotStacksChatRoutes(
    onLogout: () -> Unit
) {
    val navigator = LocalPlatformNavigator.current
    BotStacksChat.shared.onLogout = onLogout

//    val openReplies = { it: Message ->
//        navController.navigate(it.path)
//    }
//    val openProfile = { it: User -> navController.navigate(it.path) }
//    val openInvite = { it: Chat -> navController.navigate(it.invitePath) }
//    val openEditChat = { it: Chat -> navController.navigate(it.editPath) }

    val back = { navigator.pop() }
//    val tabs = @Composable {
//        Tabs(
//            openChat = openChat,
//            openReplies = openReplies,
//            openProfile = { openProfile(it) },
//            openCompose = {},
//            openCreateChat = { navController.navigate("chats/new") },
//            openSearch = { navController.navigate("search") },
//            openFavorites = { navController.navigate("favorites") },
//            openNotificationSettings = { navController.navigate("settings/notifications") },
//        )
//    }
//    val routes: NavGraphBuilder.() -> Unit = {
//        composable("chats") {
//            ChatsView(
//                openChat = openChat,
//                openCompose = { },
//                editProfile = { User.current?.let(openProfile) },
//                openFavorites = { navController.navigate("favorites") },
//                onConfirmedLogout = { BotStacksChat.logout() }
//            )
//        }
//        slideInOutComposable(
//            "user/{id}",
//            arguments = listOf(navArgument("id") { type = NavType.StringType }),
//        ) {
//            it.arguments?.getString("id")?.let { User.get(it) }?.let {
//                ProfileView(
//                    user = it,
//                    onBackClicked = back,
//                )
//            }
//        }
//        slideInOutComposable(
//            "user/{id}/chat",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        ) {
//            ChatRoute(
//                uid = it.arguments?.getString("id")!!,
//                openProfile = openProfile,
//                openInvite = openInvite,
//                openEditChat = openEditChat,
//                openReply = openReplies,
//                back = back
//            )
//        }
//        slideInOutComposable(
//            "chat/{id}",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        ) {
//            ChatRoute(
//                gid = it.arguments?.getString("id")!!,
//                openProfile = openProfile,
//                openInvite = openInvite,
//                openEditChat = openEditChat,
//                openReply = openReplies,
//                back = back
//            )
//        }
//        slideInOutComposable(
//            "chat/{id}/edit",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        ) {
//            CreateChat(
//                chat = Chat.get(it.arguments?.getString("id")!!),
//                openInvite = openInvite,
//                _back = back
//            )
//        }
//        slideInOutComposable(
//            "chat/{id}/invite",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        ) {
//            InviteView(
//                chat = Chat.get(it.arguments?.getString("id")!!)!!,
//                back = back,
//                openChat = openChat
//            )
//        }
//        slideInOutComposable(
//            "message/{id}",
//            arguments = listOf(navArgument("id") { type = NavType.StringType })
//        ) {
//            ChatRoute(
//                mid = it.arguments?.getString("id")!!,
//                openProfile = openProfile,
//                openInvite = openInvite,
//                openEditChat = openEditChat,
//                openReply = openReplies,
//                back = back
//            )
//        }
//        slideInOutComposable("chats/new") {
//            CreateChat(chat = null, openInvite = openInvite, _back = back)
//        }
//        slideInOutComposable("favorites") {
//            FavoritesView(
//                back = back,
//                openReplies = openReplies,
//                openProfile = openProfile,
//                scrollToTop = 0
//            )
//        }
//        composable("settings/notifications") {
//            NotificationSettingsView(back)
//        }
//    }

//    routes.invoke(navGraphBuilder)
}