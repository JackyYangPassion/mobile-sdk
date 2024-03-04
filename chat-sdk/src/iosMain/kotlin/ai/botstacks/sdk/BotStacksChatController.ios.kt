package ai.botstacks.sdk

import ai.botstacks.sdk.internal.navigation.BotstacksRouter
import ai.botstacks.sdk.internal.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.internal.navigation.screens.ChatListScreen
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.theme.Assets
import ai.botstacks.sdk.ui.theme.Colors
import ai.botstacks.sdk.ui.theme.Fonts
import ai.botstacks.sdk.ui.theme.ShapeDefinitions
import ai.botstacks.sdk.ui.theme.darkBotStacksColors
import ai.botstacks.sdk.ui.theme.lightBotStacksColors
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlin.experimental.ExperimentalObjCName

/**
 * Drop in Navigation Controller for a full end-to-end integration of the BotStacksSDK.
 *
 * All screen navigation is handled internally.
 *
 * @param onLogout Callback when a logout is confirmed and completes successfully. Utilize to send
 * users back to your own log in screen.
 */
@Composable
actual fun BotStacksChatController(onLogout: () -> Unit) {
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