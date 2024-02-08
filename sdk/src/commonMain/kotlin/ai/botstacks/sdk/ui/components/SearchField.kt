package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.components.internal.Pressable
import ai.botstacks.sdk.ui.resources.Res
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    showClear: Boolean = false,
    onSearch: () -> Unit = { },
    onClear: () -> Unit = { }
) {
    BasicTextField2(
        modifier = modifier,
        state = state,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorator = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    it()
                    if (state.text.isEmpty()) {
                        Text(
                            text = "Search...",
                            fontStyle = BotStacks.fonts.caption1,
                            color = BotStacks.colorScheme.onChatInput
                        )
                    }
                }
                if (showClear) {
                    Pressable(
                        onClick = {
                            state.clearText()
                            onClear()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.requiredIconSize(),
                            painter = painterResource(Res.Drawables.Outlined.Close),
                            contentDescription = "close search"
                        )
                    }
                }
            }
        }
    )
}