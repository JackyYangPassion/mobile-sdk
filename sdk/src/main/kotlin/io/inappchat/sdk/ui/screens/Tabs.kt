/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room

@Composable
fun Tabs(
    openChat: (Room) -> Unit,
    openReplies: (Message) -> Unit,
    openChannels: () -> Unit,
    openContacts: () -> Unit,
    openSettings: () -> Unit,
    openProfile: () -> Unit,
    openChats: () -> Unit,
    openCurrentChannels: () -> Unit,
    openThreads: () -> Unit,
    list: Chats.List
) {

}
