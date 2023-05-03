/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews

@Composable
fun Spinner() {
    CircularProgressIndicator(color = colors.primary)
}

@Composable
fun SpinnerList() {
    Box(
        modifier = Modifier
          .padding(64.dp)
          .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = colors.primary)
    }
}

@IPreviews
@Composable
fun SpinnerPreview() {
    InAppChatContext {
        Spinner()
    }
}