/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.type.ChatType
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.op
import java.util.*
import kotlin.collections.set
import kotlin.properties.Delegates

@Stable
data class Chats(val id: String = UUID.randomUUID().toString()) {

    val messages = ThreadPager()
    val favorites = FavoritesPager()
    val settings = Settings()
    val network = ChannelsPager()
    val contacts = ContactsPager()
    val invites = mutableStateMapOf<String, MutableList<User>>()
    val cache = Caches()
    val memberships = mutableStateListOf<Member>()
    val dms: kotlin.collections.List<Chat>
        get() = memberships.filter { it.isMember && it.chat.kind == ChatType.DirectMessage }
            .map { it.chat }
    val groups: kotlin.collections.List<Chat>
        get() = memberships.filter { it.isMember && it.chat.kind == ChatType.Group }
            .map { it.chat }

    var fcmToken: String? = null

    var loading by mutableStateOf(false)
    var loggedIn by mutableStateOf(false)

    var currentUserID: String? by Delegates.observable(null) { _, _, newValue ->
        InAppChat.shared.prefs.edit().putString("user-id", newValue).apply()
    }

    var user by mutableStateOf<User?>(null)

    fun init() {
        Monitoring.setup(InAppChat.shared.appContext)
        API.init()
        settings.init()
    }

    fun load() {
        if (loading) return
        loading = true
        op({
            loadAsync()
        })
    }

    suspend fun loadAsync() {
        currentUserID ?: return
        val user = API.me()
        User.current = user
        val fcmToken = this.fcmToken
        if (fcmToken != null) {
            API.registerFcmToken(fcmToken)
        }
        loadInvites()
    }

    private suspend fun loadInvites() {
        val invites = API.invites()
        for (invite in invites) {
            val users = this.invites[invite.chatId] ?: mutableListOf()
            val user = User.fetched(invite.by)
            if (!users.contains(user)) {
                users.add(user)
            }
            this.invites[invite.chatId] = users
        }
    }

    enum class List(val label: String) {
        chats("Channels"),
        users("Chat"),
        threads("Chats");

        companion object {
            fun forRoute(r: String?): List {
                if (r == null) return chats
                when (r) {
                    "channels" -> chats
                    "chats" -> users
                    "chats" -> chats
                }
                return chats
            }
        }
    }

    fun count(list: List): Int =
        when (list) {
            List.users ->
                dms.sumOf { it.unreadCount }

            List.chats ->
                groups.sumOf { it.unreadCount }

            List.chats ->
                0

            else -> 0
        }

    var nextGif: ((String) -> Unit)? = null


    @Stable
    companion object {
        var current = Chats()
    }
}