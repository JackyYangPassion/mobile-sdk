/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.ui.theme.IAC
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.ift

@Composable
fun PrivacyPill(_private: Boolean = false) {
    Text((if (_private) "Private" else  "Public").uppercase().annotated(),
        IAC.fonts.mini,
        Modifier.padding(5.dp, 1.dp)
            .background(ift(_private, IAC.theme.colors._private, IAC.theme.colors._public), RoundedCornerShape(8.dp))
            .radius(Int.MAX_VALUE),
        color = Color.White,
    }
}