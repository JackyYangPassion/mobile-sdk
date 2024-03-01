package ai.botstacks.sdk

import ai.botstacks.sdk.internal.navigation.BotstacksRouter
import ai.botstacks.sdk.internal.navigation.LocalPlatformNavigator
import ai.botstacks.sdk.internal.navigation.screens.ChatListScreen
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.theme.Assets
import ai.botstacks.sdk.ui.theme.Colors
import ai.botstacks.sdk.ui.theme.Fonts
import ai.botstacks.sdk.ui.theme.ShapeDefinitions
import ai.botstacks.sdk.ui.theme.darkBotStacksColors
import ai.botstacks.sdk.ui.theme.lightBotStacksColors
import androidx.compose.foundation.layout.Box
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
@OptIn(ExperimentalObjCName::class)
@ObjCName("BotStacksChatController")
fun BotStacksChatController(onLogout: () -> Unit) = BotStacksChatController(
    useDarkTheme = false,
    lightColorScheme = null,
    darkColorScheme = null,
    shapes = null,
    assets = null,
    fonts = null,
    onLogout = onLogout
)

@OptIn(ExperimentalObjCName::class)
@ObjCName("BotStacksChatController")
fun BotStacksChatController(
    useDarkTheme: Boolean = false,
    lightColorScheme: Colors? = null,
    darkColorScheme: Colors? = null,
    shapes: ShapeDefinitions? = null,
    assets: Assets? = null,
    fonts: Fonts? = null,
    onLogout: () -> Unit
) = ComposeUIViewController {
    BotStacksChat.shared.onLogout = onLogout

    BotStacksThemeEngine(
        useDarkTheme,
        lightColorScheme ?: lightBotStacksColors(),
        darkColorScheme ?: darkBotStacksColors(),
        shapes,
        assets,
        fonts,
    ) {
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
}