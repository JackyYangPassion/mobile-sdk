/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboardManager
import io.inappchat.sdk.actions.react
import io.inappchat.sdk.actions.toggleFavorite
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.genChatextMessage

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MessageActionSheet(
        message: Message?,
        onReply: (Message) -> Unit,
        hide: () -> Unit,
        content: @Composable () -> Unit
) {
    val state = rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden, skipHalfExpanded = true
    )
    LaunchedEffect(key1 = message, block = {
        if (message != null) {
            state.show()
        } else {
            state.hide()
        }
    })
    val annotatedString = (message?.text ?: "").annotated()
    val clipboardManager = LocalClipboardManager.current
    val copy = {
        clipboardManager.setText(annotatedString)
        hide()
    }
    ModalBottomSheetLayout(
            sheetState = state,
            sheetBackgroundColor = colors.background,
            sheetContentColor = colors.text,
            scrimColor = colors.caption,
            sheetContent = {
                Space(8f)
                EmojiBar(
                        current = message?.currentReaction,
                        onEmoji = {
                            message?.react(it)
                            hide()
                        }
                )
                ActionItem(
                        text = "Reply in Chat",
                        icon = io.inappchat.sdk.R.drawable.chat_dots
                ) {
                    message?.let(onReply)
                    hide()
                }

                ActionItem(
                        text = if (message?.favorite == true) "Save to Favorites" else "Remove from Favorites",
                        icon = io.inappchat.sdk.R.drawable.star_fill
                )
                {
                    message?.toggleFavorite()
                    hide()
                }

                ActionItem(
                        text = "Copy message text",
                        icon = io.inappchat.sdk.R.drawable.copy,
                ) {
                    copy()
                }
            },
            content = content
    )
}

@IPreviews
@Composable
fun MessageActionSheetPreview() {
    InAppChatContext {
        var message by remember {
            mutableStateOf<Message?>(genChatextMessage())
        }
        MessageActionSheet(message = message, hide = {}, onReply = {}) {
            Button(onClick = { message = genChatextMessage() }) {
                Text(text = "Open Sheet", iac = fonts.headline)
            }
        }
    }
}