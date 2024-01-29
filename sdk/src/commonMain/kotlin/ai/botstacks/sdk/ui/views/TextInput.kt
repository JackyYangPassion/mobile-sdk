/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.theme.FontStyle
import android.view.ViewTreeObserver
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.CodepointTransformation
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.mask
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

object TextInputDefaults {
    @OptIn(ExperimentalFoundationApi::class)
    val PasswordMask = CodepointTransformation.mask('•')
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 4,
    state: TextFieldState,
    onStateChanged: () -> Unit = { },
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    fontStyle: FontStyle = BotStacks.fonts.body1,
    color: Color = BotStacks.colorScheme.onBackground,
    codepointTransformation: CodepointTransformation? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: @Composable () -> Unit = { },
    trailingIcon: @Composable () -> Unit = { },
    scrollState: ScrollState = rememberScrollState(),
) {
    BasicTextField2(
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        state = state,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = fontStyle.asTextStyle().copy(color = color),
        lineLimits = if (maxLines == 1) {
            TextFieldLineLimits.SingleLine
        } else {
            TextFieldLineLimits.MultiLine(minHeightInLines = minLines, maxHeightInLines = maxLines)
        },
        codepointTransformation = codepointTransformation,
        decorator = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                leadingIcon()
                Box(modifier = Modifier.weight(1f)) {
                    it()
                    if (state.text.isEmpty() && placeholder.isNotEmpty()) {
                        Text(
                            text = placeholder,
                            fontStyle = BotStacks.fonts.caption1,
                            color = BotStacks.colorScheme.onChatInput
                        )
                    }
                }
                trailingIcon()
            }
        },
        scrollState = scrollState
    )

    LaunchedEffect(Unit) {
        state.textAsFlow()
            .onEach { onStateChanged() }
            .launchIn(this)
    }

    val focusManager = LocalFocusManager.current
    val keyboardState by keyboardAsState()
    LaunchedEffect(keyboardState) {
        if (!keyboardState) {
            focusManager.clearFocus(true)
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            keyboardState.value = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose { viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
    return keyboardState
}