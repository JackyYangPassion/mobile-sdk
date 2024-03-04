/*
 * Copyright (c) 2023.
 */

@file:OptIn(ExperimentalResourceApi::class)

package ai.botstacks.sdk.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter
import ai.botstacks.`chat-sdk`.generated.resources.Res
import ai.botstacks.`chat-sdk`.generated.resources.chat_multiple_outline
import ai.botstacks.`chat-sdk`.generated.resources.empty_all_channels
import ai.botstacks.sdk.internal.ui.resources.botstacks_logo_daynight
import org.jetbrains.compose.resources.DrawableResource
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
sealed class EmptyScreenConfig(
    open val image: @Composable () -> ImageAsset? = { null },
    open val caption: String? = null,
    open val type: EmptyScreenType
) {
    data class Chats(
        override val image: @Composable () -> ImageAsset? = { null },
        override val caption: String? = null,
    ) : EmptyScreenConfig(
        image = image,
        caption = caption,
        type = EmptyScreenType.Chats,
    )

    data class Messages(
        override val image: @Composable () -> ImageAsset? = { null },
        override val caption: String? = null
    ) : EmptyScreenConfig(
        image = image,
        caption = caption,
        type = EmptyScreenType.Messages,
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
    val emptyChat: EmptyScreenConfig.Messages = EmptyScreenConfig.Messages(
        caption = "No messages",
    ),
    val emptyChats: EmptyScreenConfig.Chats = EmptyScreenConfig.Chats(
        caption = "You haven't added any chats yet",
    ),
){
    var logoResource: DrawableResource? = null
}

val LocalBotStacksAssets = staticCompositionLocalOf { Assets() }

@Composable
internal fun Assets.logoPainter(): Painter? {
    return logoResource?.let { painterResource(it) }
        ?: logo?.let { painterImageAsset(it) }
}

internal val EmptyChatDefault: Painter
    @Composable get() = painterResource(Res.drawable.chat_multiple_outline)
internal val EmptyChatsDefault: Painter
    @Composable get() = painterResource(Res.drawable.chat_multiple_outline)
internal val EmptyAllChannelsDefault: Painter
    @Composable get() = painterResource(Res.drawable.empty_all_channels)
