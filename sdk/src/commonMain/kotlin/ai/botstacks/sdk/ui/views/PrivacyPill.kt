/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated
import ai.botstacks.sdk.utils.ift

@Composable
fun PrivacyPill(_private: Boolean = false) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(
                ift(!_private, BotStacks.colorScheme._private, BotStacks.colorScheme._public),
                RoundedCornerShape(8.dp)
            )
            .padding(6.dp, 1.dp)
            .requiredWidth(IntrinsicSize.Max)
    ) {
        Text(
            (if (_private) "Private" else "Public").uppercase().annotated(),
            fonts.caption2,
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