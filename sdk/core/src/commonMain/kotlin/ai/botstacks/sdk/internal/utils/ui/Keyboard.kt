package ai.botstacks.sdk.internal.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
internal expect fun keyboardAsState(): State<Boolean>