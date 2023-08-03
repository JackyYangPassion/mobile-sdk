/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import com.apollographql.apollo3.network.ws.GraphQLWsProtocol
import com.apollographql.apollo3.network.ws.WebSocketNetworkTransport
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import io.inappchat.sdk.fragment.FUser
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Member
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.state.onCoreEvent
import io.inappchat.sdk.state.onMeEvent
import io.inappchat.sdk.type.AttachmentInput
import io.inappchat.sdk.type.CreateGroupInput
import io.inappchat.sdk.type.DeviceType
import io.inappchat.sdk.type.EthLoginInput
import io.inappchat.sdk.type.LoginInput
import io.inappchat.sdk.type.MemberRole
import io.inappchat.sdk.type.ModMemberInput
import io.inappchat.sdk.type.NotificationSetting
import io.inappchat.sdk.type.OnlineStatus
import io.inappchat.sdk.type.SendMessageInput
import io.inappchat.sdk.type.UpdateGroupInput
import io.inappchat.sdk.type.UpdateMessageInput
import io.inappchat.sdk.type.UpdateProfileInput
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.ift
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.security.MessageDigest
import java.util.UUID
import kotlin.properties.Delegates

fun String.sha256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

object Server {
    val host = BuildConfig.HOST
    val ssl = BuildConfig.SSL
    val http = "http${if (ssl) "s" else ""}://${host}"
    val ws = "ws${if (ssl) "s" else ""}://${host}"
}

object API {
    lateinit var deviceId: String

    var authToken: String? by Delegates.observable(null) { property, oldValue, newValue ->
        InAppChat.shared.prefs.edit().putString("auth-token", newValue).apply()
    }


