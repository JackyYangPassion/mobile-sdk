/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.actions.send
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Upload
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.internal.ActionItem
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.GiphyModalSheet
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genChat
import ai.botstacks.sdk.utils.op
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


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
@OptIn(ExperimentalMaterialApi::class, ExperimentalResourceApi::class)
fun MediaActionSheet(
    state: ModalBottomSheetState,
    chat: Chat,
    inReplyTo: Message?,
    dismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var media by remember { mutableStateOf<Media?>(null) }
    val scope = rememberCoroutineScope()
    val hide = {
        media = null
        scope.launch { state.hide() }
    }

    val onFile = { file: KmpFile ->
        op({
            chat.send(inReplyTo?.id, upload = Upload(file = file))
        })
        hide()
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetBackgroundColor = BotStacks.colorScheme.background,
        sheetContentColor = BotStacks.colorScheme.onBackground,
        scrimColor = BotStacks.colorScheme.caption,
        sheetContent = {
            Box {
                Column {
                    Space(8f)
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.ImageSquare),
                        text = "Upload Photo",
                        divider = false
                    ) {
                        media = Media.pickPhoto
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.Camera),
                        text = "Take Photo",
                        divider = true
                    ) {
                        media = Media.recordPhoto
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.FileVideo),
                        text = "Upload Video",
                        divider = false
                    ) {
                        media = Media.pickVideo
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.VideoCamera),
                        text = "Video Camera",
                        divider = true
                    ) {
                        media = Media.recordVideo
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.Gif),
                        text = "Send a GIF",
                        divider = true
                    ) {
                        media = Media.gif
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.MapPin),
                        text = "Send Location",
                        divider = true
                    ) {
                        media = Media.location
                    }
                    ActionItem(
                        icon = painterResource(Res.Drawables.Outlined.AddressBook),
                        text = "Share Contact",
                        divider = true
                    ) {
                        media = Media.contact
                    }
                }
                when (media) {
                    Media.pickPhoto, Media.pickVideo -> AssetPicker(
                        video = media == Media.pickVideo,
                        onUri = {
                            onFile(it)
                            hide()
                        }) { hide() }

                    Media.recordPhoto, Media.recordVideo -> {
//                        CameraPicker(
//                            video = media == Media.recordVideo,
//                            onUri = {
////                            onFile(it)
//                                hide()
//                            }) { hide() }
                    }

                    Media.gif -> GifPicker(onUri = {
//                        chat.send(inReplyTo?.id, attachments = listOf(it.toUri().imageAttachment()))
                        hide()
                    }) { hide() }

                    Media.contact -> {
//                        ContactPicker(onContact = {
//                            chat.send(inReplyTo?.id, attachments = listOf(it))
//                            hide()
//                        }) {
//                            hide()
//                        }
                    }

                    Media.location -> {
//                        LocationPicker(onLocation = {
////                        chat.send(inReplyTo?.id, attachments = listOf(it.attachment()))
//                            hide()
//                        }) {
//                            hide()
//                        }
                    }

                    else -> Logger.d("empty media")
                }
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@IPreviews
@Composable
fun MediaActionSheetPreview() {
    BotStacksChatContext {
        var open = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        val ctx = rememberCoroutineScope()
        MediaActionSheet(
            open,
            genChat(),
            dismiss = { ctx.launch { open.hide() } },
            inReplyTo = null
        ) {
            Button(onClick = { ctx.launch { open.show() } }) {
                Text(text = "Open Sheet", fontStyle = BotStacks.fonts.body2)
            }
        }
    }
}

@Composable
fun AssetPicker(video: Boolean, onUri: (KmpFile) -> Unit, onCancel: () -> Unit) {
    val pickerLauncher = rememberFilePickerLauncher(
        type = if (video) FilePickerFileType.Video else FilePickerFileType.Image,
        selectionMode = FilePickerSelectionMode.Single,
        onResult = { files -> files.firstOrNull() ?: onCancel() }
    )

    LaunchedEffect(video, onUri) {
        pickerLauncher.launch()
    }

}

//@Composable
//fun CameraPicker(video: Boolean, onUri: (Uri) -> Unit, onCancel: () -> Unit) {
//    val uri = LocalContext.current.cacheDir.resolve(uuid()).toUri()
//    val launcher = rememberLauncherForActivityResult(
//        contract = if (video) ActivityResultContracts.CaptureVideo() else ActivityResultContracts.TakePicture(),
//        onResult = {
//            if (it) {
//                onUri(uri)
//            } else {
//                onCancel()
//            }
//        })
//    LaunchedEffect(key1 = true, block = {
//        launcher.launch(uri)
//    })
//}

//@Composable
//fun ContactPicker(onContact: (AttachmentInput) -> Unit, onCancel: () -> Unit) {
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickContact(),
//        onResult = {
//            it?.let({ contactUriToVCard(it).attachment() })?.let(onContact) ?: onCancel()
//        }
//    )
//    LaunchedEffect(key1 = true, block = { launcher.launch(null) })
//}

@Composable
fun GifPicker(onUri: (String) -> Unit, onCancel: () -> Unit) {
    GiphyModalSheet(onSelection = onUri, onCancel = onCancel)
}

//@SuppressLint("MissingPermission")
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun LocationPicker(onLocation: (Location) -> Unit, onCancel: () -> Unit) {
//    // Camera permission state
//    val state = rememberMultiplePermissionsState(
//        listOf(
//            android.Manifest.permission.ACCESS_FINE_LOCATION,
//            android.Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//    )
//    val have = state.allPermissionsGranted || state.permissions.contains { it.status.isGranted }
//    val activity = LocalContext.current as Activity
//    LaunchedEffect(key1 = have, block = {
//        if (have) {
//            onCancel()
//            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
//            val cancellationToken = CancellationTokenSource()
//            val loc = fusedLocationClient.getCurrentLocation(
//                Priority.PRIORITY_HIGH_ACCURACY,
//                cancellationToken.token
//            ).await()
//            if (loc != null) {
//                onLocation(loc)
//            } else {
//                onCancel()
//            }
//        } else {
//            state.launchMultiplePermissionRequest()
//        }
//    })
//}