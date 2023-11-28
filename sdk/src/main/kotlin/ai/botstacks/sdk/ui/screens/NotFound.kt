/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.Text
import ai.botstacks.sdk.utils.IPreviews

@Composable
fun NotFound(what: String = "Page", back: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "$what", back = back)
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
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
    BotStacksChatContext {
        NotFound {

        }
    }
}