package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.type.OnlineStatus
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnlineStatusIndicator(
    modifier: Modifier = Modifier,
    status: OnlineStatus
) {
    Box(
        modifier = Modifier
            .background(BotStacks.colorScheme.background, CircleShape)
            .padding(3.dp)
            .size(12.dp)
            .background(
                when (status) {
                    OnlineStatus.Away -> BotStacks.colors.Dark._100
                    OnlineStatus.DND -> BotStacks.colorScheme.background
                    OnlineStatus.Offline -> BotStacks.colors.Error._500
                    OnlineStatus.Online -> BotStacks.colors.Green._800
                    OnlineStatus.UNKNOWN__ -> BotStacks.colorScheme.background
                }, CircleShape
            ).then(modifier)
    )
}