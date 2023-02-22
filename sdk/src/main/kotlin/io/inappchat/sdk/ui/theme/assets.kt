/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.theme

import androidx.compose.runtime.Stable
import kotlin.reflect.jvm.internal.impl.resolve.scopes.MemberScope.Empty

@Stable
data class EmptyScreenConfig(val image: Int? = null, val caption: String? = null)

@Stable
data class Assets(
    val group: Int? = null,
    val emptyChat: EmptyScreenConfig = EmptyScreenConfig(),
    val emptyChannels: EmptyScreenConfig = EmptyScreenConfig(),
    val emptyThreads: EmptyScreenConfig = EmptyScreenConfig(),
    val emptyAllChannels: EmptyScreenConfig = EmptyScreenConfig()
) {
}
