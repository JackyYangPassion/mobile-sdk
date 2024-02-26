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

@Composable
fun UserProfile(modifier : Modifier = Modifier, user: User) {
    Column(
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