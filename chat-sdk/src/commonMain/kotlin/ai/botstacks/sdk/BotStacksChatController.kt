package ai.botstacks.sdk

import androidx.compose.runtime.Composable

@Composable
expect fun BotStacksChatController(
    onLogout: () -> Unit
)