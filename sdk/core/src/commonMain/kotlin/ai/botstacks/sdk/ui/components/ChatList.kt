package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.internal.state.BotStacksChatStore
import ai.botstacks.sdk.internal.ui.components.IACList
import ai.botstacks.sdk.state.Chat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    emptyState: @Composable () -> Unit,
    filter: (Chat) -> Boolean = { true },
    onChatClicked: (Chat) -> Unit,
) {
    val items = BotStacksChatStore.current.chats
        .filter { chat -> filter(chat)
        }.sortedByDescending { it.latest?.createdAt }.distinctBy { it.id }

    IACList(
        modifier = modifier,
        items = items,
        header = header,
        empty = emptyState,
    ) {
        ChatMessagePreview(chat = it, onClick = { onChatClicked(it) })
    }
}