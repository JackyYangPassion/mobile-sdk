package ai.botstacks.sample

import ai.botstacks.sample.ui.theme.Example
import ai.botstacks.sample.ui.theme.Purple40
import ai.botstacks.sample.ui.theme.Purple80
import ai.botstacks.sample.ui.theme.Router
import ai.botstacks.sdk.BotStacksChatController
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.theme.darkBotStacksColors
import ai.botstacks.sdk.ui.theme.lightBotStacksColors
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                BotStacksThemeEngine {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        val openBotstacks = { navController.navigate("router") }
                        val openLogin = { navController.navigate("login") }

                        composable("splash") {
                            Splash(openChat = openBotstacks, openLogin = openLogin)
                        }
                        composable("login") {
                            Login(openBotstacks)
                        }

                        composable("router") {
                            Router(
                                onOpenExample = { navController.navigate(it.route) },
                                onLogout = { navController.navigate("login") }
                            )
                        }

                        composable(Example.Full.route) {
                            BotStacksChatController { navController.navigate("login") }
                        }
                    }
                }
            }
        }
    }
}