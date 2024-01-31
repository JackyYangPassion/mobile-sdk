package ai.botstacks.sdk.ui.components

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin

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


@ExperimentalAnimationApi
@Composable
expect fun ContextMenu(
    key: Any? = null,
    visible : Boolean,
    onDismissRequest : () -> Unit,
    enterTransition : EnterTransition = scaleIn(
        animationSpec = tween(
            durationMillis = 100
        ),
        transformOrigin = TransformOrigin(.5f, 0f)
    ),
    exitTransition: ExitTransition = scaleOut(
        animationSpec = tween(
            durationMillis = 100
        ),
        transformOrigin = TransformOrigin(.5f, 0f)
    ),
    alignment: Alignment.Horizontal = Alignment.End,
    menu : ContextMenuScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)