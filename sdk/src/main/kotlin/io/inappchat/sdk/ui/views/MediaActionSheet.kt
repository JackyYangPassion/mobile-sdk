/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import io.inappchat.sdk.InAppChatActivity
import io.inappchat.sdk.R
import io.inappchat.sdk.actions.send
import io.inappchat.sdk.extensions.contains
import io.inappchat.sdk.models.Contact
import io.inappchat.sdk.state.*
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
  val hide = {
    media = null
    scope.launch { state.hide() }
  }

  when (media) {
    Media.pickPhoto, Media.pickVideo -> AssetPicker(video = media == Media.pickVideo, onUri = {
      room.send(
        inReplyTo?.id,
        attachment = Attachment(
          it.toFile().absolutePath,
          if (media == Media.pickVideo) AttachmentKind.video else AttachmentKind.image
        )
      )
      hide()
    }) { hide() }
    Media.recordPhoto, Media.recordVideo -> CameraPicker(
      video = media == Media.recordVideo,
      onUri = {
        room.send(
          inReplyTo?.id,
          attachment = Attachment(
            it.toFile().absolutePath,
            if (media == Media.recordVideo) AttachmentKind.video else AttachmentKind.image
          )
        )
        hide()
      }) { hide() }
    Media.gif -> GifPicker(onUri = {
      room.send(inReplyTo?.id, gif = it.toString())
      hide()
    }) { hide() }
    Media.contact -> ContactPicker(onContact = {
      room.send(inReplyTo?.id, contact = it)
      hide()
    }) {
      hide()
    }
    Media.location -> LocationPicker(onLocation = {
      room.send(inReplyTo?.id, location = it.toLocation())
      hide()
    }) {
      hide()
    }
    else -> Log.d("IAC", "empty media")
  }

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

@Composable
fun AssetPicker(video: Boolean, onUri: (Uri) -> Unit, onCancel: () -> Unit) {
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = {
      Log.v("IAC", "Got image Uri $it")
      it?.let(onUri) ?: onCancel()
    })
  LaunchedEffect(key1 = true, block = {
    launcher.launch(PickVisualMediaRequest(if (video) ActivityResultContracts.PickVisualMedia.VideoOnly else ActivityResultContracts.PickVisualMedia.ImageOnly))
  })
}

@Composable
fun CameraPicker(video: Boolean, onUri: (Uri) -> Unit, onCancel: () -> Unit) {
  val uri = LocalContext.current.cacheDir.resolve(uuid()).toUri()
  val launcher = rememberLauncherForActivityResult(
    contract = if (video) ActivityResultContracts.CaptureVideo() else ActivityResultContracts.TakePicture(),
    onResult = {
      if (it) {
        onUri(uri)
      } else {
        onCancel()
      }
    })
  LaunchedEffect(key1 = true, block = {
    launcher.launch(uri)
  })
}

@Composable
fun ContactPicker(onContact: (Contact) -> Unit, onCancel: () -> Unit) {
  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickContact(),
    onResult = {
      it?.let({ contactUriToContact(it) })?.let(onContact) ?: onCancel()
    }
  )
  LaunchedEffect(key1 = true, block = { launcher.launch(null) })
}

@Composable
fun GifPicker(onUri: (Uri) -> Unit, onCancel: () -> Unit) {
  DisposableEffect(true) {
    Chats.current.nextGif = {
      onUri(it.toUri())
    }
    InAppChatActivity.activity.get()?.giphy()
    onDispose {
      Chats.current.nextGif = null
    }
  }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPicker(onLocation: (Location) -> Unit, onCancel: () -> Unit) {
  // Camera permission state
  val state = rememberMultiplePermissionsState(
    listOf(
      android.Manifest.permission.ACCESS_FINE_LOCATION,
      android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
  )
  val have = state.allPermissionsGranted || state.permissions.contains { it.status.isGranted }
  val activity = LocalContext.current as Activity
  LaunchedEffect(key1 = have, block = {
    if (have) {
      val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
      val cancellationToken = CancellationTokenSource()
      val loc = fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        cancellationToken.token
      ).await()
      if (loc != null) {
        onLocation(loc)
      } else {
        onCancel()
      }
    } else {
      state.launchMultiplePermissionRequest()
    }
  })
}