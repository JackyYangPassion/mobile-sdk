package com.g2minus.chatapp

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.messaging.FirebaseMessaging
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.GrowSpacer
import io.inappchat.sdk.ui.views.Space
import io.inappchat.sdk.ui.views.Spinner
import io.inappchat.sdk.utils.IPreviews
import timber.log.Timber


@Composable
fun SignIn(openChat: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true, block = {
        appState.getSignature(context as Activity)
    })
    val onPermission = { havePermission: Boolean ->
        if (havePermission) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                it.result?.let {
                    InAppChat.registerFCMToken(it)
                }
            }.addOnFailureListener {
                Timber.tag("Login").e(it.stackTraceToString())
            }
        }
        openChat()
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onPermission(it)
    }
    LaunchedEffect(key1 = appState.signature, block = {
        if (appState.signature != null) {
            appState.login {
                if (InAppChat.shared.isUserLoggedIn) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onPermission(true)
                    }
                } else {
                    Toast.makeText(context, "Unable to login", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
