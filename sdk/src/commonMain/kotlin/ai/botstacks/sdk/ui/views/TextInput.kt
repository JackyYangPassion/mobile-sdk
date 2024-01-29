/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks

@Composable
fun TextInput(
    text: String,
    focusRequester: FocusRequester = remember { FocusRequester() },
    modifier: Modifier = Modifier,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = 4,
    onChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    inputModifier: Modifier = Modifier.focusRequester(focusRequester),
    right: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 44.dp)
            .background(BotStacks.colorScheme.softBackground, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable { focusRequester.requestFocus() }
            .padding(12.dp),
    ) {
        Box(modifier = Modifier.weight(1f)) {

            BasicTextField(
                value = text,
                onValueChange = onChange,
                textStyle = BotStacks.fonts.body1.asTextStyle().copy(
                    color = BotStacks.colorScheme.text,),
                minLines = minLines,
                maxLines = maxLines,
                modifier = inputModifier.focusRequester(focusRequester),
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                enabled = enabled,
                visualTransformation = visualTransformation
            )
            if (text.isBlank()) {
                Text(text = placeholder, fontStyle = BotStacks.fonts.body1, color = BotStacks.colorScheme.caption)
            }
        }
        right()
        Space(8f)
    }
}