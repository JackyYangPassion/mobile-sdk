package ai.botstacks.sdk.internal.ui.resources

import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import androidx.compose.runtime.Composable
import ai.botstacks.`chat-sdk`.generated.resources.Res
import dev.icerock.moko.resources.ImageResource


internal val Res.images.botstacks_logo_daynight: ImageResource
    @Composable get() = if (LocalBotStacksColorScheme.current.isDark) botstacks_logo_dark else botstacks_logo