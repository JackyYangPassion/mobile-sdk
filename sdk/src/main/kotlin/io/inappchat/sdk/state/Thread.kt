/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import io.inappchat.sdk.API
import io.inappchat.sdk.models.APIThread
import io.inappchat.sdk.models.NotificationSettings
import io.inappchat.sdk.utils.op

@Stable
data class Thread(override val id: String, val user: User? = null, val group: Group? = null) :
    Pager<Message>(), Identifiable {

    var unreadCount by mutableStateOf(0)
    var typingUsers = mutableStateListOf<User>()
    var sending = mutableStateListOf<Message>()
    var failed = mutableStateListOf<Message>()
    var notification by mutableStateOf(NotificationSettings.AllowFrom.all)
    var latest by mutableStateOf<Message?>(null)

    val name: String get() = group?.name ?: user?.usernameFb ?: ""
    val image: String? get() = group?.avatar ?: user?.avatar

    val isUnread: Boolean
        get() = unreadCount > 0

    val path: String
        get() {
            if (user != null) {
                return "/user/${user.id}/chat"
            } else if (group != null) {
                return "/group/${group.id}"
            }
            return ""
        }

    fun cache() {
        Chats.current.cache.threads[id] = this
        user?.let {
            Chats.current.cache.threadsByUID[it.id] = this
        }
        group?.let {
            Chats.current.cache.threadsByGroup[it.id] = this
        }
    }

    init {
        cache()
    }

    fun addMessage(message: Message) {
        items.add(0, message)
    }

    var deleting by mutableStateOf(false)

    constructor(thread: APIThread) : this(
        thread.threadId,
        thread.user?.let { User.get(it) },
        thread.group?.let { Group.get(it) }) {
        update(thread)
    }

    fun update(thread: APIThread) {
        thread.lastMessage?.let { latest = Message.get(it) }
    }

    fun set(notifications: NotificationSettings.AllowFrom, isSync: Boolean) {
        this.notification = notifications
        if (isSync) {
            return
        }
        op({
            API.updateThreadNotifications(id, notifications)
        })
    }

    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.getMessages(id, skip, items.lastOrNull()?.id)
    }

    companion object {
        fun get(id: String) = Chats.current.cache.threads[id]
        fun getByGroup(id: String) = Chats.current.cache.threadsByGroup[id]
        fun getByUser(id: String) = Chats.current.cache.threadsByUID[id]
        fun get(thread: APIThread): Thread {
            val t = get(thread.threadId)
            if (t != null) {
                t.update(thread)
                thread.group?.let { Group.get(it) }
                thread.user?.let { User.get(it) }
                return t
            }
            return Thread(thread)
        }

        fun fetch(id: String) {
            if (Chats.current.cache.threadFetches.contains(id)) return
            Chats.current.cache.threadFetches.add(id)
            op({
                API.getThread(id)
            })
        }
    }
}