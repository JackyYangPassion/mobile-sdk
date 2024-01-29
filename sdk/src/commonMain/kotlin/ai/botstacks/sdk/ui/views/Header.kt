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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
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
      .background(colorScheme.softBackground)
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.statusBars)
      .padding(start = 16.dp, end = 8.dp)
      .height(HeaderHeight)
  ) {
    if (back != null)
      HeaderButton(
        back,
        true
      ) {
        Icon(
          painter = Drawables.CaretLeft,
          contentDescription = "back",
          modifier = Modifier.fillMaxSize(1.0f),
          tint = colorScheme.text,
        )
      }
    icon?.invoke()
    Text(
      text = title.annotated(),
      iac = BotStacks.fonts.h1.copy(weight = FontWeight.Bold),
      color = colorScheme.text,
      maxLines = 1
    )
    Spacer(modifier = Modifier.weight(1.0f))
    if (search != null) {
      HeaderButton(onClick = search) {
        Icon(
          painter = Drawables.MagnifyingGlass,
          contentDescription = "Menu",
          tint = BotStacks.colorScheme.text,
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
          painter = Drawables.Plus,
          contentDescription = "Add",
          tint = BotStacks.colorScheme.text,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
    }
    if (menu != null) {
      HeaderButton(onClick = menu) {
        Icon(
          painter = Drawables.MenuOverflow,
          contentDescription = "Menu",
          tint = BotStacks.colorScheme.text,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.size(4.dp))
    }
    if (compose != null) {
      HeaderButton(onClick = compose) {
        Icon(
          painter = Drawables.PaperPlaneTiltFilled,
          contentDescription = "Menu",
          tint = colorScheme.text,
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
        if (transparent) Color.Transparent else colorScheme.softBackground,
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