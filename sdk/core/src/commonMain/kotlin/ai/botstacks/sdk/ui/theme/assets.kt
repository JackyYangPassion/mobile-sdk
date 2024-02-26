/*
 * Copyright (c) 2023.
 */

@file:OptIn(ExperimentalResourceApi::class)

package ai.botstacks.sdk.ui.theme

import androidx.compose.runtime.Stable
import ai.botstacks.sdk.internal.state.BotStacksChatStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import botstacks.sdk.core.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/**
 * Type determination for empty states for certain components
 */
sealed interface EmptyScreenType {
    /**
     * Empty state for within a chat (no messages)
     */
    data object Messages : EmptyScreenType

    /**
     * Empty state for chat list (no chats)
     */
    data object Chats : EmptyScreenType
}

/**
 * Configuration for an empty state
 *
 * @param image Image to be display above [caption], if provided.
 * @param caption Text to be displayed below [image], if provided.
 * @param type The [EmptyScreenType] for this configuration.
 */
@Stable
data class EmptyScreenConfig(
    val image: @Composable () -> ImageAsset? = { null },
    val caption: String? = null,
    val type: EmptyScreenType
) {
    constructor(asset: ImageAssetIdentifier?, caption: String?, type: EmptyScreenType) : this(
        image = @Composable {
            asset?.toImageAsset()
        },
        caption = caption,
        type = type,
    )
}

internal val EmptyScreenConfig.defaultImage: Painter
    @Composable get() = when (type) {
        EmptyScreenType.Messages -> EmptyChatDefault
        EmptyScreenType.Chats -> EmptyChatsDefault
    }

/**
 * Assets to be utilized and customized for on-brand experience within BotStacks.
 *
 * @param logo An optional logo, will default to the BotStacks Logo if not provided.
 * @param emptyChat Empty state configuration for [EmptyScreenType.Messages]
 * @param emptyChats Empty state configuration for [EmptyScreenType.Chats]
 */
@Stable
data class Assets(
    val logo: ImageAssetIdentifier? = null,
    val emptyChat: EmptyScreenConfig = EmptyScreenConfig(
        caption = "No messages",
        type = EmptyScreenType.Messages
    ),
    val emptyChats: EmptyScreenConfig = EmptyScreenConfig(
        caption = "You haven't added any chats yet",
        type = EmptyScreenType.Chats
    ),
)

internal val LocalBotStacksAssets = staticCompositionLocalOf { Assets() }

internal val EmptyChatDefault: Painter
    @Composable get() = painterResource(Res.drawable.chat_multiple_outline)
internal val EmptyChatsDefault: Painter
    @Composable get() = painterResource(Res.drawable.chat_multiple_outline)
internal val EmptyAllChannelsDefault: Painter
    @Composable get() = painterResource(Res.drawable.empty_all_channels)
