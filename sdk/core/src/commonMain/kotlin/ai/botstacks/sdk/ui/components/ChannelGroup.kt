package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach

/**
 * ChannelGroup
 *
 * Renders a given list of [Chat] channels in a [Column].
 *
 * This renders each chat in its own [ChannelRow], with the [Chat.displayName] as the title
 * and the subtitle showing a preview of the members with in it.
 *
 * This is utilized in the [UserDetailsView] to show channels the current user has in common with
 * any other user.
 *
 * @param modifier the Modifier to be applied to this ChannelList
 * @param channels List of channels to show in the list.
 */
@Composable
fun ChannelGroup(modifier: Modifier = Modifier, channels: List<Chat>) {
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