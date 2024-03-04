package ai.botstacks.sample.kmp

import ai.botstacks.sample.kmp.navigation.AppNavigation
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import androidx.compose.runtime.Composable

@Composable
fun App() {
    BotStacksThemeEngine {
        AppNavigation()
    }
}