@file:OptIn(ExperimentalResourceApi::class)

package ai.botstacks.sdk.internal.ui.resources

import ai.botstacks.sdk.ui.theme.LocalBotStacksColorScheme
import androidx.compose.runtime.Composable
import ai.botstacks.`chat-sdk`.generated.resources.Res
import ai.botstacks.`chat-sdk`.generated.resources.botstacks_logo
import ai.botstacks.`chat-sdk`.generated.resources.botstacks_logo_dark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi


internal val Res.drawable.botstacks_logo_daynight: DrawableResource
    @Composable get() = if (LocalBotStacksColorScheme.current.isDark) botstacks_logo_dark else botstacks_logo