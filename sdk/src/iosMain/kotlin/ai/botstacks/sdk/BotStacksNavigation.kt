package ai.botstacks.sdk

import ai.botstacks.sdk.navigation.BotstacksRouter
import ai.botstacks.sdk.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.navigation.screens.ChatListScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

fun BotStacksNavigation() = ComposeUIViewController {
    BotStacksChat.shared.onLogout = {  }

    BotstacksRouter {
        Navigator(ChatListScreen) { navigator ->
            val platformNavigator = LocalPlatformNavigator.current
            LaunchedEffect(navigator.lastItem) {
                // update global navigator for platform access to support push/pop from a single
                // navigator current
                platformNavigator.screensNavigator = navigator
            }

            Box(modifier = Modifier) {
                SlideTransition(navigator)
            }
        }
    }
}