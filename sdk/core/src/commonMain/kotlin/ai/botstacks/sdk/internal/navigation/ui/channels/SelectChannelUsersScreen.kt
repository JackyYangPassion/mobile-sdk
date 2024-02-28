package ai.botstacks.sdk.internal.navigation.ui.channels

import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.components.Header
import ai.botstacks.sdk.ui.components.HeaderDefaults
import ai.botstacks.sdk.ui.components.SelectChannelUsersConnectingView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier

@Stable
class ChannelUserSelectionState(
    var selections: SnapshotStateList<User> = mutableStateListOf()
) {
    constructor(chat: Chat) : this(chat.members.map { it.user }.toMutableStateList())

    fun addUser(user: User) {
        selections = selections.apply { add(user) }
    }

    fun removeUser(user: User) {
        selections = selections.apply { remove(user) }
    }
}

@Composable
internal fun SelectChannelUsersScreen(
    state: ChannelUserSelectionState,
    onBackClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(
                title = "Add members",
                onBackClicked = onBackClicked,
                endAction = {
                    HeaderDefaults.SaveAction {
                        onBackClicked()
                    }
                }
            )

            SelectChannelUsersConnectingView(
                selectedUsers = state.selections,
                onUserSelected = { state.addUser(it) },
                onUserRemoved = { state.removeUser(it) }
            )
        }
    }
}