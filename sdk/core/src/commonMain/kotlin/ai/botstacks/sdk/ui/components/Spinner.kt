/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.internal.utils.IPreviews

@Composable
fun Spinner() {
    CircularProgressIndicator(color = colorScheme.primary)
}

@IPreviews
@Composable
private fun SpinnerPreview() {
    BotStacksThemeEngine {
        Spinner()
    }
}