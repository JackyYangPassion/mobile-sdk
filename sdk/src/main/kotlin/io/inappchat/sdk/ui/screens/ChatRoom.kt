/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User

enum class Media {
  pickPhoto,
  pickVideo,
  recordPhoto,
  recordVideo,
  gif,
  file,
  contact
}

@Composable
fun ChatRoom(
  room: Room,
  message: Message? = null,
  openProfile: (User) -> Unit,
  openInvite: (Group) -> Unit,
  back: () -> Unit
) {
  var focusRequester = remember { FocusRequester() }
  var selectMedia = remember { mutableStateOf<Media?>(null) }
  var media = remember { mutableStateOf(false) }
  var menu = remember { mutableStateOf(false) }
  var messageForAction = remember {
    mutableStateOf<Message?>(null)
  }
  var messageForEmojiKeyboard = remember {
    mutableStateOf<Message?>(null)
  }

}