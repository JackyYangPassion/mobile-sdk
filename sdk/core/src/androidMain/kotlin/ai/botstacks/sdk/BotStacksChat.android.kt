package ai.botstacks.sdk

import ai.botstacks.sdk.internal.state.BotStacksChatStore
import ai.botstacks.sdk.internal.utils.bg
import ai.botstacks.sdk.internal.utils.op
import ai.botstacks.sdk.internal.utils.retryIO
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.compose.runtime.Stable
import androidx.core.content.ContextCompat
import com.giphy.sdk.ui.Giphy
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Stable
actual class BotStacksChatPlatform : BotStacksChat() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var _apiKey: String
    private lateinit var packageName: String

    actual val apiKey: String
        get() = _apiKey

    actual val appIdentifier: String
        get() = packageName

    override val prefs: Settings
        get() = SharedPreferencesSettings(sharedPreferences)

    actual val scope = CoroutineScope(Dispatchers.Main)

    fun setup(
        context: Context,
        apiKey: String,
        giphyApiKey: String? = null,
        delayLoad: Boolean = false
    ) {
        this.sharedPreferences = context.getSharedPreferences("botstackschat", Context.MODE_PRIVATE)
        this._apiKey = apiKey
        this.packageName = context.packageName

        BotStacksChatStore.current.init()
        BotStacksChatStore.current.contacts.requestContacts =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED

        if (!giphyApiKey.isNullOrEmpty()) {
            Giphy.configure(context, giphyApiKey)
            hasGiphySupport = true
        }

        // retrieve google maps key from merged manifest
        // if present, enable map rendering support
        val googleMapsApiKey = context.packageManager.getApplicationInfo(
            appIdentifier,PackageManager.GET_META_DATA)
            .metaData.getString("com.google.android.geo.API_KEY")

        if (!googleMapsApiKey.isNullOrEmpty()) {
            hasMapsSupport = true // runtime permission grants location access
        }

        hasLocationSupport = true // runtime permission grants camera access
        hasCameraSupport = true // runtime permission grants camera access

        if (!delayLoad) {
            scope.launch {
                op({ bg { load() } }) {}
            }
        }
    }


    private var didStartLoading = false

    actual suspend fun load() {
        println("Start load")
        if (apiKey.isEmpty()) {
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
}