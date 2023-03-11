/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.theme.IAC
import io.inappchat.sdk.ui.theme.IACColors
import io.inappchat.sdk.ui.theme.InAppChatTheme
import io.inappchat.sdk.utils.Fn
import io.inappchat.sdk.utils.annotated

val HeaderHeight = 44.dp

@Composable
fun Header(
    title: String,
    icon: @Composable Fn? = null,
    search: Fn? = null,
    compose: Fn? = null,
    back: Fn? = null,
    menu: Fn? = null,
    right: @Composable Fn? = null,
    add: Fn? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(4.dp, 2.dp)
    ) {
        if (back != null)
            HeaderButton(
                back,
                true
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "back",
                    modifier = Modifier.fillMaxSize(1.0f)
                )
            }
        icon?.invoke()
        Text(text = title.annotated(), iac = IAC.fonts.title.copy(weight = FontWeight.Bold))
        Spacer(modifier = Modifier.weight(1.0f))
        if (search != null) {
            HeaderButton(onClick = search) {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = "Menu",
                    tint = IAC.colors.text
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        if (add != null) {
            HeaderButton(
                add,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        if (menu != null) {
            HeaderButton(onClick = menu) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        if (compose != null) {
            HeaderButton(onClick = compose) {
                Icon(
                    painter = painterResource(id = R.drawable.paper_plane_tilt_fill),
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        right?.invoke()
    }
}

@Composable
fun HeaderButton(onClick: Fn, transparent: Boolean = false, icon: @Composable Fn) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(0.dp)
            .size(30.dp)
            .background(
                if (transparent) Color.Transparent else IACColors.current.softBackground,
                CircleShape
            )
            .clickable(onClick = onClick)
            .border(0.dp, Color.Transparent, CircleShape)
    ) {
        icon()
    }
}


@Preview
@Composable
fun HeaderPreview() {
    InAppChatTheme {
        MaterialTheme {
            Header("Title", back = {}, add = {}, menu = {}, search = {}, compose = {})
        }
    }
}