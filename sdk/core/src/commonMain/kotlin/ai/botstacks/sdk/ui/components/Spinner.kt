/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacksThemeEngine
import ai.botstacks.sdk.internal.utils.IPreviews
import androidx.compose.ui.Modifier

/**
 * Spinner
 *
 * Themed spinner, colored with [ai.botstacks.sdk.ui.theme.Colors.primary].
 *
 * @param modifier The modifier to apply to the spinner.
 */
@Composable
fun Spinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier, color = colorScheme.primary)
}

@IPreviews
@Composable
private fun SpinnerPreview() {
    BotStacksThemeEngine {
        Spinner()
    }
}