    var client = ApolloClient.Builder()
        .serverUrl(Server.http + "/graphql")
        .subscriptionNetworkTransport(
            WebSocketNetworkTransport.Builder()
                .protocol(GraphQLWsProtocol.Factory(
                    connectionPayload = {
                        authToken?.let { mapOf("authToken" to it) } ?: mapOf()
                    }
                ))
                .serverUrl(Server.ws + "/graphql")
                .build()
        )
        .httpEngine(
            DefaultHttpEngine(
                OkHttpClient.Builder()
                    .addInterceptor(Interceptor { chain ->
                        val request = chain.request().newBuilder()
                            .apply {
                                authToken?.let { addHeader("Authorization", "Bearer $it") }
                                addHeader("X-API-Key", InAppChat.shared.apiKey)
                                addHeader("X-Device-ID", deviceId)
                            }.build()
                        chain.proceed(request)
                    })
                    .addInterceptor(CurlInterceptor(logger = object : Logger {
                        override fun log(message: String) {
                            Log.v("InAppChat", message)
                        }
                    }))
                    .build()
            )
        )
        .addHttpInterceptor(LoggingInterceptor())
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
        ).execute().dataOrThrow().messages.map { Message.get(it.fMessage) }


    suspend fun send(
        chat: String,
        inReplyTo: String?,
        text: String? = null,
        attachments: List<AttachmentInput>? = null,
    ) =
        client.mutation(
            SendMessageMutation(
                SendMessageInput(
                    chat = chat,
                    parent = Optional.presentIfNotNull(inReplyTo),
                    text = Optional.presentIfNotNull(text),
                    attachments = Optional.presentIfNotNull(attachments),
                )
            )
        ).execute().data?.sendMessage?.fMessage?.let { Message.get(it) }

    suspend fun dm(user: String) =
        client.mutation(DMMutation(user)).execute().dataOrThrow().dm?.let {
            val chat = Chat.get(it.chat.fChat)
            chat.membership?.let { membership ->
                Chats.current.memberships.add(membership)
            }
            chat
        }

    suspend fun updateChat(
        input: UpdateGroupInput
    ) = client.mutation(UpdateGroupMutation(input)).execute().data?.updateGroup

    suspend fun createChat(
        name: String,
        _private: Boolean,
        description: String? = null,
        image: String? = null,
        invites: List<String> = listOf()
    ) = client.mutation(
        CreateGroupMutation(
            CreateGroupInput(
                name = name,
                _private = Optional.presentIfNotNull(_private),
                description = Optional.presentIfNotNull(description),
                image = Optional.presentIfNotNull(image),
                invites = Optional.presentIfNotNull(invites)
            )
        )
    ).execute().dataOrThrow().createGroup?.fChat?.let { Chat.get(it) }

    suspend fun deleteChat(id: String) =
        client.mutation(DeleteGroupMutation(id)).execute().data?.deleteGroup

    suspend fun joinChat(g: String) =
        client.mutation(JoinChatMutation(g)).execute().data?.join?.let { Member.get(it.fMember) }

    suspend fun leaveChat(g: String) = client.mutation(LeaveChatMutation(g)).execute().data?.leave
    suspend fun getChat(g: String) =
        client.query(GetChatQuery(g)).execute().data?.chat?.fChat?.let { Chat.get(it) }

    suspend fun modAdmin(g: String, uid: String, promote: Boolean) = client.mutation(
        ModMemberRoleMutation(
            ModMemberInput(
                chat = g,
                user = uid,
                role = Optional.present(ift(promote, MemberRole.Admin, MemberRole.Member))
            )
        )
    ).execute().data?.modMember

    suspend fun dismissAdmin(g: String, uid: String) = modAdmin(g, uid, true)
    suspend fun promoteAdmin(g: String, uid: String) = modAdmin(g, uid, true)

    suspend fun deleteMessage(id: String) =
        client.mutation(DeleteMessageMutation(id)).execute().data?.removeMessage

    suspend fun favorite(message: String, remove: Boolean) =
        if (remove) client.mutation(UnfavoriteMutation(message)).execute().data?.unfavorite else
            client.mutation(FavoriteMutation(message)).execute().data?.favorite

    suspend fun favorites(skip: Int, limit: Int) =
        client.query(
            ListFavoritesQuery(
                count = Optional.present(limit),
                offset = Optional.present(skip)
            )
        ).execute().dataOrThrow().favorites.map { Message.get(it.fMessage) }

    suspend fun react(mid: String, emoji: String) =
        client.mutation(ReactMutation(mid, Optional.present(emoji))).execute().data?.react

    suspend fun invites() = client.query(GetInvitesQuery()).execute().dataOrThrow().invites

    suspend fun getUsers(skip: Int, limit: Int) = client.query(
        ListUsersQuery(
            count = Optional.present(limit),
            offset = Optional.present(skip)
        )
    )
        .execute().dataOrThrow().users.map { User.get(it.fUser) }

    suspend fun editMessageText(id: String, text: String) =
        client.mutation(
            UpdateMessageMutation(
                UpdateMessageInput(
                    id = id,
                    text = Optional.present(text)
                )
            )
        )

    suspend fun dismissInvites(g: String) =
        client.mutation(DismissInvitesMutation(g)).execute().data?.dismissInvites

    suspend fun acceptInvites(g: String) = joinChat(g)

    suspend fun invite(users: List<String>, toChat: String) =
        client.mutation(InviteUsersMutation(toChat, users))
            .execute().data?.inviteMany?.map { Member.get(it.fMember) }

