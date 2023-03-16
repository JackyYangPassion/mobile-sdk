package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.inappchat.sdk.ui.IAC


@Composable
fun Badge(count: Int) {
    if (count > 0) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color = IAC.colors.unread, shape = CircleShape)
                .padding(2.dp)
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

@IPreview
@Composable
fun BadgePreview() {
    Badge(count = 1)
}