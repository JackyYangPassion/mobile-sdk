/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.ContentResolver
import android.net.Uri
import android.support.v4.os.IResultReceiver.Default
import android.util.Log
import androidx.core.net.toUri
import com.moczul.ok2curl.CurlInterceptor
import io.inappchat.sdk.apis.*
import io.inappchat.sdk.auth.ApiKeyAuth
import io.inappchat.sdk.auth.HttpBearerAuth
import io.inappchat.sdk.infrastructure.ApiClient
import io.inappchat.sdk.models.*
import io.inappchat.sdk.state.*
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import okio.IOException
import okio.source
import org.json.JSONObject
import java.io.File
import java.math.BigDecimal
import java.security.MessageDigest
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
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

fun APIMessage.m() = Message.get(this)
fun APIGroup.g() = Group.get(this)
fun APIThread.t() = io.inappchat.sdk.state.Thread.get(this)
fun APIUser.u() = User.get(this)

object API {
    lateinit var deviceId: String
    var cfg: JSONObject? = null
    var server: String by Delegates.observable("https://chat.inappchat.io/") { property, old, new ->
        client = makeClient()
    }

    var apiKey: String? = null

    var authToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.prefs.edit().putString("auth-token", newValue).apply()
        client = makeClient()
    }
    var refreshToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.prefs.edit().putString("refresh-token", newValue).apply()
    }
    var tokenExpiresAt: LocalDateTime? by Delegates.observable(null) { property, oldValue, newValue ->
        if (newValue != null) {
            InAppChat.prefs.edit().putLong("token-expires", newValue.toEpochSecond(ZoneOffset.UTC))
                .apply()
        } else {
            InAppChat.prefs.edit().remove("token-expires").apply()
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
            okHttpClientBuilder = okHttpBuilder()
        )
        apiKey?.let { client.addAuthorization("ApiKeyAuth", ApiKeyAuth("header", "X-API-Key", it)) }
        authToken?.let { client.addAuthorization("BearerAuth", HttpBearerAuth("bearer", it)) }
        if (this::deviceId.isInitialized) {
            client.addAuthorization("DeviceId", ApiKeyAuth("header", "X-Device-ID", deviceId))
        }
        chat = client.createService(ChatApi::class.java)
        auth = client.createService(AuthApi::class.java)
        settings = client.createService(ChatSettingApi::class.java)
        group = client.createService(GroupApi::class.java)
        search = client.createService(SearchApi::class.java)
        thread = client.createService(ThreadApi::class.java)
        user = client.createService(UserApi::class.java)
        _default = client.createService(DefaultApi::class.java)
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
    lateinit var _default: DefaultApi

    fun init() {
        var deviceId = InAppChat.prefs
            .getString("device-id", null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            InAppChat.prefs.edit().putString("device-id", deviceId).apply()
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

    suspend fun getMessages(thread: String, pageSize: Int, currentMsgId: String? = null) =
        chat.getMessages(thread, pageSize = pageSize, currentMsgId = currentMsgId).result()
            .map(Message::get)


    fun reply(to: String?): Reply? =
        to?.let { Reply(baseMsgUniqueId = it, replyMsgConfig = BigDecimal.ONE) }

    suspend fun updateMessageStatus(mid: String, status: MessageStatus) {
        chat.updateMessage(mid, UpdateMessageInput(status = status)).result()
    }

    suspend fun send(
        thread: String,
        inReplyTo: String?,
        text: String? = null,
        attachment: Attachment? = null,
        gif: String? = null,
        location: Location? = null,
        contact: Contact? = null,
        mediaType: MediaType = "application/octet-stream".toMediaType()
    ) =
        chat.sendMessage(
            senderTimeStampMs = System.currentTimeMillis().toBigDecimal(),
            thread,
            message = text,
            gif = gif,
            location = location,
            msgType = attachment?.let { MessageType.valueOf(it.kind.value) }
                ?: gif?.let { MessageType.gif },
            contact = contact,
            file = attachment?.let {
                MultipartBody.Part.create(
                    InputStreamRequestBody(
                        mediaType,
                        InAppChat.appContext.contentResolver,
                        attachment.url.toUri()
                    )
                )
            },
            replyThreadFeatureData = reply(inReplyTo)
        ).result().message!!.m()

    suspend fun updateGroup(
        id: String,
        name: String? = null,
        groupType: UpdateGroupInput.GroupType? = null,
        description: String? = null,
        profilePic: File? = null
    ) = group.updateGroup(
        id,
        updateGroupInput = UpdateGroupInput(
            name,
            groupType = groupType,
            description = description,
            profilePic = profilePic
        )
    ).result().g()

    suspend fun createGroup(
        name: String,
        _private: Boolean,
        description: String? = null,
        profilePic: File? = null,
        profilePicType: MediaType = "application/octet-stream".toMediaType(),
        participants: List<String> = listOf()
    ) = group.createGroup(
        name = name,
        groupType = if (_private) "private" else "public",
        description = description,
        profilePic = profilePic?.let {
            MultipartBody.Part.create(
                InputStreamRequestBody(
                    profilePicType,
                    InAppChat.appContext.contentResolver,
                    it.toUri()
                )
            )
        },
        participants = participants
    ).result().g()

    suspend fun deleteGroup(id: String) = group.deleteGroup(id).result()
    suspend fun addParticipant(g: String, u: String) = group.addParticipant(g, u).result()
    suspend fun removeParticipant(g: String, u: String) = group.addParticipant(g, u).result()
    suspend fun joinGroup(g: String) = group.addParticipant(g, User.current!!.id).result()
    suspend fun leaveGroup(g: String) = group.removeParticipant(g, User.current!!.id).result()
    suspend fun getGroup(g: String) = group.getGroup(g).result().g()
    suspend fun promoteAdmin(g: String, uid: String) = group.groupAddAdmin(g, uid).result()
    suspend fun dismissAdmin(g: String, uid: String) = group.groupDismissAdmin(g, uid).result()

    suspend fun deleteMessage(id: String) = chat.deleteMessage(id).result()

    suspend fun favorite(message: String, remove: Boolean) =
        chat.updateMessage(message, UpdateMessageInput(isStarred = !remove)).result()

    suspend fun favorites(skip: Int, limit: Int) =
        chat.getFavorites(skip, limit).result().map { it.m() }

    suspend fun invites() = group.getInvites().result()
    suspend fun dismissInvites(g: String) = group.dismissGroupInvite(g).result()
    suspend fun acceptInvites(g: String) = group.acceptGroupInvite(g).result()

    suspend fun invite(users: List<String>, toGroup: String) =
        group.inviteUser(toGroup, users).result()

    suspend fun getSharedMedia(uid: String) =
        _default.getUserMessages(uid, 0, 10, MessageType.image).result().map { it.m() }

    fun onLogin(auth: Auth) {
        onToken(
            auth.token.accessToken,
            auth.token.refreshToken,
            LocalDateTime.now().plusSeconds(auth.token.expiresIn.toLong())
        )
        onUser(auth.user.u())
    }

    fun onUser(user: User) {
        Chats.current.user = user
        Chats.current.currentUserID = user.id
        Socket.connect()
    }

    fun onToken(access: String, refresh: String, expires: LocalDateTime) {
        authToken = access
        refreshToken = refresh
        tokenExpiresAt = expires
    }

    
    fun getUser(id: String): User = user.getUser(id).result().let(User::get)

}

fun <T> retrofit2.Response<T>.result(): T {
    val result = this.body()
    if (result == null) {
        throw IOException("${this.code()} ${this.message()} ${this.headers()}")
    } else {
        return result
    }
}

class InputStreamRequestBody(
    private val contentType: MediaType,
    private val contentResolver: ContentResolver,
    private val uri: Uri
) : RequestBody() {
    override fun contentType() = contentType

    override fun contentLength(): Long = -1

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri)

        input?.use { sink.writeAll(it.source()) }
            ?: throw IOException("Could not open $uri")
    }
}