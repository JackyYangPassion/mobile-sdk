/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.ui.resources.Res
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Stable
data class EmptyScreenConfig(
    val image: @Composable () -> Painter? = { null },
    val caption: String? = null
)

@OptIn(ExperimentalResourceApi::class)
@Stable
data class Assets(
    @DrawableRes val chat: Int? = null,
    val emptyChat: EmptyScreenConfig = EmptyScreenConfig(
        { painterResource(Res.Drawables.EmptyChats) },
        "Your friends are waiting for you"
    ),
    val emptyChannels: EmptyScreenConfig = EmptyScreenConfig(
        { painterResource(Res.Drawables.EmptyChannels) },
        "No channels yet. Go join one"
    ),
    val emptyChats: EmptyScreenConfig = EmptyScreenConfig(
        { painterResource(Res.Drawables.EmptyChats) },
        "You haven't added any chats yet"
    ),
    val emptyAllChannels: EmptyScreenConfig = EmptyScreenConfig(
        { painterResource(Res.Drawables.EmptyChannels) },
        "No channels around here yet. Make one"
    )
) {
    fun list(list: BotStacksChatStore.ChatList) = when (list) {
        BotStacksChatStore.ChatList.dms -> emptyChat
        BotStacksChatStore.ChatList.groups -> emptyChannels
    }
}

internal val LocalBotStacksAssets = staticCompositionLocalOf { Assets() }
