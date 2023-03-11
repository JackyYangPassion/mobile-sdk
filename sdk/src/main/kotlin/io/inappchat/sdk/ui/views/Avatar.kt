/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.theme.IAC

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Avatar(url: String?, size: Double = 35.0, group: Boolean = false) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(IAC.colors.softBackground, CircleShape), contentAlignment = Alignment.Center
    ) {
        if (url != null) {
            GlideImage(
                model = url,
                contentDescription = "user profile picture",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            if (group) {
                GroupPlaceholder(modifier = Modifier.fillMaxSize())
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

@Preview
@Composable
fun AvatarPreview() {
    Column {
        Avatar(url = null)
        
        Avatar(
            url = null, size = 55.0
        )
        Avatar(url = null, group = true)
    }

}
