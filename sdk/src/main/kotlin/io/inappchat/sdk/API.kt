/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import io.inappchat.sdk.apis.*
import io.inappchat.sdk.auth.ApiKeyAuth
import io.inappchat.sdk.auth.HttpBearerAuth
import io.inappchat.sdk.infrastructure.ApiClient
import io.inappchat.sdk.state.User
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.properties.Delegates

fun String.sha256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

val provisioningServerProd = "https://prov.ripbullertc.com/v1/tenants/get-tenant-details/"
val provisioningServerDev = "https://prov-dev.inappchat.io/v1/tenants/get-tenant-details/"
val provisioningServerQA = "https://prov-qa.ripbullertc.com/v1/tenants/get-tenant-details/"

val env = "qa"

fun provisioningServer(): String =
    when (env) {
        "qa" -> provisioningServerQA
        "dev" -> provisioningServerDev
        else -> provisioningServerProd
    }


internal suspend inline fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation ->
        val callback = ContinuationCallback(this, continuation)
        enqueue(callback)
        continuation.invokeOnCancellation(callback)
    }
}

class ContinuationCallback(
    private val call: Call,
    private val continuation: CancellableContinuation<Response>
) : Callback, CompletionHandler {

    override fun onResponse(call: Call, response: Response) {
        continuation.resume(response)
    }

    override fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled()) {
            continuation.resumeWithException(e)
        }
    }

    override fun invoke(cause: Throwable?) {
        try {
            call.cancel()
        } catch (_: Throwable) {
        }
    }
}

object API {
    lateinit var deviceId: String
    var cfg: JSONObject? = null
    var server: String by Delegates.observable("https://chat.inappchat.io/") { property, old, new ->
        client = makeClient()
    }

    var authToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.prefs.edit().putString("auth-token", newValue).commit()
        client = makeClient()
    }
    var refreshToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.prefs.edit().putString("refresh-token", newValue).commit()
    }
    var tokenExpiresAt: Date? by Delegates.observable(null) { property, oldValue, newValue ->
        if (newValue != null) {
            InAppChat.prefs.edit().putLong("token-expires", newValue.time).commit()
        } else {
            InAppChat.prefs.edit().remove("token-expires").commit()
        }
    }

    fun okHttpBuilder() = OkHttpClient()
        .newBuilder()
        .addInterceptor(CurlInterceptor(object : com.moczul.ok2curl.logger.Logger {
            override fun log(message: String) {
                Log.d("API", message)
            }
        }))
        .addInterceptor(HttpLoggingInterceptor { message -> Log.d("API", message) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }
        )

    fun makeClient(): ApiClient {
        val client = ApiClient(
            baseUrl = server,
            okHttpClientBuilder = okHttpBuilder(),
            authNames = if (authToken != null) arrayOf(
                "ApiKeyAuth",
                "BearerAuth"
            ) else arrayOf("ApiKeyAuth")
        )
        client.addAuthorization("ApiKeyAuth", ApiKeyAuth("header", "X-API-Key", InAppChat.apiKey))
        authToken?.let { client.addAuthorization("BearerAuth", HttpBearerAuth("bearer", it)) }
        chat = client.createService(ChatApi::class.java)
        auth = client.createService(AuthApi::class.java)
        settings = client.createService(ChatSettingApi::class.java)
        group = client.createService(GroupApi::class.java)
        search = client.createService(SearchApi::class.java)
        thread = client.createService(ThreadApi::class.java)
        user = client.createService(UserApi::class.java)
        return client
    }

    var client = makeClient()
    lateinit var chat: ChatApi
    lateinit var auth: AuthApi
    lateinit var settings: ChatSettingApi
    lateinit var group: GroupApi
    lateinit var search: SearchApi
    lateinit var thread: ThreadApi
    lateinit var user: UserApi

    fun init() {
        var deviceId = InAppChat.prefs
            .getString("device-id", null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            InAppChat.prefs.edit().putString("device-id", deviceId).commit()
        }
        this.deviceId = deviceId
        this.authToken = InAppChat.prefs.getString("auth-token", null)
    }

    fun headers(): Headers {
        val ts = (Date().time / 1000.0).toInt().toString()
        val signature = "${InAppChat.apiKey}~${InAppChat.appContext.packageName}~$ts".sha256()
        return Headers.Builder().add(
            "X-API-Key", InAppChat.apiKey
        )
            .add(
                "X-Request-Signature", signature
            )
            .add(
                "X-nonce", (ts)
            )
            .add(
                "DeviceId", deviceId
            )
            .build()
    }

    suspend fun getTenant(): JSONObject {
        val client = okHttpBuilder().build()
        val request = Request.Builder()
            .url(provisioningServer() + InAppChat.namespace)
            .headers(headers()).build()
        val response = client.newCall(request).await()
        val body = response.body?.string()
        if (body == null) {
            throw IOException("Empty body, expected json")
        }
        val cfg = JSONObject(body).getJSONObject("result")
        this.cfg = cfg
        return cfg
    }

    suspend fun fetchMessages(thread: String, pageSize: Int, currentMsgId: String? = null) =
        chat.getMessages(thread, pageSize = pageSize, currentMsgId = currentMsgId).result()
            .map(Message::get)

    suspend fun getUser(id: String): User = user.getUser(id).result().let(User::get)

}

fun <T> retrofit2.Response<T>.result(): T {
    val result = this.body()
    if (result == null) {
        throw IOException("${this.code()} ${this.message()} ${this.headers()}")
    } else {
        return result
    }
}