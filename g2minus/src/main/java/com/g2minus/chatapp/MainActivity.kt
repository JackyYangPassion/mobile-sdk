package com.g2minus.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.g2minus.chatapp.ui.theme.BotStacksChatTheme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.BotStacksChatRoutes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val g2mTheme = remember {
                makeTheme(this)
            }
            BotStacksChatContext(g2mTheme) {
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
                        Login(openChat, openCreateProfile = {
                            navController.navigate("create-profile")
                        })
                    }
                    composable("create-profile") {
                        CreateProfile {
                            navController.navigate("sign-in")
                        }
                    }
                    composable("sign-in") {
                        SignIn(openChat)
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BotStacksChatTheme {
        Greeting("Android")
    }
}