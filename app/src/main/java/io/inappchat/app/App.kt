/*
 * Copyright (c) 2023.
 */

package io.inappchat.app

import android.app.Application
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.giphy.sdk.ui.Giphy
import io.inappchat.sdk.InAppChat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class App : Application() {

    private lateinit var account: Auth0
    override fun onCreate() {
        super.onCreate()
        app = this
        account = Auth0(
            getString(R.string.auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        Giphy.configure(this, getString(R.string.giphy))
        InAppChat.shared.setup(
            this,
            getString(R.string.inappchat_namespace),
            getString(R.string.inappchat_api_key)
        )
    }

    suspend fun login() {
        val credentials = auth0()
        val uid = credentials.user.getId()
        if (uid == null) {
            throw Error("Expected a user ID")
        }
    }

    companion object {
        lateinit var app: App
    }

    private suspend fun auth0() = suspendCoroutine<Credentials> { continuation ->
        // Setup the WebAuthProvider, using the custom scheme and scope.
        WebAuthProvider.login(account)
            .withScheme("demo")
            .withScope("openid profile email")
            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                // Called when there is an authentication failure
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                    continuation.resumeWithException(exception)
                }

                // Called when authentication completed successfully
                override fun onSuccess(credentials: Credentials) {
                    // Get the access token from the credentials object.
                    // This can be used to call APIs
                    continuation.resume(credentials)
                }
            })
    }

}