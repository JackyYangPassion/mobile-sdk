/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.User

@Composable
fun UserView(id: String = User.current!!.id, openChat: (User) -> Unit, back: () -> Unit) {

}