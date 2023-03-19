/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.material.*
import androidx.compose.runtime.*
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genGroupRoom
import kotlinx.coroutines.launch

enum class Media {
  pickPhoto,
  pickVideo,
  recordPhoto,
  recordVideo,
  gif,
  file,
  contact,
  location
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MediaActionSheet(
  open: Boolean,
  room: Room,
  inReplyTo: Message?,
  content: @Composable () -> Unit
) {
  val state = rememberModalBottomSheetState(
    ModalBottomSheetValue.Hidden, skipHalfExpanded = true
  )
  var media by remember { mutableStateOf<Media?>(null) }
  val scope = rememberCoroutineScope()
  LaunchedEffect(key1 = open, block = {
    if (open) {
      state.show()
    } else {
      state.hide()
    }
  })
  val hide = { scope.launch { state.hide() } }

  ModalBottomSheetLayout(
    sheetState = state,
    sheetBackgroundColor = IAC.colors.background,
    sheetContentColor = IAC.colors.text,
    scrimColor = IAC.colors.caption,
    sheetContent = {
      Space(8f)
      ActionItem(icon = R.drawable.image_square, text = "Upload Photo", divider = false) {
        media = Media.pickPhoto
      }
      ActionItem(icon = R.drawable.camera, text = "Take Photo", divider = true) {
        media = Media.recordPhoto
      }
      ActionItem(icon = R.drawable.file_video, text = "Upload Video", divider = false) {
        media = Media.pickVideo
      }
      ActionItem(icon = R.drawable.video_camera, text = "Video Camera", divider = true) {
        media = Media.recordVideo
      }
      ActionItem(icon = R.drawable.gif, text = "Send a GIF", divider = true) {
        media = Media.gif
      }
      ActionItem(icon = R.drawable.map_pin, text = "Send Location", divider = true) {
        media = Media.location
      }
      ActionItem(icon = R.drawable.address_book, text = "Share Contact", divider = true) {
        media = Media.contact
      }
    },
    content = content
  )
}

@IPreviews
@Composable
fun MediaActionSheetPreview() {
  InAppChatContext {
    var open by remember {
      mutableStateOf(false)
    }
    MediaActionSheet(true, genGroupRoom(), inReplyTo = null) {
      Button(onClick = { open = true }) {
        Text(text = "Open Sheet", iac = IAC.fonts.headline)
      }
    }
  }
}