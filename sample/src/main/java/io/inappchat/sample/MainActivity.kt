package ai.botstacks.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.BotStacksChatRoutes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                BotStacksChatContext {
                    NavHost(navController = navController, startDestination = "splash") {
                        val openChat = {
                            navController.navigate("chats")
                        }
                        composable("splash") {
                            Splash(openChat = openChat, openLogin = {
                                navController.navigate("login")
                            })
                        }
                        composable("login") {
                            Login(openChat) {
                                navController.navigate("register")
                            }
                        }
                        composable("register") {
                            Register(openChat = openChat) {
                                navController.navigate("login")
                            }
                        }
                        BotStacksChatRoutes(
                            navController = navController,
                            navGraphBuilder = this,
                            onLogout = {
                                navController.navigate("login")
                            })
                    }
                }
            }
        }
    }
}