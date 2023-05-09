package com.g2minus.chatapp

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.GrowSpacer
import io.inappchat.sdk.ui.views.Space
import io.inappchat.sdk.ui.views.Spinner
import io.inappchat.sdk.utils.IPreviews


@Composable
fun SignIn(openChat: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true, block = {
        appState.getSignature(context as Activity)
    })
    Splash(openLogin = {}, openChat = openChat) {
        Column {
            GrowSpacer()
            Space(280f)
            if (appState.signature != null) {
                Spinner()
            } else {
                Space(64f)
            }
            Text(
                if (appState.signature != null)
                    "Signing you in..." else "Open your Ethereum Wallet \n and sign your login message",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            GrowSpacer()
        }
    }
}

@IPreviews
@Composable
fun SignInPreview() {
    InAppChatContext {
        SignIn {

        }
    }
}
