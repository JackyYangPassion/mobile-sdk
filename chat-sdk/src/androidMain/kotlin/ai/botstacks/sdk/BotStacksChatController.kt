package ai.botstacks.sdk

import ai.botstacks.sdk.internal.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.internal.navigation.screens.ChatListScreen
import ai.botstacks.sdk.internal.navigation.BotstacksRouter
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

/**
 * Drop in Navigation Controller for a full end-to-end integration of the BotStacksSDK.
 *
 * All screen navigation is handled internally.
 *
 * @param onLogout Callback when a logout is confirmed and completes successfully. Utilize to send
 * users back to your own log in screen.
 */
@Composable
fun BotStacksChatController(
    onLogout: () -> Unit
) {
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
                CompositionLocalProvider(LocalContentColor provides BotStacks.colorScheme.onBackground) {
                    SlideTransition(navigator)
                }
            }
        }
    }
}