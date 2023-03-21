/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.ui.views.Text
import io.inappchat.sdk.utils.IPreviews

@Composable
fun NotFound(what: String = "Page", back: () -> Unit) {
  Column(modifier = Modifier.fillMaxSize()) {
    Header(title = "$what", back = back)
    Column(modifier = Modifier.padding(32.dp)) {
      Text(
        text = "The page you were looking for could not be found",
        iac = fonts.title2,
        color = colors.text
      )
    }
  }
}

@IPreviews
@Composable
fun NotFoundPreview() {
  InAppChatContext {
    NotFound {
      
    }
  }
}