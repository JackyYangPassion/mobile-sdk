/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User

@Composable
fun ChatRoom(
    room: Room,
    message: Message? = null,
    openProfile: (User) -> Unit,
    openInvite: (Group) -> Unit,
    back: () -> Unit
) {

}