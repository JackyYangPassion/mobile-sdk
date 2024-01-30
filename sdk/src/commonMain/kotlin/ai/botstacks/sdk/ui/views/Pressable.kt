/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import ai.botstacks.sdk.extensions.unboundedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ai.botstacks.sdk.utils.Fn
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun Pressable(
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