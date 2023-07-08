/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.state.usernames
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genRepliesMessage

@Composable
fun RepliesView(
        message: Message,
        modifier: Modifier = Modifier,
        onPress: (Message) -> Unit,
        onPressUser: (User) -> Unit
) {
    Column {
        Column(
                modifier = modifier
                        .clickable { onPress(message) }
                        .padding(16.dp, 12.dp, 0.dp, 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "#${message.chat.name ?: ""}", iac = fonts.title3, color = colors.text)
            Text(
                    text = message.chat.members.map { it.user }.usernames() ,
                    iac = fonts.body,
                    color = colors.caption
            )
            for (message in message.replies.items) {
                MessageView(message = message, onPressUser = onPressUser)
            }
            Space(24f)
        }
        Spacer(
                modifier = Modifier
                        .background(colors.bubble.copy(alpha = 0.5f))
                        .height(20.dp)
                        .fillMaxWidth()
        )
    }
}


@IPreviews
@Composable
fun RepliesViewPreview() {
    InAppChatContext {
        RepliesView(message = genRepliesMessage(), onPress = {}, onPressUser = {})
    }
}