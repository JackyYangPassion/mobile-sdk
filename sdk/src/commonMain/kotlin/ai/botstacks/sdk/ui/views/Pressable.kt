/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ai.botstacks.sdk.utils.Fn

@Composable
fun Pressable(onClick: Fn, content: @Composable Fn) {
  Box(modifier = Modifier.clickable(onClick = onClick)) {
    content()
  }
}