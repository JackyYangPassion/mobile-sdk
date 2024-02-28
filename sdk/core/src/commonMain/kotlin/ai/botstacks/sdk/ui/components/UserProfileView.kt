package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.internal.ui.components.Text
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

/**
 * UserProfileView
 *
 * A screen content view that renders an [Avatar] and the display name for a given [User] in a centered [Column].
 *
 * @param modifier The modifier to apply to the Column
 * @param user The user to show in the view
 *
 */
@Composable
fun UserProfileView(modifier : Modifier = Modifier, user: User) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BotStacks.dimens.inset),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Avatar(
            modifier = Modifier.clip(CircleShape),
            type = AvatarType.User(
                url = user.avatar,
            ),
            size = AvatarSize.Large
        )
        Text(user.displayNameFb, fontStyle = BotStacks.fonts.h3)
    }
}