package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.extensions.debugBounds
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.utils.log
import ai.botstacks.sdk.utils.randomUsers
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectChannelUsersView(
    selectedUsers: List<User>,
    onUserSelected: (User) -> Unit,
    onUserRemoved: (User) -> Unit,
) {
    val search = rememberTextFieldState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(
                bottom = BotStacks.dimens.inset
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = BotStacks.dimens.inset,
                    start = BotStacks.dimens.inset,
                    end = BotStacks.dimens.inset,
                ),
            contentAlignment = Alignment.Center
        ) {
            TextInput(
                modifier = Modifier.fillMaxWidth(),
                state = search,
                placeholder = "Search name",
                maxLines = 1,
                fontStyle = BotStacks.fonts.body2,
                indicatorColor = BotStacks.colorScheme.caption,
            )
        }
        PagerList(
            modifier = Modifier.weight(1f),
            pager = BotStacksChatStore.current.users,
        ) { user ->
            var isSelected by remember(selectedUsers) {
                mutableStateOf(selectedUsers.contains(user))
            }

            val handleChange = {
                isSelected = if (isSelected) {
                    onUserRemoved(user)
                    false
                } else {
                    onUserSelected(user)
                    true
                }
            }

            UserRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = BotStacks.dimens.inset,
                        end = BotStacks.dimens.inset,
                    ),
                avatar = {
                    Avatar(
                        user = user,
                        isSelected = isSelected,
                        showOnlineStatus = false,
                    )
                },
                displayName = user.displayNameFb,
                onClick = { handleChange() }
            )
        }
    }
}