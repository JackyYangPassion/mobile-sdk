package ai.botstacks.sdk.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.theme.FontStyle
import ai.botstacks.sdk.internal.ui.theme.LocalBotStacksColorPalette
import ai.botstacks.sdk.internal.utils.IPreviews
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.type.OnlineStatus
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Shape


/**
 * Badge
 *
 * A "badge" to show a count.
 *
 * This is utilized in the [MessageList] component serving as the date separators,
 * in the [ChatList] component to show unread counts, and in the [ChannelSettingsView] for displaying admins.
 *
 * @param count Count to display in the badge.
 * @param modifier the Modifier to be applied to this Badge
 *
 */
@Composable
fun Badge(
    count: Int,
    modifier: Modifier = Modifier
) {
    val palette = LocalBotStacksColorPalette.current
    if (count > 0) {
        val text = when {
            count in 1..99 -> "$count"
            else -> "99+"
        }

        val contentPadding = when {
            count in 1..99 -> PaddingValues(
                horizontal = BotStacks.dimens.grid.x2,
                vertical = BotStacks.dimens.grid.x1
            )

            else -> PaddingValues(
                horizontal = BotStacks.dimens.grid.x3,
                vertical = BotStacks.dimens.grid.x1
            )
        }
        Badge(
            modifier = modifier,
            label = text,
            contentPadding = contentPadding,
            backgroundColor = palette.primary._800,
            contentColor = Color.White,
            fontStyle = BotStacks.fonts.caption2.copy(size = 10.sp)
        )
    }
}

/**
 * Badge
 *
 * A "badge" to show a label.
 *
 * This is utilized in the [MessageList] component serving as the date separators,
 * in the [ChatList] component to show unread counts, and in the [ChannelSettingsView] for displaying admins.
 *
 * @param label The text to be displayed in the badge.
 * @param modifier the Modifier to be applied to this Badge
 *
 */
@Composable
fun Badge(
    label: String,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier,
        label = label,
        fontStyle = BotStacks.fonts.caption2,
    )
}


/**
 * Badge
 *
 * A "badge" to show a label.
 *
 * This is utilized in the [MessageList] component serving as the date separators,
 * in the [ChatList] component to show unread counts, and in the [ChannelSettingsView] for displaying admins.
 *
 * @param label The text to be displayed in the badge.
 * @param modifier the Modifier to be applied to this Badge
 * @param backgroundColor Background color of the badge.
 * @param contentColor Text color of the badge.
 * @param contentPadding Padding around the badge content.
 * @param fontStyle Style for the text in the badge.
 * @param shape Shape of the badge.
 *
 */
@Composable
fun Badge(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = BotStacks.colorScheme.primary,
    contentColor: Color = BotStacks.colorScheme.onPrimary,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = BotStacks.dimens.grid.x2,
        vertical = BotStacks.dimens.grid.x1
    ),
    fontStyle: FontStyle = BotStacks.fonts.caption2,
    shape: Shape = CircleShape
) {
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .padding(contentPadding),
    ) {
        androidx.compose.material3.Text(
            text = label,
            color = contentColor,
            style = fontStyle.asTextStyle(),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@IPreviews
@Composable
private fun BadgePreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Badge(count = 1)
        Badge(count = 101)
    }
}
