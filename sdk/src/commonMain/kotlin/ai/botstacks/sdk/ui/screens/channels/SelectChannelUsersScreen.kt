package ai.botstacks.sdk.ui.screens.channels

import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.HeaderDefaults
import ai.botstacks.sdk.ui.views.SelectChannelUsersView
import ai.botstacks.sdk.utils.log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun SelectChannelUsersScreen(
    selections: List<User>,
    onUsersSelected: (List<User>) -> Unit,
    onBackClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        var selectedUsers = remember(selections) {
            mutableStateListOf<User>().apply {
                addAll(selections)
            }
        }

        Column {
            Header(
                title = "Add members",
                onBackClicked = onBackClicked,
                endAction = {
                    HeaderDefaults.SaveAction {
                        log("count=${selectedUsers.count()}")
                        onUsersSelected(selectedUsers)
                        onBackClicked()
                    }
                }
            )

            SelectChannelUsersView(
                selectedUsers = selectedUsers,
                onUserSelected = { selectedUsers = selectedUsers.apply { add(it) } },
                onUserRemoved = { selectedUsers = selectedUsers.apply { remove(it) } }
            )
        }
    }
}