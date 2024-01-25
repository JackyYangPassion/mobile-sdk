package ai.botstacks.sdk.ui.utils

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
object FullAlphaRipple : RippleTheme {
    @Composable
    override fun defaultColor() = LocalContentColor.current

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(1f, 1f, 1f, 1f)
}