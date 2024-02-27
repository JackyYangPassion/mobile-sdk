package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach

@Composable
fun ChannelList(modifier: Modifier = Modifier, channels: List<Chat>) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BotStacks.dimens.grid.x4)
    ) {
        channels.fastForEach { channel ->
            ChannelRow(
                modifier = Modifier.fillMaxWidth(),
                chat = channel,
                showMemberPreview = true,
                titleColor = BotStacks.colorScheme.primary,
                titleFontStyle = BotStacks.fonts.caption1,
            )
        }
    }
}