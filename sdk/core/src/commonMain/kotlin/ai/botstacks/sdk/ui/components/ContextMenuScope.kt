package ai.botstacks.sdk.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

interface ContextMenuScope {

    fun item(
        content: @Composable (PaddingValues) -> Unit
    )

    fun label(
        enabled: Boolean = true,
        onClick: () -> Unit,
        icon: (@Composable () -> Unit)? = null,
        subtitle: @Composable () -> Unit = { },
        title: @Composable () -> Unit,
    )
}