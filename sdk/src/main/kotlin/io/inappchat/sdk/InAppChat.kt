/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.Context
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.auth0.android.result.Credentials
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.Socket
import io.inappchat.sdk.utils.async
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.opbg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import kotlin.math.sign
import kotlin.properties.Delegates

@Stable
class InAppChat private constructor() {

    val TAG = "InAppChat"

    lateinit var appContext: Context
    lateinit var prefs: SharedPreferences
    lateinit var namespace: String
    lateinit var apiKey: String

    fun setup(appContext: Context, namespace: String, apiKey: String, delayLoad: Boolean = false) {
        this.appContext = appContext
        this.prefs = appContext.getSharedPreferences("inappchat", Context.MODE_PRIVATE)
        this.namespace = namespace
        this.apiKey = apiKey
        Chats.current.init()
        ImageLoader.Builder(appContext)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
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
    suspend fun load(): JSONObject {
        Log.v("InAppChat", "Start load")
        if (!this::apiKey.isInitialized) {
            throw Error("You must initialize InAppChat with InAppChat.init before calling load")
        }
        if (didStartLoading) throw Error("SDK Already initialized")
        didStartLoading = true
        val tenant = API.getTenant()
        println("Tenant ${tenant.toString(2)}")
        Chats.current.loadAsync()
        loaded = true
        val cfg = tenant.getJSONObject("config")
        this.config = cfg
        API.cfg = cfg
        val chatServer = cfg.getJSONObject("serverDetails").getJSONObject("chatServer")
        API.apiKey = chatServer.getString("apiKey")
        API.server = chatServer.getString("url") + "v3"
        val mqttServer = cfg.getJSONObject("serverDetails").getJSONObject("mqttServer")
        Socket.apiKey = mqttServer.getString("apiKey")
        Socket.server = mqttServer.getString("url")
        API.updateClient()
        Log.v("InAppChat", "Stop loading")
        return cfg
    }

    var loggingIn by mutableStateOf(false)
    suspend fun login(
        accessToken: String,
        userId: String,
        email: String,
        name: String?,
        nickname: String?,
        picture: String?
    ) {
        if (loggingIn) return
        loggingIn = true
        try {
            API.login(
                accessToken,
                userId,
                email,
                name,
                nickname,
                picture
            )
        } catch (err: Error) {
            Monitoring.error(err)
        }
    }

    suspend fun nftLogin(
        contract: String,
        address: String,
        tokenID: String,
        signature: String,
        profilePicture: String,
        username: String?
    ) {
        if (loggingIn) {
            return
        }
        loggingIn = true
        try {
            API.nftLogin(contract, address, tokenID, signature, profilePicture, username)
        } catch (err: Error) {
            Monitoring.error(err)
        }
    }

    companion object {
        val shared = InAppChat()

        suspend fun load() {
            shared.load()
        }

        fun logout() {
            Chats.current.currentUserID = null
            Chats.current.user = null
            User.current = null
            async {
                try {
                    API.logout()
                    Socket.disconnect()
                } catch (err: Error) {
                    Monitoring.error(err)
                }
            }
        }

        fun registerFCMToken(token: String) {
            Chats.current.fcmToken = token
            if (shared.isUserLoggedIn) {
                opbg({
                    API.registerFcmToken(token)
                })
            }
        }
    }
}
