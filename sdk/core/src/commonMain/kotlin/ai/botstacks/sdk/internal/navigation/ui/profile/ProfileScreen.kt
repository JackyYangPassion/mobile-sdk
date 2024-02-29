/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.internal.navigation.ui.profile

import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.ui.views.EditProfileState
import ai.botstacks.sdk.ui.views.EditProfileView
import ai.botstacks.sdk.ui.components.Header
import ai.botstacks.sdk.ui.components.HeaderDefaults
import ai.botstacks.sdk.internal.ui.components.ProgressOverlay
import ai.botstacks.sdk.ui.views.UserDetailsView
import ai.botstacks.sdk.internal.utils.IPreviews
import ai.botstacks.sdk.internal.utils.genImageMessage
import ai.botstacks.sdk.internal.utils.genU
import ai.botstacks.sdk.internal.utils.random
import ai.botstacks.sdk.internal.utils.ui.keyboardAsState
import ai.botstacks.sdk.ui.views.UserDetailsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun ProfileView(
    user: User,
    onBackClicked: () -> Unit,
) {
    val isCurrentUser = remember(user) { user.isCurrent }
    if (isCurrentUser) {
        EditProfileScreen(user = user, onBackClicked = onBackClicked)
    } else {
        UserDetailsScreen(user = user, onBackClicked = onBackClicked)
    }
}

@Composable
private fun EditProfileScreen(
    user: User,
    onBackClicked: () -> Unit,
) {
    val composeScope = rememberCoroutineScope()
    val state = remember(user) { EditProfileState() }

    val keyboardVisible by keyboardAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(
                title = "Edit Profile",
                onBackClicked = onBackClicked,
                endAction = {
                    HeaderDefaults.SaveAction {
                        composeScope.launch {
                            if (keyboardVisible) {
                                keyboardController?.hide()
                                delay(300.milliseconds)
                            }
                            val saveResult = state.update()
                            if (saveResult.isSuccess) {
                                onBackClicked()
                            }
                        }
                    }
                }
            )

            EditProfileView(state = state)
        }
        ProgressOverlay(visible = state.saving, touchBlocking = true)
    }
}

@Composable
private fun UserDetailsScreen(user: User, onBackClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Header(
                title = "User Details",
                onBackClicked = onBackClicked,
            )

            val state = remember(user) { UserDetailsState(user) }
            UserDetailsView(state)
        }
    }
}

@IPreviews
@Composable
private fun ProfileViewPreview() {
    val u = genU()
    val ms = random(10, { genImageMessage(user = u) })
//    u.sharedMedia.items.addAll(ms)
    BotStacksThemeEngine {
        ProfileView(user = u, onBackClicked = { /*TODO*/ })
    }
}