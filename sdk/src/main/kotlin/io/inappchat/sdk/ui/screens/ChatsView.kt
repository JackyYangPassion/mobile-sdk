/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room

@Composable
fun ChatsView(
    openChat: (Room) -> Unit,
    openReplies: (Message) -> Unit,
    openChannels: () -> Unit,
    openContacts: () -> Unit
) {
    TODO("Not yet implemented")
}
