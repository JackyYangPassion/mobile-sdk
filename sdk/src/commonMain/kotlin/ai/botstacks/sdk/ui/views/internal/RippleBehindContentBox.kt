package ai.botstacks.sdk.ui.views.internal

import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.utils.FullAlphaRipple
import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun RippleBehindContentBox(
    modifier: Modifier = Modifier,
    rippleColor: Color = LocalBotStacksColorScheme.current.ripple,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRippleTheme provides FullAlphaRipple) {
        Box(
            Modifier
                .padding(horizontal = 10.dp)
                .then(modifier)) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(BotStacks.shapes.medium)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = rippleColor),
                        onClick = onClick
                    )
            )
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}