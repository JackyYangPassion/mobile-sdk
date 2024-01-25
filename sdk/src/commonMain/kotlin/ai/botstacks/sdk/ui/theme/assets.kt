/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import ai.botstacks.sdk.R
import ai.botstacks.sdk.state.BotStacksChatStore
import androidx.compose.runtime.staticCompositionLocalOf

@Stable
data class EmptyScreenConfig(@DrawableRes val image: Int? = null, val caption: String? = null)

@Stable
data class Assets(
    @DrawableRes val chat: Int? = null,
    val emptyChat: EmptyScreenConfig = EmptyScreenConfig(
        R.drawable.empty_chats,
        "Your friends are waiting for you"
    ),
    val emptyChannels: EmptyScreenConfig = EmptyScreenConfig(
        R.drawable.empty_channels,
        "No channels yet. Go join one"
    ),
    val emptyChats: EmptyScreenConfig = EmptyScreenConfig(
        R.drawable.empty_chats,
        "You haven't added any chats yet"
    ),
    val emptyAllChannels: EmptyScreenConfig = EmptyScreenConfig(
        R.drawable.empty_all_channels,
        "No channels around here yet. Make one"
    )
) {
    fun list(list: BotStacksChatStore.List) = when (list) {
        BotStacksChatStore.List.dms -> emptyChat
        BotStacksChatStore.List.groups -> emptyChannels
    }
}

internal val LocalBotStacksAssets = staticCompositionLocalOf { Assets() }
