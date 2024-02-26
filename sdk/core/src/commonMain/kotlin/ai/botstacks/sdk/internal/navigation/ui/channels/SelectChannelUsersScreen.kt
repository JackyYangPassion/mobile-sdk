package ai.botstacks.sdk.internal.navigation.ui.channels

import ai.botstacks.sdk.InviteUsersMutation
import ai.botstacks.sdk.internal.API
import ai.botstacks.sdk.internal.state.Upload
import ai.botstacks.sdk.internal.utils.bg
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.components.Header
import ai.botstacks.sdk.ui.components.HeaderDefaults
import ai.botstacks.sdk.ui.components.SelectChannelUsersView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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

            SelectChannelUsersView(
                selectedUsers = state.selections,
                onUserSelected = { state.addUser(it) },
                onUserRemoved = { state.removeUser(it) }
            )
        }
    }
}