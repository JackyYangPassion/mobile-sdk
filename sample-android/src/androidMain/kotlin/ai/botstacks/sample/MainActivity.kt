package ai.botstacks.sample

import ai.botstacks.sdk.BotStacksNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.botstacks.sdk.ui.BotStacksChatContext
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            BotStacksChatContext {
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
                        BotStacksNavigation { navController.navigate("login") }
                    }
                }
            }
        }
    }
}