/*
 * Copyright (c) 2023.
 */

@file:OptIn(ExperimentalResourceApi::class)

package ai.botstacks.sdk.ui.theme

import androidx.compose.runtime.Stable
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.ui.ImageAsset
import ai.botstacks.sdk.utils.ui.ImageAssetIdentifier
import ai.botstacks.sdk.utils.ui.toImageAsset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

sealed interface EmptyScreenType {
    data object Chat : EmptyScreenType
    data object Chats : EmptyScreenType

    data object Channels : EmptyScreenType

    data object AllChannels : EmptyScreenType
}

@Stable
data class EmptyScreenConfig(
    val image: @Composable () -> ImageAsset? = { null },
    val caption: String? = null,
    val type: EmptyScreenType?
) {
    constructor(asset: ImageAssetIdentifier?, caption: String?, type: EmptyScreenType?) : this(
        image = @Composable {
            asset?.toImageAsset()
        },
        caption = caption,
        type = type,
    )
}

val EmptyScreenConfig.defaultImage: Painter?
    @Composable get() = when (type) {
        EmptyScreenType.AllChannels -> EmptyAllChannelsDefault
        EmptyScreenType.Channels -> EmptyChannelsDefault
        EmptyScreenType.Chat -> EmptyChatDefault
        EmptyScreenType.Chats -> EmptyChatsDefault
        else -> null
    }

@Stable
data class Assets(
    val chat: ImageAssetIdentifier? = null,
    val emptyChat: EmptyScreenConfig = EmptyScreenConfig(
        caption = "Your friends are waiting for you",
        type = EmptyScreenType.Chat
    ),
    val emptyChannels: EmptyScreenConfig = EmptyScreenConfig(
        caption = "No channels yet. Go join one",
        type = EmptyScreenType.Channels
    ),
    val emptyChats: EmptyScreenConfig = EmptyScreenConfig(
        caption = "You haven't added any chats yet",
        type = EmptyScreenType.Chats
    ),
    val emptyAllChannels: EmptyScreenConfig = EmptyScreenConfig(
        caption = "No channels around here yet. Make one",
        type = EmptyScreenType.AllChannels
    ),
) {
    fun list(list: BotStacksChatStore.ChatList) = when (list) {
        BotStacksChatStore.ChatList.dms -> emptyChat
        BotStacksChatStore.ChatList.groups -> emptyChannels
    }
}

internal val LocalBotStacksAssets = staticCompositionLocalOf { Assets() }

internal val EmptyChatDefault: Painter
    @Composable get() = painterResource(Res.Drawables.EmptyChats)
internal val EmptyChannelsDefault: Painter
    @Composable get() = painterResource(Res.Drawables.EmptyChannels)
internal val EmptyChatsDefault: Painter
    @Composable get() = painterResource(Res.Drawables.EmptyChats)
internal val EmptyAllChannelsDefault: Painter
    @Composable get() = painterResource(Res.Drawables.EmptyChannels)
