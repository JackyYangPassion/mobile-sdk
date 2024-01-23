package ai.botstacks.sdk.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize

fun Modifier.measured(block: (DpSize) -> Unit): Modifier = composed {
    val density = LocalDensity.current
    onPlaced { block(it.size.toDp(density)) }
}