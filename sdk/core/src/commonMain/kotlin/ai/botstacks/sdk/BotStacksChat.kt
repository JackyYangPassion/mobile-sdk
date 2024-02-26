/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk

import ai.botstacks.sdk.internal.API
import ai.botstacks.sdk.internal.Monitoring
import ai.botstacks.sdk.internal.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.internal.utils.async
import ai.botstacks.sdk.internal.utils.opbg
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BotStacksChat {
    var loaded by mutableStateOf(false)

    var loggingIn by mutableStateOf(false)
    var isUserLoggedIn by mutableStateOf(false)

    internal abstract val prefs: Settings

    /**
     * Register a callback for handling log out events
     */
    var onLogout: (() -> Unit)? = null

    internal var hasGiphySupport by mutableStateOf(false)
    internal var hasMapsSupport by mutableStateOf(false)
    internal var hasLocationSupport by mutableStateOf(false)
    internal var hasCameraSupport by mutableStateOf(false)


    /**
     * login to BotStacks Backend
     *
     * @param accessToken BotStacks API key
     * @param userId userId of user to associate session with
     * @param username username for user
     * @param displayName optional display name for user
     * @param picture optional user image (avatar) URL
     */
    suspend fun login(
        accessToken: String? = null,
        userId: String,
        username: String,
        displayName: String?,
        picture: String?
    ) {
        if (loggingIn) return
        loggingIn = true
        runCatching {
            API.login(
                accessToken = accessToken,
                userId = userId,
                username = username,
                displayName = displayName,
                picture = picture
            )
        }.onSuccess {
            isUserLoggedIn = BotStacksChatStore.current.currentUserID != null
            loggingIn = false
        }.onFailure { err ->
            Monitoring.error(err)
            loggingIn = false
        }
    }

    companion object {
        val shared = BotStacksChatPlatform()

        /**
         * logout from BotStacks
         */
        fun logout() {
            async {
                try {
                    API.logout()
                } catch (err: Error) {
                    Monitoring.error(err)
                }
            }
        }

        /**
         * Register an Firebase Cloud Messaging (FCM) token with our Backend
         */
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

expect class BotStacksChatPlatform(): BotStacksChat {
    val apiKey: String
    val appIdentifier: String
    val scope: CoroutineScope

    suspend fun load()
}
