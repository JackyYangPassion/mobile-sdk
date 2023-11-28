/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.R
import ai.botstacks.sdk.ui.IAC
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.utils.Fn
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated

val HeaderHeight = 50.dp

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
      .background(colors.softBackground)
      .padding(start = 16.dp, end = 8.dp)
      .height(HeaderHeight)
  ) {
    if (back != null)
      HeaderButton(
        back,
        true
      ) {
        Icon(
          painterResource(id = R.drawable.caret_left),
          contentDescription = "back",
          modifier = Modifier.fillMaxSize(1.0f),
          tint = colors.text,
        )
      }
    icon?.invoke()
    Text(
      text = title.annotated(),
      iac = IAC.fonts.title.copy(weight = FontWeight.Bold),
      color = colors.text,
      maxLines = 1
    )
    Spacer(modifier = Modifier.weight(1.0f))
    if (search != null) {
      HeaderButton(onClick = search) {
        Icon(
          painter = painterResource(id = R.drawable.magnifying_glass),
          contentDescription = "Menu",
          tint = IAC.colors.text,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
    }
    if (add != null) {
      HeaderButton(
        add,
      ) {
        Icon(
          painter = painterResource(id = R.drawable.plus),
          contentDescription = "Add",
          tint = IAC.colors.text,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
    }
    if (menu != null) {
      HeaderButton(onClick = menu) {
        Icon(
          painter = painterResource(id = R.drawable.dots_three_vertical_fill),
          contentDescription = "Menu",
          tint = IAC.colors.text,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
    }
    if (compose != null) {
      HeaderButton(onClick = compose) {
        Icon(
          painter = painterResource(id = R.drawable.paper_plane_tilt_fill),
          contentDescription = "Menu",
          tint = colors.text,
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
        if (transparent) Color.Transparent else colors.softBackground,
        CircleShape
      )
      .clickable(onClick = onClick)
      .border(0.dp, Color.Transparent, CircleShape)
  ) {
    icon()
  }
}


@IPreviews
@Composable
fun HeaderPreview() {
  BotStacksChatContext {
    MaterialTheme {
      Header("Title", back = {}, add = {}, menu = {}, search = {}, compose = {})
    }
  }
}