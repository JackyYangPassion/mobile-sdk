/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.ui.IAC

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
    right: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
          .defaultMinSize(minHeight = 44.dp)
          .background(IAC.colors.softBackground, RoundedCornerShape(22.dp))
          .padding(12.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = text,
                onValueChange = onChange,
                textStyle = TextStyle.Default.copy(
                    color = IAC.colors.text,
                    fontFamily = IAC.fonts.body.family,
                    fontSize = IAC.fonts.body.size,
                    fontWeight = IAC.fonts.body.weight
                ),
                minLines = minLines,
                maxLines = maxLines,
                modifier = Modifier.focusRequester(focusRequester),
                keyboardActions = keyboardActions
            )
            if (text.isBlank()) {
                Text(text = placeholder, iac = IAC.fonts.body, color = IAC.colors.caption)
            }
        }
        right()
        Space(8f)
    }
}