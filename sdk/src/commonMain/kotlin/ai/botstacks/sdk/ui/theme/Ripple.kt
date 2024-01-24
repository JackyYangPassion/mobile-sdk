package ai.botstacks.sdk.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class BotStacksRippleTheme(private val rippleColor: Color, private val isDark: Boolean) :
    RippleTheme {
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            rippleColor,
            lightTheme = !isDark
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            rippleColor.copy(alpha = 0.75f),
            lightTheme = !isDark
        )
}