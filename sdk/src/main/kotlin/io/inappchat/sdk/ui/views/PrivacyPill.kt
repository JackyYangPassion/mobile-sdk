/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.ift

@Composable
fun PrivacyPill(_private: Boolean = false) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
        .background(
            ift(_private, IAC.theme.colors._private, IAC.theme.colors._public),
            RoundedCornerShape(8.dp)
        )
        .padding(6.dp, 1.dp)
  ) {
    Text(
      (if (_private) "Private" else "Public").uppercase().annotated(),
      fonts.mini,
      color = Color.White,
    )
  }
}

@IPreviews
@Composable
fun PrivacyPillPreview() {
  Column {
    PrivacyPill(true)
    PrivacyPill(false)
  }
}