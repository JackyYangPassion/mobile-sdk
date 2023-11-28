package ai.botstacks.sample

import android.Manifest
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.messaging.FirebaseMessaging
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.theme
import ai.botstacks.sdk.ui.views.Space
import ai.botstacks.sdk.ui.views.TextInput
import kotlinx.coroutines.launch
import timber.log.Timber

val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
fun isValidEmail(email: String): Boolean {
    return email.matches(emailRegex)
}


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun Login(openChat: () -> Unit, register: () -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                it.result?.let {
                    BotStacksChat.registerFCMToken(it)
                }
            }.addOnFailureListener {
                Timber.tag("Login").e(it.stackTraceToString())
            }
        }
        openChat()
    }
    val scope = rememberCoroutineScope()
    val terms = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    BackHandler(enabled = true) {
        scope.launch { terms.hide() }
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val passwordFocus = remember { FocusRequester() }
    val canLogin = isValidEmail(email) && password.length > 4
    val login = {
        if (canLogin) {
            Log.v("InAppChat-Sample", "Login Click")
            scope.launch {
                BotStacksChat.shared.login(email, password)
                Log.v("InAppChat-Sample", "Finish login")
                if (BotStacksChat.shared.isUserLoggedIn) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
    ModalBottomSheetLayout(sheetContent = {
        BrowserModal(url = "https://inappchat.io/terms-of-services") {
            scope.launch { terms.hide() }
        }
    }, sheetState = terms) {
        LazyColumn(
            modifier = Modifier
                .background(theme.dark.background)
                .padding(24.dp)
                .imePadding()
                .fillMaxSize(),
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
                Space(12f)
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
                    visualTransformation = PasswordVisualTransformation(),
                    onChange = {
                        password = it
                    },
                    inputModifier = Modifier
                        .connectNode(autofillPassword)
                        .defaultFocusChangeAutoFill(autofillPassword),
                    focusRequester = passwordFocus
                )

                ElevatedButton(
                    onClick = login,
                    enabled = !BotStacksChat.shared.loggingIn && canLogin,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = colors.primary,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Login")
                }
                Space(4f)
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clickable {
                            register()
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("Don't have an account? ")

                        pushStringAnnotation(
                            tag = "register",
                            annotation = ""
                        )
                        withStyle(
                            style = SpanStyle(
                                color = theme.dark.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Create one")
                        }
                    }
                    Text(annotatedString, color = theme.dark.text, textAlign = TextAlign.Center)
                }
                Space(16f)
                Row(
                    modifier = Modifier
                        .clickable {
                            scope.launch { terms.show() }
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("By logging in, you agree to the InAppChat ")

                        pushStringAnnotation(
                            tag = "terms of service",
                            annotation = "https://google.com/policy"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = theme.dark.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("terms of service")
                        }
                    }
                    Text(annotatedString, color = theme.dark.text, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    MaterialTheme {
        Login(openChat = {}, register = {})
    }
}