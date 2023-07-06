/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import io.inappchat.sdk.auth.ApiKeyAuth
import io.inappchat.sdk.auth.HttpBearerAuth
import io.inappchat.sdk.state.*
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.Socket
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import okio.IOException
import okio.source
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.math.BigDecimal
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneId
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

val env = "prod"

object Servers {
    data class Server(val host: String, val ssl: Boolean) {
        val http: String
            get() = "http${if (ssl) "s" else ""}://${host}/graphql"

        val ws: String
            get() = "ws${if (ssl) "s" else ""}://${host}/graphql"
    }

    val prod = Server("chat.inappchat.io", true)
    val dev = Server("chat.dev.inappchat.io", true)
    val local = Server("chat.dev.inappchat.io", true)

    fun get() = when (env) {
        "dev" -> dev
        "local" -> local
        else -> prod
    }
}


suspend inline fun Call.await(): Response {
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
    var apiKey: String? = null

    var authToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.shared.prefs.edit().putString("auth-token", newValue).apply()
    }


    var client = ApolloClient.Builder()
        .serverUrl(Servers.get().http)
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .apply {
                            authToken?.let { addHeader("Authorization", "Bearer $it") }
                            apiKey?.let { addHeader("X-API-Key", it) }
                            addHeader("X-Device-ID", deviceId)
                        }.build()
                    chain.proceed(request)
                })
                .addInterceptor(CurlInterceptor(logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("InAppChat", message)
                    }
                }))
                .addInterceptor(LoggingInterceptor)
                .build()
        )
        .build()


    fun init() {
        var deviceId = InAppChat.shared.prefs
            .getString("device-id", null)
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString()
            InAppChat.shared.prefs.edit().putString("device-id", deviceId).apply()
        }
        this.deviceId = deviceId
        this.authToken = InAppChat.shared.prefs.getString("auth-token", null)
    }


    suspend fun getMessages(chat: String, skip: Int = 0, limit: Int = 40) =
        client.query(
            ListMessagesQuery(
                chat = chat,
                offset = Optional.present(skip),
                count = Optional.present(limit)
            )
        ).execute().data?.let { it.messages.map(Message::get) }

    suspend fun updateMessageStatus(mid: String, status: MessageStatus) {
        chat.updateMessage(mid, UpdateMessageInput(status = status)).result()
    }

    suspend fun send(
        chat: String,
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
            chat,
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
                        InAppChat.shared.appContext.contentResolver,
                        attachment.url.toUri()
                    )
                )
            },
            replyChatFeatureData = reply(inReplyTo)
        ).result().message!!.m()

    suspend fun updateChat(
        id: String,
        name: String? = null,
        chatType: UpdateChatInput.ChatType? = null,
        description: String? = null,
        profilePic: File? = null
    ) = chat.updateChat(
        id,
        updateChatInput = UpdateChatInput(
            name,
            chatType = chatType,
            description = description,
            profilePic = profilePic
        )
    ).result().g()

    suspend fun createChat(
        name: String,
        _private: Boolean,
        description: String? = null,
        profilePic: File? = null,
        profilePicType: MediaType = "application/octet-stream".toMediaType(),
        participants: List<String> = listOf()
    ) = chat.createChat(
        name = name,
        chatType = if (_private) "private" else "public",
        description = description,
        profilePic = profilePic?.let {
            MultipartBody.Part.create(
                InputStreamRequestBody(
                    profilePicType,
                    InAppChat.shared.appContext.contentResolver,
                    it.toUri()
                )
            )
        },
        participants = participants
    ).result().g()

    suspend fun deleteChat(id: String) = chat.deleteChat(id).result()
    suspend fun addParticipant(g: String, u: String) = chat.addParticipant(g, u).result()
    suspend fun removeParticipant(g: String, u: String) = chat.addParticipant(g, u).result()
    suspend fun joinChat(g: String) = chat.addParticipant(g, User.current!!.id).result()
    suspend fun leaveChat(g: String) = chat.removeParticipant(g, User.current!!.id).result()
    suspend fun getChat(g: String) = chat.getChat(g).result().g()
    suspend fun promoteAdmin(g: String, uid: String) = chat.chatAddAdmin(g, uid).result()
    suspend fun dismissAdmin(g: String, uid: String) = chat.chatDismissAdmin(g, uid).result()

    suspend fun deleteMessage(id: String) = chat.deleteMessage(id).result()

    suspend fun favorite(message: String, remove: Boolean) =
        chat.updateMessage(message, UpdateMessageInput(isStarred = !remove)).result()

    suspend fun favorites(skip: Int, limit: Int) =
        chat.getFavorites(skip, limit).result().map { it.m() }

    suspend fun react(mid: String, emoji: String) = chat.react(mid, emoji).result()
    suspend fun unreact(mid: String, emoji: String) = chat.unreact(mid, emoji).result()
    suspend fun invites() = chat.getInvites().result()

    suspend fun editMessageText(id: String, text: String) =
        chat.updateMessage(id, UpdateMessageInput(message = text)).result()

    suspend fun dismissInvites(g: String) = chat.dismissChatInvite(g).result()
    suspend fun acceptInvites(g: String) = chat.acceptChatInvite(g).result()

    suspend fun invite(users: List<String>, toChat: String) =
        chat.inviteUser(toChat, users).result()

    suspend fun getSharedMedia(uid: String) =
        _default.getUserMessages(uid, 0, 10, MessageType.image).result().map { it.m() }

    suspend fun onLogin(auth: Auth) {
        onToken(
            auth.token.accessToken,
            auth.token.refreshToken,
            LocalDateTime.now().plusSeconds(auth.token.expiresIn.toLong())
        )
        onUser(auth.user.u())
    }

    suspend fun onUser(user: User) {
        Chats.current.user = user
        Chats.current.currentUserID = user.id
        User.current = user
        try {
            Chats.current.loadAsync()
            Socket.connect()
        } catch (err: Error) {
            Monitoring.error(err)
        }
    }

    fun onToken(access: String, refresh: String?, expires: LocalDateTime) {
        authToken = access
        refreshToken = refresh
        tokenExpiresAt = expires
    }

    suspend fun login(
        accessToken: String,
        userId: String,
        email: String,
        name: String?,
        nickname: String?,
        picture: String?
    ): User {
        val res = auth.login(
            LoginInput(
                userId = userId,
                accessToken = accessToken,
                email = email,
                deviceId = deviceId,
                picture = picture,
                name = name,
                nickname = nickname,
                deviceType = LoginInput.DeviceType.android
            )
        ).result()
        val user = res.user.u()
        onLogin(res)
        return user
    }

    suspend fun nftLogin(
        contract: String,
        address: String,
        tokenID: String,
        signature: String,
        profilePicture: String,
        username: String?
    ): User {
        val res = auth.nftLogin(
            NFTLoginInput(
                address = address, contract = contract, signature = signature, tokenID = tokenID,
                username = username, profilePicture = profilePicture
            )
        ).result()
        onLogin(res)
        return res.user.u()
    }

    suspend fun getUser(id: String): User = user.getUser(id).result().let(User::get)

    suspend fun logout() {
        auth.logout()
        refreshToken = null
        authToken = null
        tokenExpiresAt = null
    }

    suspend fun block(id: String, isBlock: Boolean) {
        if (isBlock) {
            user.blockUser(id)
        } else {
            user.unblockUser(id)
        }
    }

    suspend fun updateNotifications(setting: NotificationSettings.AllowFrom) =
        user.updateMe(
            UpdateUserInput(notificationSettings = NotificationSettings(setting))
        )

    suspend fun updateAvailability(setting: AvailabilityStatus) =
        user.updateMe(
            UpdateUserInput(availabilityStatus = (setting))
        )

    suspend fun updateChatNotifications(id: String, setting: NotificationSettings.AllowFrom) =
        chat.updateChat(
            id,
            UpdateChatInput(notificationSettings = NotificationSettings(setting))
        )

    suspend fun getContacts(existing: List<String>): List<User> =
        user.syncContacts(SyncContactsInput(existing)).result().map { it.u() }

    suspend fun getJoinedUserChats(skip: Int, limit: Int) = chat.getChats(
        skip = skip,
        limit = limit,
        chatType = ChatApi.ChatType_getChats.single
    )
        .result().map { it.t() }

    suspend fun getJoinedChatChats(skip: Int, limit: Int) = chat.getChats(
        skip = skip,
        limit = limit,
        chatType = ChatApi.ChatType_getChats.chat
    )
        .result().map { it.t() }

    suspend fun chats(skip: Int, limit: Int) =
        chat.getChats(limit = limit, skip = skip, joined = ChatApi.Joined_getChats.no).result()
            .map { it.g() }

    suspend fun getReplyChats(skip: Int, limit: Int) =
        chat.getReplyChats(limit = limit, skip = skip, deep = true).result().map { it.m() }

    suspend fun getChat(id: String) = chat.getChat(id).result().t()
    suspend fun getMessage(id: String) = chat.getMessage(id).result().m()
    suspend fun getChatChat(gid: String) = chat.getChatChat(gid).result().t()
    suspend fun getUserChat(uid: String) = chat.createChat(uid).result().t()

    suspend fun getReplies(mid: String, skip: Int, limit: Int) =
        chat.getReplies(mid, skip, limit).result().map { it.m() }

    suspend fun registerFcmToken(token: String) = user.updateMe(UpdateUserInput(fcmToken = token))

    suspend fun me(): User = user.me().result().u()

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