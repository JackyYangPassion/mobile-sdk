package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.internal.ui.theme.LocalBotStacksColorPalette
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.theme.dayNightColor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import botstacks.sdk.core.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UserSelectView(
    modifier: Modifier = Modifier,
    selectedUsers: List<User>,
    canRemove: Boolean = false,
    showAdd: Boolean = true,
    onRemove: (User) -> Unit = { },
    onAddSelected: () -> Unit = { },
) {
    val palette = LocalBotStacksColorPalette.current
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(BotStacks.dimens.inset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(selectedUsers) {
            Avatar(
                modifier = Modifier
                    .clickable { onRemove(it) },
                user = it,
                isRemovable = canRemove,
            )
        }
        if (showAdd) {
            item {
                Avatar(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAddSelected() },
                    type = AvatarType.User(
                        url = null,
                        empty = painterResource(Res.drawable.plus),
                    ),
                    backgroundColor = dayNightColor(palette.dark._900, palette.dark._500),
                    contentColor = Color.White,

                    )
            }
        }
    }
}