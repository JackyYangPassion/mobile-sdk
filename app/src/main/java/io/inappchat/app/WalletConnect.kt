package io.inappchat.app

import android.util.Log
import androidx.compose.runtime.Stable
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.auth.client.Auth
import com.walletconnect.auth.client.AuthClient
import com.walletconnect.auth.client.AuthInterface
import com.walletconnect.sign.client.Sign
import com.walletconnect.sign.client.SignClient
import com.walletconnect.util.bytesToHex
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

@Stable
class WalletConnect(private val app: App): AuthInterface.RequesterDelegate, SignClient.DappDelegate {
    private val tag = "WalletConnect"

    init {
        val projectId = "" // Get Project ID at https://cloud.walletconnect.com/
        val relayUrl = "relay.walletconnect.com"
        val serverUrl = "wss://$relayUrl?projectId=${projectId}"
        val appMetaData = Core.Model.AppMetaData(name = "g2minus",
            description = "Poison Pog InAppChat dApp",
            url = "poisonpog.com",
            icons = listOf("https://www.poisonpog.com/static/logo-cd370e1283f460c588292e57c1d0ac44.gif"),
            redirect = "g2minus:/request",
        )

        CoreClient.initialize(relayServerUrl = serverUrl, connectionType = ConnectionType.AUTOMATIC, application = app, metaData = appMetaData, onError = {error -> Log.e(tag, error.throwable.stackTraceToString())})

        AuthClient.initialize(params = Auth.Params.Init(core = CoreClient), onSuccess = {
            Log.v(tag, "AuthClient on Success")
        }) { error -> Log.e(tag, error.throwable.stackTraceToString()) }
        AuthClient.setRequesterDelegate(this)

        val init = Sign.Params.Init(core = CoreClient)

        SignClient.initialize(init) { error ->
            // Error will be thrown if there's an issue during initialization
            Log.e(tag, error.throwable.stackTraceToString())
        }
    }

    override fun onAuthResponse(authResponse: Auth.Event.AuthResponse) {
        Log.v(tag, "Auth Client Response $authResponse")
    }

    override fun onConnectionStateChange(connectionStateChange: Auth.Event.ConnectionStateChange) {
        Log.v(tag, "Auth Client Connection Change $connectionStateChange")
    }

    override fun onError(error: Auth.Event.Error) {
        Log.e(tag, "Auth Client Error " + error.error.throwable.stackTraceToString())
    }

    fun randomNonce(): String = Random.nextBytes(16).bytesToHex()

    suspend fun login(): String {
        suspendCoroutine<Unit> { continuation ->
            val requestParams = Auth.Params.Request(
                topic = "g2minus-android", // a pairing topic is used to send a authentication request, pass it from [Pairing API](../../kotlin/core/pairing.md)
                chainId = "1", // is the EIP-155 Chain ID to which the session is bound, and the network where Contract Accounts MUST be resolved.
                domain = "g2minus.inappchat.io", // is the RFC 3986 authority that is requesting the signing.
                nonce = randomNonce(), // is a randomized token typically chosen by the relying party and used to prevent replay attacks, at least 8 alphanumeric characters.
                aud = "https://g2minus.inappchat.io/login", //  is an RFC 3986 URI referring to the resource that is the subject of the signing (as in the subject of a claim).
                type = null, // (Not yet implemented) Type of signing. Currently ignored and always set to `eip4361`.
                nbf = null, // (optional) is the ISO 8601 datetime string that, if present, indicates when the signed authentication message will become valid.
                exp = null, // (optional) is the ISO 8601 datetime string that, if present, indicates when the signed authentication message is no longer valid.
                statement = "Sign in to g2minus with your wallet.", // (optional) is a human-readable ASCII assertion that the user will sign, and it must not contain '\n' (the byte 0x0a).
                requestId = null, // (optional) is an system-specific identifier that may be used to uniquely refer to the sign-in request.
                resources = null, // (optional) is a list of information or references to information the user wishes to have resolved as part of authentication by the relying party. They are expressed
                // as RFC 3986 URIs.
            )



            AuthClient.request(requestParams,
                onSuccess = {
                    // Callback triggered when the authentication request has been sent successfully. Expose Pairing URL using [Pairing API](../../kotlin/core/pairing.md), to a wallet to establish a secure connection
                    continuation.resume(Unit)
                },
                onError = { error ->
                    Log.e("Requester request", error.throwable.stackTraceToString())
                    continuation.resumeWithException(error.throwable)
                }
            )

        }
        return suspendCoroutine<String> { continuation ->
            var didCreate = false
            val pairing: Core.Model.Pairing = CoreClient.Pairing.getPairings().firstOrNull() ?: run {
                didCreate = true
                CoreClient.Pairing.create(onError = {
                    throw it.throwable
                })!!
            }
            if (didCreate) {
                SignClient.connect(Sign.Params.Connect(pairing = pairing), onSuccess = {
                    continuation.resume(pairing.uri)
                }, onError = {
                    throw it.throwable
                })
            } else {
                continuation.resume(pairing.uri)
            }
        }
    }

    suspend fun connect(): String {
        return suspendCoroutine { continuation ->
            var didCreate = false
            val pairing: Core.Model.Pairing = CoreClient.Pairing.getPairings().firstOrNull() ?: run {
                didCreate = true
                CoreClient.Pairing.create(onError = {
                    throw it.throwable
                })!!
            }
            if (didCreate) {
                SignClient.connect(Sign.Params.Connect(pairing = pairing), onSuccess = {
                    continuation.resume(pairing.uri)
                }, onError = {
                    throw it.throwable
                })
            } else {
                continuation.resume(pairing.uri)
            }
        }
    }

    override fun onConnectionStateChange(state: Sign.Model.ConnectionState) {
        Log.v(tag, "Sign Client Connection State $state")
    }

    override fun onError(error: Sign.Model.Error) {
        Log.e(tag, "Sign Client on Error " + error.throwable.stackTraceToString())
    }

    override fun onSessionApproved(approvedSession: Sign.Model.ApprovedSession) {
        Log.v(tag, "Sign Client session approved $approvedSession")
    }

    override fun onSessionDelete(deletedSession: Sign.Model.DeletedSession) {
        Log.v(tag, "Session deleted")
    }

    override fun onSessionEvent(sessionEvent: Sign.Model.SessionEvent) {
        Log.v(tag, "Session event $sessionEvent " + sessionEvent.name + " " + sessionEvent.data)
    }

    override fun onSessionExtend(session: Sign.Model.Session) {
        Log.v(tag, "Session extend $session "  + session.topic + session.pairingTopic)
    }

    override fun onSessionRejected(rejectedSession: Sign.Model.RejectedSession) {
        Log.v(tag, "Session Rejected")
    }

    override fun onSessionRequestResponse(response: Sign.Model.SessionRequestResponse) {
        Log.v( tag, "session request response ${response.result}")
    }

    override fun onSessionUpdate(updatedSession: Sign.Model.UpdatedSession) {
        Log.v(tag, "session updated")
    }
}