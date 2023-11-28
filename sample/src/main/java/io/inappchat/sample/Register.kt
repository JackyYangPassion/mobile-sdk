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
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.messaging.FirebaseMessaging
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.state.Upload
import ai.botstacks.sdk.ui.IAC
import ai.botstacks.sdk.ui.views.AssetPicker
import ai.botstacks.sdk.ui.views.Avatar
import ai.botstacks.sdk.ui.views.Space
import ai.botstacks.sdk.ui.views.TextInput
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun Register(openChat: () -> Unit, login: () -> Unit) {
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
    var pickImage by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        scope.launch { terms.hide() }
    }
    var profilePicture by remember { mutableStateOf<Upload?>(null) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val canLogin =
        isValidEmail(email) && username.length >= 4 && password.length > 4
    val register = {
        if (canLogin) {
            Log.v("InAppChat-Sample", "Login Click")
            scope.launch {
                BotStacksChat.shared.register(
                    email = email,
                    password = password,
                    displayName = username,
                    profilePicture = profilePicture?.url
                )
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
                .background(IAC.theme.dark.background)
                .padding(24.dp)
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
        ) {
            item {
                InAppChatHeader()
                Space(20f)
                Text("Register", color = Color.White, fontSize = 24.sp)
                Space(12f)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            pickImage = true
                        }
                ) {
                    Avatar(url = profilePicture?.uri?.toString(), size = 75.0)
                }
                Space(12f)
                val autofillUsername =
                    AutoFillRequestHandler(autofillTypes = listOf(AutofillType.NewUsername),
                        onFill = {
                            username = it
                        }
                    )
                TextInput(
                    text = username,
                    placeholder = "Username",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    maxLines = 1,
                    onChange = {
                        username = it
                    },
                    inputModifier = Modifier
                        .connectNode(handler = autofillUsername)
                        .defaultFocusChangeAutoFill(handler = autofillUsername)
                )
                Space(12f)
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
                        register()
                    }),
                    visualTransformation = PasswordVisualTransformation(),
                    onChange = {
                        password = it
                    },
                    inputModifier = Modifier
                        .connectNode(autofillPassword)
                        .defaultFocusChangeAutoFill(autofillPassword)
                )

                ElevatedButton(
                    onClick = register,
                    enabled = !BotStacksChat.shared.loggingIn && canLogin,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = IAC.colors.primary,
                        contentColor = Color.White
                    ),
                ) {
                    Text(text = "Register")
                }
                Space(24f)
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .clickable {
                            register()
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("Already have an account? ")

                        pushStringAnnotation(
                            tag = "login",
                            annotation = ""
                        )
                        withStyle(
                            style = SpanStyle(
                                color = IAC.theme.dark.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Login")
                        }
                    }
                    Text(annotatedString, color = IAC.theme.dark.text, textAlign = TextAlign.Center)
                }
                Space(32f)
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
                                color = IAC.theme.dark.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("terms of service")
                        }
                    }
                    Text(annotatedString, color = IAC.theme.dark.text, textAlign = TextAlign.Center)
                }
            }
        }
        if (pickImage) {
            AssetPicker(video = false, onUri = {
                profilePicture = Upload(uri = it)
                pickImage = false
            }) {
                pickImage = false
            }
        }
    }
}