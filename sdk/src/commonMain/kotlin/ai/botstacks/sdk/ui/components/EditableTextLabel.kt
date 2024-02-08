package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.utils.ui.unboundedClickable
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.resources.Res
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun EditableTextLabel(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onEditComplete: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    AnimatedContent(
        modifier = modifier,
        targetState = isEditing,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { editing ->
        if (editing) {
            TextInput(
                Modifier.fillMaxWidth().focusRequester(focusRequester),
                state = state,
                placeholder = placeholder,
                maxLines = 1,
                fontStyle = BotStacks.fonts.body2,
                indicatorColor = BotStacks.colorScheme.primary,
                keyboardActions = KeyboardActions(onDone = { onEditComplete() }),
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(BotStacks.dimens.staticGrid.x4)
                            .unboundedClickable { onEditComplete() },
                        painter = painterResource(Res.Drawables.Outlined.Check),
                        tint = BotStacks.colorScheme.primary,
                        contentDescription = "Save name change"
                    )
                }
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartEditing() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BotStacks.dimens.grid.x2, Alignment.CenterHorizontally)
            ) {
                Text(state.text.toString(), fontStyle = BotStacks.fonts.h3)
                Icon(
                    painterResource(Res.Drawables.Outlined.Edit),
                    contentDescription = null,
                    tint = BotStacks.colorScheme.primary
                )
            }
        }
    }
}