package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.internal.state.BotStacksChatStore
import ai.botstacks.sdk.internal.ui.components.EmptyListView
import ai.botstacks.sdk.internal.ui.components.IACList
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** ChatList
 *
 * Renders a given list of [Chat] in an infinite scrolling list. Each chat will render a preview of
 * it using [ChatMessagePreview].
 *
 *
 * @param modifier the Modifier to be applied to this ChatList
 * @param header Optional header to display, fixed, at the top of the list. Normally this would be a
 * [Header].
 * @param emptyState Optional UI state for when there is no chats available. See [ai.botstacks.sdk.ui.theme.Assets] and [ai.botstacks.sdk.ui.theme.EmptyScreenConfig]
 * @param filter predicate to filter the chats that are loaded. This is generally done from search in [Header].
 * @param onChatClicked callback for when a [Chat] in the list is clicked.
 *
 */
@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = { },
    emptyState: @Composable () -> Unit = { EmptyListView(config = BotStacks.assets.emptyChats)  },
    filter: (Chat) -> Boolean = { true },
    onChatClicked: (Chat) -> Unit,
) {
    val items = BotStacksChatStore.current.chats
        .filter { chat ->
            filter(chat)
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