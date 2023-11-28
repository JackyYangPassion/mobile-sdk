package com.g2minus.chatapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.op
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Stable
class AppState {

    var loading by mutableStateOf(true)
    var loggedIn by mutableStateOf(false)
    var ethAccounts = mutableListOf<String>()
    var tokens = mutableListOf<Token>()
    var didLoadTokens by mutableStateOf(false)
    var username by mutableStateOf<String?>(null)
    var signature by mutableStateOf<String?>(null)
    var token by mutableStateOf<Token?>(null)

    lateinit var uriString: String

    var wc: WalletConnect? = null

    fun load(app: App) =
            op({
                println("Load InAppChat")
                bg {
                    BotStacksChat.load()
                }
                println("InAppChat loaded")
                if (!BotStacksChat.shared.isUserLoggedIn) {
                    println("Connecting to wallet connect")
                    wc = bg { WalletConnect(app) }
                    println("Created wallet")
                    uriString = bg {
                        wc!!.connect()
                    }
                }
                loggedIn = BotStacksChat.shared.isUserLoggedIn
                loading = false
                println("Finish loading")
            })

    fun login(cb: () -> Unit) {
        val token = this.token ?: return
        val username = this.username ?: return
        op({
            bg {
                BotStacksChat.shared.nftLogin(
                        wallet = token.account,
                        tokenID = token.id,
                        signature = signature!!,
                        picture = token.image,
                        username = username,
                )
            }
            cb()
        })
    }

    fun didConnect(accounts: List<String>) {
        op({
            didLoadTokens = false
            ethAccounts.addAll(accounts)
            val result = bg {
                var first = true
                accounts.map { account ->
                    if (!first)
                        delay(1000)
                    first = false
                    Etherscan.getTokens(account)
                }
            }.flatten()
            tokens.addAll(result)
            didLoadTokens = true
        })
    }

    fun getSignature(activity: Activity) {
        val token = this.token ?: return
        try {
            BotStacksChat.shared.scope.launch {
                signature = bg {
                    wc?.sign(
                            "Login to G2Minus with Poison Pog ${token.id} and username $username",
                            token.account
                    ) {
                        activity.startActivity(Intent(Intent.ACTION_VIEW, it))
                    }
                }
            }
        } catch (err: Error) {
            Log.v("InAppChat", "Error " + err.stackTraceToString())
        }
    }
}

val appState = AppState()