/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk

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
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.utils.Monitoring
import ai.botstacks.sdk.utils.async
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.op
import ai.botstacks.sdk.utils.opbg
import ai.botstacks.sdk.utils.retryIO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

@Stable
class BotStacksChat private constructor() {

    val TAG = "BotStacksChat"

    lateinit var appContext: Context
    lateinit var prefs: SharedPreferences
    lateinit var apiKey: String
    lateinit var imageLoader: ImageLoader
    lateinit var packageName: String

    var onLogout: (() -> Unit)? = null

    fun setup(appContext: Context, apiKey: String, delayLoad: Boolean = false) {
        this.appContext = appContext
        this.prefs = appContext.getSharedPreferences("botstackschat", Context.MODE_PRIVATE)
        this.apiKey = apiKey
        this.packageName = appContext.packageName
        BotStacksChatStore.current.init()
        BotStacksChatStore.current.contacts.requestContacts =
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        Log.v(TAG, "BotStacksChat Setup")
        if (!delayLoad) {
            Log.v(TAG, "Launch load")
            scope.launch {
                Log.v(TAG, "Launching load in bg")
                op({
                    bg {
                        load()
                    }
                }) {

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
        Log.v(TAG, "Start load")
        if (!this::apiKey.isInitialized) {
            throw Error("You must initialize BotStacksChat with BotStacksChat.init before calling load")
        }
        if (didStartLoading) throw Error("SDK Already initialized")
        didStartLoading = true
        retryIO {
            BotStacksChatStore.current.loadAsync()
            isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
            loaded = true
        }
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        if (loggingIn) return
        loggingIn = true
        bg {
            try {
                API.login(email = email, password = password)
                isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
            } catch (ex: Throwable) {
                Monitoring.error(ex)
            }
        }
        loggingIn = false
    }

    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        profilePicture: String? = null
    ) {
        if (loggingIn) return
        loggingIn = true
        bg {
            try {
                API.register(
                    email = email,
                    password = password,
                    displayName = displayName,
                    picture = profilePicture
                )
                isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
            } catch (ex: Throwable) {
                Monitoring.error(ex)
            }
        }
        loggingIn = false
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
            isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
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
            isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
        } catch (err: Error) {
            Monitoring.error(err)
        }
        loggingIn = false
    }

    companion object {
        val shared = BotStacksChat()

        suspend fun load() {
            shared.load()
        }

        fun logout() {
            shared.onLogout?.invoke()
            BotStacksChatStore.current.currentUserID = null
            BotStacksChatStore.current.user = null
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
            BotStacksChatStore.current.fcmToken = token
            if (shared.isUserLoggedIn) {
                opbg({
                    API.registerFcmToken(token)
                })
            }
        }
    }
}
