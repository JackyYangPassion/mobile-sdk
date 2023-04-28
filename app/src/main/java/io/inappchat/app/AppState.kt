package io.inappchat.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.utils.opbg
import kotlinx.coroutines.launch

@Stable
class AppState {

    var loading by mutableStateOf(true)
    var loggedIn by mutableStateOf(false)
    var ethAccounts = mutableListOf<String>()
    var tokens = mutableListOf<Token>()
    var username by mutableStateOf<String?>(null)
    var signature by mutableStateOf<String?>(null)

    var wc: WalletConnect? = null

    fun load(app: App) =
        opbg({
            InAppChat.load()
            if (!InAppChat.shared.isUserLoggedIn) {
                wc = WalletConnect(app)
                wc?.login()
            }
        })
}

val appState = AppState()