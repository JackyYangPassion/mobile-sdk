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
import com.g2minus.chatapp.ui.theme.InappchatTheme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.InAppChatRoutes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val g2mTheme = remember {
                makeTheme(this)
            }
            InAppChatContext(g2mTheme) {
                NavHost(navController = navController, startDestination = "splash") {
                    val openChat = {
                        navController.navigate("chats") {
                            popUpTo("splash") {
                                inclusive = true
                            }
                        }
                    }
                    composable("splash") {
                        Splash(openChat = openChat, openLogin = {
                            navController.navigate("login") {
                                popUpTo("splash") {
                                    inclusive = true
                                }
                            }
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
                    InAppChatRoutes(navController = navController, navGraphBuilder = this)
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
    InappchatTheme {
        Greeting("Android")
    }
}