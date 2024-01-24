/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ai.botstacks.sdk.API
import ai.botstacks.sdk.actions.update
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.Upload
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.op

@Stable
data class CreateChatState(val chat: Chat? = null) {
    var upload by mutableStateOf<Upload?>(null)
    var image: String? = chat?.image
    var name by mutableStateOf<String>(chat?.name ?: "")
    var description by mutableStateOf<String?>(chat?.description ?: "")
    var _private by mutableStateOf(chat?._private ?: false)

    init {
        current = this
    }

    var selectImage by mutableStateOf(false)
    val executing: Boolean get() = creating || chat?.updating ?: false
    var creating by mutableStateOf(false)
    fun exec(openInvite: (Chat) -> Unit, back: () -> Unit) {
        if (executing) return
        if (chat != null) {
            op({
                val img = upload?.await()
                chat.update(name, description, img, _private) {
                    back()
                }
            })
        } else {
            op({
                creating = true
                val img = upload?.await()
                val chat =
                    API.createChat(
                        name,
                        description = description,
                        _private = _private,
                        image = img
                    )
                creating = false
                chat?.let {
                    newChats.add(it.id)
                    back()
                    openInvite(it)
                }
            }) {
                creating = false
            }

        }
    }

    companion object {
        var current by mutableStateOf<CreateChatState?>(null)
        var newChats = mutableSetOf<String>()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateChat(chat: Chat?, openInvite: (Chat) -> Unit, _back: () -> Unit) {
    val back = {
        CreateChatState.current = null
        _back()
    }
    val state = remember { CreateChatState(chat) }
    val nameFocus = remember { FocusRequester() }
    val descriptionFocus = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    var canSubmit =
        state.name.length >= 3 && state.name.length <= 25 && (state.description?.length
            ?: 0) <= 100
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxHeight()) {
        Header(title = "Create Channel", back = back)
        Column(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .verticalScroll(scrollState)
                .clickable { keyboardController?.hide() }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .clickable { state.selectImage = true }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center

                ) {

                    (state.image ?: state.upload?.uri?.toString())?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "chat avatar",
                            modifier = Modifier.circle(116.dp, colorScheme.softBackground),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Image(
                        painter = Drawables.PlusCircleFilled,
                        contentDescription = "Add chat image",
                        colorFilter = ColorFilter.tint(colorScheme.softBackground),
                        modifier = Modifier.size(95.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(8.dp, 0.dp)
                        ) {
                            Text(text = "Channel Name", iac = fonts.headline, color = colorScheme.text)
                            Text(
                                text = "${state.name.length}/25",
                                iac = fonts.headline,
                                color = colorScheme.caption
                            )
                        }
                        Space()
                        TextInput(
                            text = state.name,
                            placeholder = "Showcase what your channel is all about",
                            onChange = {
                                if (it.endsWith("\n")) {
                                    descriptionFocus.captureFocus()
                                } else {
                                    state.name = if (it.length > 25) it.slice(0..25) else it
                                }
                            },
                            modifier = Modifier
                                .border(1.dp, colorScheme.border, CircleShape)
                                .clickable { nameFocus.requestFocus() },
                            focusRequester = nameFocus,
                            keyboardActions = KeyboardActions(onNext = { descriptionFocus.captureFocus() }),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    }
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(8.dp, 0.dp)
                        ) {
                            Text(
                                text = "Channel Description (Optional)",
                                iac = fonts.headline,
                                color = colorScheme.text
                            )
                            Text(
                                text = "${state.description?.length ?: "0"}/100",
                                iac = fonts.headline,
                                color = colorScheme.caption
                            )
                        }
                        Space()
                        TextInput(
                            text = state.description ?: "",
                            placeholder = "Showcase what your channel is all about",
                            onChange = {
                                state.description = if (it.length > 100) it.slice(0..100) else it
                            },
                            modifier = Modifier
                                .border(1.dp, colorScheme.border, RoundedCornerShape(22.dp))
                                .clickable { descriptionFocus.requestFocus() },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() }),
                            focusRequester = descriptionFocus,
                            minLines = 4,
                        )
                    }
                    Column {
                        Text(
                            text = "Privacy Settings",
                            iac = fonts.headline,
                            color = colorScheme.text,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Space()
                        Row(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .background(colorScheme.softBackground, RoundedCornerShape(25.dp))
                                .border(1.dp, colorScheme.border, RoundedCornerShape(25.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f)
                                    .background(
                                        if (state._private) Color.Transparent else colorScheme.primary,
                                        RoundedCornerShape(25.dp)
                                    )
                                    .clickable {
                                        state._private = false
                                        descriptionFocus.freeFocus()
                                        nameFocus.freeFocus()
                                    }
                                    .clip(RoundedCornerShape(25.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Public",
                                    iac = fonts.title3,
                                    color = if (state._private) colorScheme.text else colorScheme.background
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f)
                                    .background(
                                        if (!state._private) Color.Transparent else colorScheme.primary,
                                        RoundedCornerShape(25.dp)
                                    )
                                    .clickable {
                                        state._private = true
                                        keyboardController?.hide()
                                    }
                                    .clip(RoundedCornerShape(25.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Private",
                                    iac = fonts.title3,
                                    color = if (!state._private) colorScheme.text else colorScheme.background
                                )
                            }
                        }
                        Space(12f)
                        Text(
                            text = if (state._private) "Private channels will not be seen by anyone unless they are\n" +
                                    "invited to join the channel." else "Public channels will be viewed by all and available for everyone to join.",
                            iac = fonts.caption,
                            color = colorScheme.text
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f, true))
                    if (state.chat != null) {
                        Box(
                            modifier = Modifier
                                .clickable { }
                                .height(50.dp)
                                .background(
                                    if (canSubmit) colorScheme.primary else colorScheme.softBackground,
                                    RoundedCornerShape(25.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.chat.updating == true) {
                                    Spinner()
                                }
                                Text(text = "Save", iac = fonts.headline, color = colorScheme.text)
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    state.exec(openInvite, back)
                                },
                                modifier = Modifier.circle(
                                    50.dp,
                                    if (canSubmit) colorScheme.primary else colorScheme.softBackground
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = androidx.media3.ui.R.drawable.exo_ic_chevron_right),
                                    contentDescription = "Create chat",
                                    tint = colorScheme.text,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (state.selectImage)
        AssetPicker(video = false, onUri = {
            state.selectImage = false
            println("Got asset ur $it")
            state.upload = Upload(uri = it)
        }) {
            state.selectImage = false
        }
}

@IPreviews
@Composable
fun CreateChatPreview() {
    BotStacksChatContext {
        CreateChat(chat = null, openInvite = {}, _back = {})
    }
}