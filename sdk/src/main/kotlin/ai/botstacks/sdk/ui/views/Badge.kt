package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ai.botstacks.sdk.ui.IAC
import ai.botstacks.sdk.utils.IPreviews


@Composable
fun Badge(count: Int, modifier: Modifier = Modifier) {
  if (count > 0) {
    Box(
      modifier = modifier
          .size(18.dp)
        .background(color = IAC.colors.unread, shape = CircleShape),
      contentAlignment = Alignment.Center
    ) {
      androidx.compose.material3.Text(
        text = count.toString(),
        color = Color.White,
        fontSize = 10.sp,
        modifier = Modifier.align(Alignment.Center)
      )
    }
  }
}

@IPreviews
@Composable
fun BadgePreview() {
  Badge(count = 1)
}