//    suspend fun getSharedMedia(uid: String) =
//        _default.getUserMessages(uid, 0, 10, MessageType.image).result().map { it.m() }

    suspend fun onLogin(token: String, user: FUser) {
        onToken(
            token
        )
        onUser(User.get(user))
    }

    suspend fun onUser(user: User) {
        Chats.current.user = user
        Chats.current.currentUserID = user.id
        User.current = user
        try {
            Chats.current.loadAsync()
            subscribe()
        } catch (err: Error) {
            Monitoring.error(err)
        }
    }

    val subscriptionScope = CoroutineScope(Dispatchers.IO)
    fun subscribe() {
        subscriptionScope.launch {
            client.subscription(CoreSubscription()).toFlow().collectLatest {
                it.data?.let {
                    InAppChat.shared.scope.launch {
                        Chats.current.onCoreEvent(it.core)
                    }
                } ?: it.errors?.forEach {
                    Monitoring.error(it.message)
                }
            }

            client.subscription(MeSubscription()).toFlow().collectLatest {
                it.data?.let {
                    InAppChat.shared.scope.launch {
                        Chats.current.onMeEvent(it.me)
                    }
                } ?: it.errors?.forEach {
                    Monitoring.error(it.message)
                }
            }
        }
    }

    fun unsubscribe() {
        subscriptionScope.cancel()
    }

    fun onToken(access: String) {
        authToken = access
    }

    suspend fun login(
        accessToken: String?,
        userId: String,
        username: String,
        displayName: String?,
        picture: String?
    ) {
        val res = client.mutation(
            LoginMutation(
                LoginInput(
                    user_id = userId,
                    access_token = Optional.presentIfNotNull(accessToken),
                    image = Optional.presentIfNotNull(picture),
                    username = username,
                    display_name = Optional.presentIfNotNull(displayName)
                )
            )
        ).execute().dataOrThrow().login
        if (res != null) {
            onLogin(res.token, res.user.fUser)
            return
        }
        throw Error("There was a problem logging in")
    }

    suspend fun nftLogin(
        wallet: String,
        tokenID: String,
        signature: String,
        picture: String?,
        username: String,
        displayName: String?
    ) {
        val res = client.mutation(
            EthLoginMutation(
                EthLoginInput(
                    wallet = wallet,
                    signed_message = signature,
                    token_id = tokenID,
                    username = username,
                    image = Optional.presentIfNotNull(picture)
                )
            )
        ).execute().dataOrThrow().ethLogin
        if (res != null) {
            onLogin(res.token, res.user.fUser)
        } else {
            throw Error("There was a problem logging in")
        }
    }

    suspend fun getUser(id: String): User? =
        client.query(GetUserQuery(id)).execute().data?.user?.fUser?.let { User.get(it) }

    suspend fun logout() {
        client.mutation(LogoutMutation()).execute().data?.logout
        unsubscribe()
        authToken = null
    }

    suspend fun block(id: String, isBlock: Boolean) =
        if (isBlock)
            client.mutation(BlockMutation(id)).execute().data?.block
        else
            client.mutation(UnblockMutation(id)).execute().data?.unblock


    suspend fun updateProfile(input: UpdateProfileInput) = client.mutation(
        UpdateProfileMutation(
            input
        )
    ).execute().data?.updateProfile

    suspend fun updateNotifications(setting: NotificationSetting) = updateProfile(
        UpdateProfileInput(
            notification_setting = Optional.present(
                setting
            )
        )
    )


    suspend fun updateAvailability(status: OnlineStatus) = updateProfile(
        UpdateProfileInput(
            status = Optional.present(
                status
            )
        )
    )

    suspend fun updateChatNotifications(id: String, setting: NotificationSetting) =
        client.mutation(SetNotificationSettingMutation(id, setting)).execute()
            .data?.setNotificationSetting

    suspend fun getContacts(existing: List<String>) =
        client.mutation(SyncContactsMutation(existing)).execute().data?.syncContacts

    suspend fun chats(skip: Int, limit: Int) =
        client.query(
            ListGroupsQuery(
                count = Optional.present(limit),
                offset = Optional.present(skip)
            )
        )
            .execute().dataOrThrow().groups.map { Chat.get(it.fChat) }

    suspend fun getMessage(id: String) = client.query(GetMessageQuery(id)).execute()
        .dataOrThrow().message?.let { Message.get(it.fMessage) }

    suspend fun getReplies(mid: String, skip: Int, limit: Int) =
        client.query(
            ListRepliesQuery(
                mid,
                skip = Optional.present(skip),
                limit = Optional.present(limit)
            )
        )
            .execute().dataOrThrow().replies.map { Message.get(it.fMessage) }

    suspend fun registerFcmToken(token: String) =
        client.mutation(RegisterPushMutation(token, DeviceType.ANDROID, Optional.present(true)))
//            .execute()
//            .dataOrThrow().registerPush

    suspend fun me(): User =
        client.query(GetMeQuery()).execute().dataOrThrow().let { data ->
            data.memberships.forEach {
                Chat.get(it.chat.fChat)
            }
            Chats.current.memberships.addAll(data.memberships.map { Member.get(it.fMember) })
            data.me.let {
                Chats.current.settings.blocked.addAll(it.blocks ?: listOf())
                User.get(it.fUser)
            }
        }

}