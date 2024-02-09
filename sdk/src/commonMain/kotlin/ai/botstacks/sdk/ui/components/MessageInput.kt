/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.actions.send
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.internal.Pressable
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.Fn
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genChat
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.foundation.text2.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(
    ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    chat: Chat,
    replyingTo: Message? = null,
    onMedia: Fn,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    val composeScope = rememberCoroutineScope()
    val state = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val onSend = {
        if (state.text.isNotBlank()) {
            chat.send(replyingTo?.id, state.text.toString())
            keyboardController?.hide()
            state.clearText()
        }
    }

    Row(
        modifier = modifier
            .clickable { keyboardController?.show() }
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimens.grid.x2),
    ) {
        TextInput(
            state = state,
            modifier = Modifier
                .background(colorScheme.chatInput, BotStacks.shapes.medium)
                .padding(dimens.grid.x3)
                .weight(1f)
                .focusRequester(focusRequester),
            color = colorScheme.onChatInput,
            keyboardActions = KeyboardActions(onDone = { onSend() }),
            placeholder = "Message...",
            leadingIcon = {
                Pressable(
                    onClick = {
                        keyboardController?.hide()
                        composeScope.launch {
                            delay(300)
                            onMedia()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(Res.Drawables.Filled.Paperclip),
                        contentDescription = "send attachment",
                        modifier = Modifier.requiredIconSize(),
                        tint = colorScheme.caption
                    )
                }
            },
        )
        val canSend by remember {
            derivedStateOf { state.text.isNotBlank() }
        }

        Pressable(enabled = canSend, onClick = onSend) {
            val color by animateColorAsState(
                targetValue = if (canSend) colorScheme.primary else colorScheme.surface,
                label = "send button bg color"
            )
            val contentColor by animateColorAsState(
                targetValue = if (canSend) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
                label = "send button content color"
            )
            Box(
                modifier = Modifier
                    .background(color, CircleShape)
                    .padding(dimens.grid.x3),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.Drawables.Filled.PaperPlaneTilt),
                    contentDescription = "send message",
                    modifier = Modifier.requiredIconSize(),
                    tint = contentColor
                )
            }
        }
    }
}

@IPreviews
@Composable
fun MessageInputPreview() {
    BotStacksChatContext {
        MessageInput(chat = genChat(), replyingTo = null, onMedia = {})
    }

}