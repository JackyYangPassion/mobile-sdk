package io.inappchat.sample

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.views.Space
import io.inappchat.sdk.ui.views.TextInput
import io.inappchat.sdk.utils.launch
import timber.log.Timber

val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
fun isValidEmail(email: String): Boolean {
    return email.matches(emailRegex)
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(openChat: () -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
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
    val activity = LocalContext.current as Activity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordFocus = remember { FocusRequester() }
    val canLogin = isValidEmail(email) && password.length > 4
    val login = {
        Log.v("InAppChat-Sample", "Login Click")
        launch {
            App.app.login(email, password)
            Log.v("InAppChat-Sample", "Finish login")
            if (InAppChat.shared.isUserLoggedIn) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .background(theme.dark.background)
            .fillMaxSize()
            .padding(24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
    ) {
        item {
            InAppChatLogo()
            Space(60f)
            val autofillEmail =
                AutoFillRequestHandler(autofillTypes = listOf(AutofillType.EmailAddress),
                    onFill = {
                        email = it
                    }
                )
            TextInput(
                text = email,
                placeholder = "Email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { passwordFocus.captureFocus() }),
                maxLines = 1,
                onChange = {
                    email = it
                },
                inputModifier = Modifier
                    .connectNode(handler = autofillEmail)
                    .defaultFocusChangeAutoFill(handler = autofillEmail)
            )
            val autofillPassword =
                AutoFillRequestHandler(autofillTypes = listOf(AutofillType.Password),
                    onFill = {
                        email = it
                    }
                )
            TextInput(
                text = password,
                placeholder = "Password",
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(onGo = {
                    login()
                }),
                onChange = {
                    password = it
                },
                inputModifier = Modifier
                    .defaultFocusChangeAutoFill(autofillPassword)
                    .connectNode(autofillPassword)
            )

            ElevatedButton(
                onClick = login,
                enabled = !InAppChat.shared.loggingIn,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = colors.primary,
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Login")
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    MaterialTheme {
        Login(openChat = {})
    }
}