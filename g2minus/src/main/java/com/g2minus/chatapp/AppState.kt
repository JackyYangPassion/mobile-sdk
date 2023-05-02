package com.g2minus.chatapp

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.opbg
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@Stable
class AppState {

    var loading by mutableStateOf(true)
    var loggedIn by mutableStateOf(false)
    var ethAccounts = mutableListOf<String>()
    var tokens = mutableListOf<Token>()
    var username by mutableStateOf<String?>(null)
    var signature by mutableStateOf<String?>(null)
    var token by mutableStateOf<Token?>(null)

    lateinit var uriString: String

    var wc: WalletConnect? = null

    fun load(app: App) =
        op({
            bg {
                InAppChat.load()
            }
            if (!InAppChat.shared.isUserLoggedIn) {
                wc = WalletConnect(app)
                uriString = wc!!.connect()
            }
            loggedIn = InAppChat.shared.isUserLoggedIn
            loading = false
        })

    fun login(cb: () -> Unit) {
        val token = this.token ?: return
        op({
            bg {
                InAppChat.shared.nftLogin(
                    contract = "0x41112a2e8626330752a8f9353462edd4771a48a2",
                    address = token.account,
                    tokenID = token.id,
                    signature = signature!!,
                    profilePicture = token.image,
                    username = username
                )
            }
            cb()
        })
    }

    fun getSignature(openDeeplink: (Uri) -> Unit) {
        val token = this.token ?: return
        op({
            val sig = bg {
                wc?.sign("InAppChat NFT Login", token.account, openDeeplink)
            }
            if (sig != null) {
                signature = sig
            }
        })
    }

    fun didConnect(accounts: List<String>) {
        op({
            ethAccounts.addAll(accounts)
            val result = bg {
                var first = true
                accounts.map { account ->
                    if (!first)
                        Thread.sleep(1000)
                    first = false
                    Etherscan.getTokens(account) }
            }.flatten()
            tokens.addAll(result)
        })
    }
}

val appState = AppState()