package ai.botstacks.sdk.ui.screens

import ai.botstacks.sdk.ui.views.CreateChannelState
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.HeaderDefaults
import ai.botstacks.sdk.ui.views.SelectChannelUsersView
import ai.botstacks.sdk.utils.log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun SelectChannelUsersScreen(
    state: CreateChannelState,
    onBackClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        var selectedUsers by remember(state.participants) {
            mutableStateOf(state.participants)
        }

        Column {
            Header(
                title = "Add members",
                onBackClicked = onBackClicked,
                endAction = {
                    HeaderDefaults.SaveAction {
                        log("count=${selectedUsers.count()}")
                        state.participants = selectedUsers
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