/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.fonts
import ai.botstacks.sdk.ui.IAC.theme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.theme.EmptyScreenConfig
import ai.botstacks.sdk.utils.IPreviews


data class CTA(@DrawableRes val icon: Int?, val text: String, val to: () -> Unit)


@Composable
fun EmptyListView(
  config: EmptyScreenConfig,
  cta: CTA?
) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Spacer(modifier = Modifier.weight(1f))
      config.image?.let {
        Image(painter = painterResource(id = it), contentDescription = "empty list")
      }
      Space(8f)
      config.caption?.let {
        Text(
          text = it,
          iac = fonts.title2.copy(weight = FontWeight.Normal),
          textAlign = TextAlign.Center,
          color = colors.text
        )
      }
      Spacer(modifier = Modifier.weight(1f))
      cta?.let {
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier
          .clickable { it.to() }
          .padding(32.dp, 0.dp)) {
          Row(
            modifier = Modifier
              .height(60.dp)
              .border(2.dp, colors.text, RoundedCornerShape(30.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Spacer(modifier = Modifier.weight(1f))
            it.icon?.let {
              Image(
                painter = painterResource(id = it),
                contentDescription = "call to action",
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(colors.text)
              )
            }
            Text(text = it.text.uppercase(), iac = fonts.headline, color = colors.text)
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
    }
  }
}

@IPreviews
@Composable
fun EmptyListViewPreview() {
  BotStacksChatContext {
    EmptyListView(config = theme.assets.emptyAllChannels, cta = CTA(null, "Create a Channel", {}))
  }
}