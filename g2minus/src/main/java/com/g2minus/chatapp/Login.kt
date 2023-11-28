package com.g2minus.chatapp

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.messaging.FirebaseMessaging
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.ui.views.Spinner
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun Login(openChat: () -> Unit, openCreateProfile: () -> Unit) {
    val onPermission = { havePermission: Boolean ->
        if (havePermission) {
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
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onPermission(it)
    }
    val activity = LocalContext.current
    val isEthLogin =
        !BotStacksChat.shared.config?.optString("loginType", "email").equals("email")
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val haveAccounts = appState.ethAccounts.isNotEmpty()
    LaunchedEffect(key1 = haveAccounts, block = {
        if (haveAccounts) {
            state.hide()
        }
    })
    val tokensLoading = !appState.didLoadTokens
    val haveTokens = appState.tokens.isNotEmpty()
    LaunchedEffect(key1 = haveTokens, block = {
        if (haveTokens) {
            openCreateProfile()
        }
    })
    LaunchedEffect(key1 = tokensLoading, block = {
        if (!tokensLoading && !haveTokens && haveAccounts) {
            Toast.makeText(
                activity,
                "No tokens found with connected wallets. Please connect an eth wallet containing Poison Pogs.",
                Toast.LENGTH_LONG
            )
        }
    })
    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            Box(contentAlignment = Alignment.TopEnd) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp, 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "Login to G2Minus",
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Scan this QR code with a WalletConnect enabled wallet or select an option below.",
                            textAlign = TextAlign.Center
                        )
                        QRCode(
                            qrData = if (LocalInspectionMode.current) "abc" else appState.uriString,
                            primaryColor = Color.Blue,
                            logoColor = Color.White
                        )
                    }
                    items(walletProviders, { it.name }) {
                        ElevatedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

                            }) {
                            Image(
                                painter = painterResource(id = it.image),
                                contentDescription = it.name,
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                }
                IconButton(onClick = {
                    scope.launch {
                        state.hide()
                    }
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Close Ethereum Login")
                }
            }
        }) {
        Splash(openLogin = {}, openChat = openChat) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (haveAccounts && tokensLoading) {
                    Spinner()
                    Text("Fetching Tokens", color = Color.White)
                } else {
                    ElevatedButton(onClick = {
                        scope.launch {
                            state.show()
                        }
                    }, enabled = !BotStacksChat.shared.loggingIn, modifier = Modifier.fillMaxWidth()) {
                        if (isEthLogin) {
                            Icon(
                                painter = painterResource(id = R.drawable.eth),
                                contentDescription = "Ethereum Login",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(text = "Login with Ethereum")
                        } else {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun LoginPreview() {
    MaterialTheme {
        Login(openChat = {}, openCreateProfile = {})
    }
}