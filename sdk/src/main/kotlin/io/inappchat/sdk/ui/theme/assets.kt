/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import io.inappchat.sdk.R
import io.inappchat.sdk.state.InAppChatStore

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
    fun list(list: InAppChatStore.List) = when (list) {
        InAppChatStore.List.dms -> emptyChat
        InAppChatStore.List.groups -> emptyChannels
    }
}
