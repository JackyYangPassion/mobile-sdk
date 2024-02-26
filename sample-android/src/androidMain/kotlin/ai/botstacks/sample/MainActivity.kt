package ai.botstacks.sample

import ai.botstacks.sample.ui.theme.Purple40
import ai.botstacks.sample.ui.theme.Purple80
import ai.botstacks.sample.ui.theme.Typography
import ai.botstacks.sdk.BotStacksChatController
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.theme.FontStyle
import ai.botstacks.sdk.ui.theme.darkBotStacksColors
import ai.botstacks.sdk.ui.theme.botstacksFonts
import ai.botstacks.sdk.ui.theme.lightBotStacksColors
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BotStacksThemeEngine(
                useDarkTheme = isSystemInDarkTheme(),
                lightColorScheme = lightBotStacksColors(
                    primary = Purple40,
                    onPrimary = Color.White,
                ),
                darkColorScheme = darkBotStacksColors(
                    primary = Purple80,
                    onPrimary = Color.Black
                ),
                fonts = with(Typography.bodyLarge) {
                    botstacksFonts(
                        body1 = FontStyle(
                            size = fontSize,
                        )
                    )
                }
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    val openBotstacks = { navController.navigate("botstacks") }
                    val openLogin = { navController.navigate("login") }

                    composable("splash") {
                        Splash(openChat = openBotstacks, openLogin = openLogin)
                    }
                    composable("login") {
                        Login(openBotstacks) {

                        }
                    }
                    composable("botstacks") {
                        BotStacksChatController { navController.navigate("login") }
                    }
                }
            }
        }
    }
}