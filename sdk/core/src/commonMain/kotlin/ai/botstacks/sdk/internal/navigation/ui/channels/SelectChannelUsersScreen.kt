package ai.botstacks.sdk.internal.navigation.ui.channels

import ai.botstacks.sdk.ui.components.Header
import ai.botstacks.sdk.ui.components.HeaderDefaults
import ai.botstacks.sdk.ui.views.ChannelUserSelectionState
import ai.botstacks.sdk.ui.views.SelectChannelUsersView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


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

            SelectChannelUsersView(state = state)
        }
    }
}