/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import coil.ImageLoader
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.async
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.opbg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Stable
class InAppChat private constructor() {

    val TAG = "InAppChat"

    lateinit var appContext: Context
    lateinit var prefs: SharedPreferences
    lateinit var apiKey: String
    lateinit var imageLoader: ImageLoader

    fun setup(appContext: Context, apiKey: String, delayLoad: Boolean = false) {
        this.appContext = appContext
        this.prefs = appContext.getSharedPreferences("inappchat", Context.MODE_PRIVATE)
        this.apiKey = apiKey
        InAppChatStore.current.init()
        InAppChatStore.current.contacts.requestContacts =
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        Log.v(TAG, "InAppChat Setup")
        if (!delayLoad) {
            Log.v("InAppChat", "Launch load")
            scope.launch {
                Log.v(TAG, "Launching load in bg")
                bg {
                    Log.v(TAG, "Loading app")
                    load()
                }
            }
        }
    }

    val scope = CoroutineScope(Dispatchers.Main)

    var loaded by mutableStateOf(false)
    var isUserLoggedIn by mutableStateOf(false)
    private var appMeta: JSONObject? = null
    var config: JSONObject? = null

    private var didStartLoading = false
    suspend fun load() {
        Log.v("InAppChat", "Start load")
        if (!this::apiKey.isInitialized) {
            throw Error("You must initialize InAppChat with InAppChat.init before calling load")
        }
        if (didStartLoading) throw Error("SDK Already initialized")
        didStartLoading = true
        InAppChatStore.current.loadAsync()
        isUserLoggedIn = InAppChatStore.current.currentUserID != null
        loaded = true
    }

    var loggingIn by mutableStateOf(false)
    suspend fun login(
        accessToken: String? = null,
        userId: String,
        username: String,
        displayName: String?,
        picture: String?
    ) {
        if (loggingIn) return
        loggingIn = true
        try {
            API.login(
                accessToken = accessToken,
                userId = userId,
                username = username,
                displayName = displayName,
                picture = picture
            )
            isUserLoggedIn = InAppChatStore.current.currentUserID != null
        } catch (err: Error) {
            Monitoring.error(err)
        }
        loggingIn = false
    }

    suspend fun nftLogin(
        wallet: String,
        tokenID: String,
        username: String,
        signature: String,
        picture: String?,
        displayName: String? = null
    ) {
        if (loggingIn) {
            return
        }
        loggingIn = true
        try {
            API.nftLogin(
                wallet = wallet,
                tokenID = tokenID,
                signature = signature,
                picture = picture,
                username = username,
                displayName = displayName
            )
            isUserLoggedIn = InAppChatStore.current.currentUserID != null
        } catch (err: Error) {
            Monitoring.error(err)
        }
        loggingIn = false
    }

    companion object {
        val shared = InAppChat()

        suspend fun load() {
            shared.load()
        }

        fun logout() {
            InAppChatStore.current.currentUserID = null
            InAppChatStore.current.user = null
            User.current = null
            async {
                try {
                    API.logout()
                } catch (err: Error) {
                    Monitoring.error(err)
                }
            }
        }

        fun registerFCMToken(token: String) {
            InAppChatStore.current.fcmToken = token
            if (shared.isUserLoggedIn) {
                opbg({
                    API.registerFcmToken(token)
                })
            }
        }
    }
}
