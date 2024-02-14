package ai.botstacks.sdk

import ai.botstacks.sdk.navigation.BotstacksRouter
import ai.botstacks.sdk.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.navigation.screens.ChatListScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

@Composable
fun BotStacksNavigation(onLogout: () -> Unit) {
    BotStacksChat.shared.onLogout = onLogout

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