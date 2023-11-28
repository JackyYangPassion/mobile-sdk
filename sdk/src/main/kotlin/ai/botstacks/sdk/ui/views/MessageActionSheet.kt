/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import ai.botstacks.sdk.actions.react
import ai.botstacks.sdk.actions.toggleFavorite
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated
import ai.botstacks.sdk.utils.genChatextMessage

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
        modifier = Modifier.fillMaxSize(),
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
                icon = ai.botstacks.sdk.R.drawable.chat_dots
            ) {
                message?.let(onReply)
                hide()
            }

            ActionItem(
                text = if (message?.favorite == true) "Remove from Favorites" else "Save to Favorites",
                icon = ai.botstacks.sdk.R.drawable.star_fill
            )
            {
                message?.toggleFavorite()
                hide()
            }

            ActionItem(
                text = "Copy message text",
                icon = ai.botstacks.sdk.R.drawable.copy,
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
    BotStacksChatContext {
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