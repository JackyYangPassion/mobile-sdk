/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.inappchat.sdk.actions.send
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts


@Composable
fun MessageList(
    chat: Chat,
    modifier: Modifier,
    onPressUser: (User) -> Unit,
    onLongPress: (Message) -> Unit
) {
    PagerList(
        prefix = chat.sending,
        pager = chat,
        modifier = modifier,
        scrollToTop = chat.sending.firstOrNull()?.id ?: chat.items.firstOrNull()?.id,
        invert = true,
        empty = { Text(text = "No messages yet.", iac = fonts.title3) }) {
        MessageView(message = it, onPressUser = onPressUser, onLongPress = onLongPress)
    }
}