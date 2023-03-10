/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui

import Theme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.inappchat.sdk.ui.screens.Tabs

@Composable
fun InAppChatUI() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile") {
        composable("chats") { Tabs() }
    }
}
