/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionItem(text: String, @DrawableRes icon: Int, divider: Boolean = false, action: () -> Unit) {
  Column {

    ListItem(
      text = {
        Text(
          text = text,
          iac = IAC.fonts.headline,
          color = IAC.colors.text
        )
      },
      icon = {
        Icon(
          painter = painterResource(id = icon),
          contentDescription = text,
          tint = IAC.colors.text,
          modifier = androidx.compose.ui.Modifier.size(25.dp)
        )
      },
      modifier = Modifier.clickable {
        action()
      }
    )
    if (divider) Divider(color = IAC.colors.caption)
  }
}

@IPreviews
@Composable
fun ActionItemPreview() {
  InAppChatContext {
    Column {
      ActionItem(text = "Item", icon = R.drawable.address_book_fill) {
        
      }
    }
  }
}