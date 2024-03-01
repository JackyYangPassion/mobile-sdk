package ai.botstacks.sdk.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shape definitions for Components.
 *
 * @param small A shape style with 4 same-sized corners whose size are bigger than RectangleShape and smaller than Shapes.medium.
 * @param medium A shape style with 4 same-sized corners whose size are bigger than Shapes.small and smaller than Shapes.large.
 * @param large A shape style with 4 same-sized corners whose size are bigger than Shapes.medium and smaller than CircleShape.
 */
data class ShapeDefinitions(
    val small: Dp = 6.dp,
    val medium: Dp = 10.dp,
    val large: Dp = 20.dp,
)