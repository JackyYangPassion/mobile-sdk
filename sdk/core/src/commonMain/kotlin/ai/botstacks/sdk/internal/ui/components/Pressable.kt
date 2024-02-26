/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.internal.ui.components

import ai.botstacks.sdk.internal.utils.ui.unboundedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ai.botstacks.sdk.internal.utils.Fn

@Composable
internal fun Pressable(
    enabled: Boolean = true,
    onClick: Fn,
    content: @Composable Fn
) {
    Box(
        modifier = Modifier
            .unboundedClickable(enabled = enabled, onClick = onClick)
    ) {
        content()
    }
}