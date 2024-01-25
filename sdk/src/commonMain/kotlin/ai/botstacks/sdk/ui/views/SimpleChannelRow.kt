package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.theme.FullAlphaRipple
import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import ai.botstacks.sdk.utils.IPreviews
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SimpleChannelRow(
    modifier: Modifier = Modifier,
    chat: Chat,
    onClick: () -> Unit,
) {
    SimpleChannelRow(
        modifier = modifier,
        imageUrls = listOf(chat.image),
        name = chat.displayName,
        onClick = onClick
    )
}

@Composable
fun SimpleChannelRow(
    modifier: Modifier,
    imageUrls: List<String?>,
    name: String,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalRippleTheme provides FullAlphaRipple) {
        Row(
            modifier = Modifier
                .background(BotStacks.colorScheme.background)
                .padding(horizontal = 10.dp)
                .clip(BotStacks.shapes.medium)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = LocalBotStacksColorScheme.current.ripple),
                    onClick = onClick
                )
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Avatar(type = AvatarType.Channel(imageUrls))
            Text(
                text = name,
                fontStyle = BotStacks.fonts.body1,
                color = BotStacks.colorScheme.text
            )
        }
    }
}

@IPreviews
@Composable
fun ChannelRow_Preview() {
    BotStacksChatContext {
        Column(Modifier.background(BotStacks.colorScheme.background)) {
            SimpleChannelRow(
                modifier = Modifier
                    .fillMaxWidth(),
                imageUrls = listOf(null),
                name = "Channel Name",
                onClick = {}

            )
            SimpleChannelRow(
                modifier = Modifier
                    .fillMaxWidth(),
                imageUrls = listOf(null),
                name = "Channel Name",
                onClick = {}

            )
        }
    }
}