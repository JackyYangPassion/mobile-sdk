/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.state

import androidx.compose.runtime.*
import ai.botstacks.sdk.API
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.type.ChatType
import ai.botstacks.sdk.utils.Monitoring
import java.util.*
import kotlin.properties.Delegates

@Stable
data class BotStacksChatStore(val id: String = UUID.randomUUID().toString()) {

    //    val messages = ThreadPager()
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

    var currentUserID: String? by Delegates.observable(
        BotStacksChat.shared.prefs.getString(
            "user-id",
            null
        )
    ) { _, _, newValue ->
        BotStacksChat.shared.prefs.edit().putString("user-id", newValue).apply()
    }

    var user by mutableStateOf<User?>(null)

    fun init() {
        Monitoring.setup(BotStacksChat.shared.appContext)
        API.init()
        settings.init()
    }

    suspend fun loadAsync() {
        currentUserID ?: return
        val user = API.me()
        print("user id ${user.id}")
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
            val chat = Chat.get(invite.chat.fChat)
            val user = User.get(invite.user.fUser)
            if (!chat.invites.contains(user))
                chat.invites.add(user)
        }
    }

    enum class List(val label: String) {
        groups("Channels"),
        dms("Chat");

        companion object {
            fun forRoute(r: String?): List {
                if (r == null) return dms
                when (r) {
                    "channels" -> groups
                    "dms" -> dms
                }
                return dms
            }
        }
    }

    fun count(list: List): Int =
        when (list) {
            List.dms -> {
                val it = dms.sumOf { it.unreadCount }
                println("Dms unread count $it")
                it
            }

            List.groups -> {
                val it = groups.sumOf { it.unreadCount }
                print("groups unread count $it")
                it
            }
        }

    val totalCount: Int
        get() = dms.sumOf { it.unreadCount } + groups.sumOf { it.unreadCount }
    var nextGif: ((String) -> Unit)? = null


    @Stable
    companion object {
        var current = BotStacksChatStore()
    }
}