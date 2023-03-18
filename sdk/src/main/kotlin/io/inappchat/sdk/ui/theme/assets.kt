/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Chats

@Stable
data class EmptyScreenConfig(@DrawableRes val image: Int? = null, val caption: String? = null)

@Stable
data class Assets(
  @DrawableRes val group: Int? = null,
  val emptyChat: EmptyScreenConfig = EmptyScreenConfig(
    R.drawable.empty_chats,
    "Your friends are waiting for you"
  ),
  val emptyChannels: EmptyScreenConfig = EmptyScreenConfig(
    R.drawable.empty_channels,
    "No channels yet. Go join one"
  ),
  val emptyThreads: EmptyScreenConfig = EmptyScreenConfig(
    R.drawable.empty_threads,
    "You haven't added any threads yet"
  ),
  val emptyAllChannels: EmptyScreenConfig = EmptyScreenConfig(
    R.drawable.empty_all_channels,
    "No channels around here yet. Make one"
  )
) {
  fun list(list: Chats.List) = when (list) {
    Chats.List.users -> emptyChat
    Chats.List.groups -> emptyChannels
    Chats.List.threads -> emptyThreads
  }
}
