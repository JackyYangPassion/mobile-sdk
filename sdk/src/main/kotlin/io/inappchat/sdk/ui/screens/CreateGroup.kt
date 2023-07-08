/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.inappchat.sdk.API
import io.inappchat.sdk.R
import io.inappchat.sdk.actions.update
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Upload
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.op
import java.io.File

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

    val executing: Boolean get() = executing || chat?.updating ?: false
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
                    openInvite(it)
                }
            }) {
                creating = false
            }

        }
    }

    companion object {
        var current by mutableStateOf<CreateChatState?>(null)
    }
}

@Composable
fun CreateChat(chat: Chat?, openInvite: (Chat) -> Unit, _back: () -> Unit) {
    val back = {
        CreateChatState.current = null
        _back()
    }
    val state = CreateChatState.current ?: CreateChatState(chat)
    val nameFocus = remember { FocusRequester() }
    val descriptionFocus = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    var canSubmit =
            state.name.length >= 3 && state.name.length <= 25 && (state.description?.length
                    ?: 0) <= 100
    Column(modifier = Modifier.fillMaxHeight()) {
        Header(title = "Create Channel", back = back)
        Column(
                modifier = Modifier
                        .padding(16.dp, 0.dp)
                        .verticalScroll(scrollState)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                        modifier = Modifier
                                .clickable { }
                                .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                ) {
                    state.image?.let {
                        AsyncImage(
                                model = it,
                                contentDescription = "chat avatar",
                                modifier = Modifier.circle(116.dp, colors.softBackground)
                        )
                    } ?: Image(
                            painter = painterResource(id = R.drawable.plus_circle_fill),
                            contentDescription = "Add chat image",
                            colorFilter = ColorFilter.tint(colors.softBackground),
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
                            Text(text = "Channel Name", iac = fonts.headline, color = colors.text)
                            Text(
                                    text = "${state.name.length}/25",
                                    iac = fonts.headline,
                                    color = colors.caption
                            )
                        }
                        Space()
                        TextInput(
                                text = state.name,
                                placeholder = "Showcase what your channel is all about",
                                onChange = { state.name = it.slice(0..25) },
                                modifier = Modifier
                                        .border(1.dp, colors.border, CircleShape)
                                        .clickable { nameFocus.requestFocus() },
                                focusRequester = nameFocus
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
                                    color = colors.text
                            )
                            Text(
                                    text = "${state.description?.length ?: "0"}/100",
                                    iac = fonts.headline,
                                    color = colors.caption
                            )
                        }
                        Space()
                        TextInput(
                                text = state.description ?: "",
                                placeholder = "Showcase what your channel is all about",
                                onChange = { state.description = it.slice(0..100) },
                                modifier = Modifier
                                        .border(1.dp, colors.border, RoundedCornerShape(22.dp))
                                        .clickable { descriptionFocus.requestFocus() },
                                focusRequester = descriptionFocus,
                                minLines = 4
                        )
                    }
                    Column {
                        Text(
                                text = "Privacy Settings",
                                iac = fonts.headline,
                                color = colors.text,
                                modifier = Modifier.padding(start = 8.dp)
                        )
                        Space()
                        Row(
                                modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .background(colors.softBackground, RoundedCornerShape(25.dp))
                                        .border(1.dp, colors.border, RoundedCornerShape(25.dp))
                        ) {
                            Box(
                                    modifier = Modifier
                                            .height(50.dp)
                                            .weight(1f)
                                            .background(
                                                    if (state._private) Color.Transparent else colors.primary,
                                                    RoundedCornerShape(25.dp)
                                            ),
                                    contentAlignment = Alignment.Center
                            ) {
                                Text(
                                        text = "Public",
                                        iac = fonts.title3,
                                        color = if (!state._private) colors.text else colors.background
                                )
                            }
                            Box(
                                    modifier = Modifier
                                            .height(50.dp)
                                            .weight(1f)
                                            .background(
                                                    if (!state._private) Color.Transparent else colors.primary,
                                                    RoundedCornerShape(25.dp)
                                            ),
                                    contentAlignment = Alignment.Center
                            ) {
                                Text(
                                        text = "Private",
                                        iac = fonts.title3,
                                        color = if (state._private) colors.text else colors.background
                                )
                            }
                        }
                        Space(12f)
                        Text(
                                text = if (state._private) "Private channels will not be seen by anyone unless they are\n" +
                                        "invited to join the channel." else "Public channels will be viewed by all and available for everyone to join.",
                                iac = fonts.caption,
                                color = colors.text
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f, true))
                    if (state.chat != null) {
                        Box(
                                modifier = Modifier
                                        .clickable { }
                                        .height(50.dp)
                                        .background(
                                                if (canSubmit) colors.primary else colors.softBackground,
                                                RoundedCornerShape(25.dp)
                                        ),
                                contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.chat.updating == true) {
                                    Spinner()
                                }
                                Text(text = "Save", iac = fonts.headline, color = colors.text)
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
                                            if (canSubmit) colors.primary else colors.softBackground
                                    )
                            ) {
                                Icon(
                                        painter = painterResource(id = R.drawable.caret_left),
                                        contentDescription = "Create chat",
                                        tint = colors.text,
                                        modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@IPreviews
@Composable
fun CreateChatPreview() {
    InAppChatContext {
        CreateChat(chat = null, openInvite = {}, _back = {})
    }
}