/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.utils.IPreviews

@Composable
fun Avatar(url: String?, size: Double = 35.0, chat: Boolean = false) {
    Box(
        modifier = Modifier
            .circle(size.dp, colors.softBackground), contentAlignment = Alignment.Center
    ) {
        if (url != null) {
            AsyncImage(
                model = url,
                contentDescription = "user profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            if (chat) {
                ChatPlaceholder(modifier = Modifier.fillMaxSize())
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_fill),
                    contentDescription = "user profile picture",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size((18.0 * size / 35.0).dp),
                )
            }
        }
    }
}

@IPreviews
@Composable
fun AvatarPreview() {
    Column {
        Avatar(url = null)

        Avatar(
            url = null, size = 55.0
        )
        Avatar(url = null, chat = true)
    }

}
