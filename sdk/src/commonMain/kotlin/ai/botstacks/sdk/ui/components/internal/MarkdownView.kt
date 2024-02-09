package ai.botstacks.sdk.ui.components.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MarkdownView(modifier: Modifier = Modifier, content: String, isCurrentUser: Boolean)