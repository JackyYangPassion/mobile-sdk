package io.inappchat.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.inappchat.sample.ui.theme.InappchatTheme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.InAppChatRoutes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme {
                InAppChatContext {
                    NavHost(navController = navController, startDestination = "splash") {
                        val openChat = {
                            navController.navigate("chats") {
//                                popUpTo("splash") {
//                                    inclusive = true
//                                }
                            }
                        }
                        composable("splash") {
                            Splash(openChat = openChat, openLogin = {
                                navController.navigate("login") {
//                                    popUpTo("splash") {
//                                        inclusive = true
//                                    }
                                }
                            })
                        }
                        composable("login") {
                            Login(openChat)
                        }
                        InAppChatRoutes(navController = navController, navGraphBuilder = this)
                    }
                }
            }
        }
    }
}