package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.utils.IPreviews
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding


@Composable
fun Badge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count > 0) {
        val text = when {
            count in 1..99 -> "$count"
            else -> "99+"
        }

        val contentPadding = when {
            count in 1 .. 99 -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            else -> PaddingValues(horizontal = 10.dp, vertical = 4.dp)
        }
        Box(
            modifier = modifier
                .background(color = BotStacks.colors.Primary._800, shape = CircleShape)
                .padding(contentPadding),
        ) {
            androidx.compose.material3.Text(
                text = text,
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@IPreviews
@Composable
fun BadgePreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Badge(count = 1)
        Badge(count = 101)
    }
}
