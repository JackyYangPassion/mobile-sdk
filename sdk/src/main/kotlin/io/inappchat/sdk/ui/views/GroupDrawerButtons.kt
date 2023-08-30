/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.actions.delete
import io.inappchat.sdk.actions.leave
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.*


@Composable
fun ChatDrawerButtons(
    chat: Chat,
    openEdit: (Chat) -> Unit,
    openInvite: (Chat) -> Unit,
    dismiss: () -> Unit,
    back: () -> Unit
) {
    val dialog = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .height(60.dp)
                .background(colors.softBackground.copy(0.3f), RoundedCornerShape(16.dp))
                .padding(8.dp, 0.dp)
        ) {
            if (chat.isAdmin) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        dismiss()
                        openEdit(chat)
                    }) {
                    Image(
                        painter = painterResource(id = io.inappchat.sdk.R.drawable.gear_fill),
                        contentDescription = "settings",
                        colorFilter = ColorFilter.tint(colors.border),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(text = "Edit", iac = theme.fonts.mini, color = colors.border)
                }
                Space()
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    dismiss()
                    openInvite(chat)
                }) {
                Image(
                    painter = painterResource(id = io.inappchat.sdk.R.drawable.archive_box_fill),
                    contentDescription = "settings",
                    colorFilter = ColorFilter.tint(colors.border),
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Invite", iac = theme.fonts.mini, color = colors.border)
            }
            Space()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    dismiss()
                    dialog.value = true
                }) {
                Image(
                    painter = painterResource(id = io.inappchat.sdk.R.drawable.trash_fill),
                    contentDescription = "settings",
                    colorFilter = ColorFilter.tint(colors.border),
                    modifier = Modifier.size(24.dp)
                )
                Text(text = "Leave", iac = theme.fonts.mini, color = colors.border)
            }
        }
    }
    if (dialog.value) {

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                dialog.value = false
            },
            title = {
                Text(
                    text = ift(chat.isAdmin, "Delete ${chat.name}", "Leave ${chat.name}"),
                    iac = fonts.title2
                )
            },
            text = {
                Text(
                    "Are you sure you want to ${
                        ift(
                            chat.isAdmin,
                            "delete",
                            "leave"
                        )
                    } this channel?", fonts.body
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (chat.isAdmin) {
                            op(
                                {
                                    bg {
                                        chat.delete()
                                    }
                                    back()
                                })
                        } else {
                            chat.leave()
                        }
                        dialog.value = false
                        dismiss()
                    }) {
                    Text(
                        ift(chat.isAdmin, "Delete", "Leave"),
                        fonts.headline,
                        color = colors.destructive
                    )
                }
            },
            dismissButton = {
                Button(

                    onClick = {
                        dialog.value = false
                    }) {
                    Text("Cancel", fonts.body)
                }
            }
        )
    }
}

@IPreviews
@Composable
fun ChatDrawerButtonsPreview() {
    InAppChatContext {
        ChatDrawerButtons(chat = genG(), {}, {}, {}, {})
    }
}