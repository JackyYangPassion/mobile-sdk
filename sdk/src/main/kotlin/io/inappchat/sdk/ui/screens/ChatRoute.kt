/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.User

@Composable
fun ChatRoute(
    gid: String? = null, uid: String? = null, mid: String? = null,
    openProfile: (User) -> Unit,
    openInvite: (Group) -> Unit,
    openEditGroup: (Group) -> Unit,
    back: () -> Unit
) {

}