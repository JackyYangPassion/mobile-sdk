/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genU

@Composable
fun ProfileView(user: User, back: () -> Unit, openChat: (User) -> Unit) {
  Column {
    Header(title = user.usernameFb)
  }
}

@IPreviews
@Composable
fun ProfileViewPreview() {
  InAppChatContext {
    ProfileView(user = genU(), back = { /*TODO*/ }, openChat = {})
  }
}