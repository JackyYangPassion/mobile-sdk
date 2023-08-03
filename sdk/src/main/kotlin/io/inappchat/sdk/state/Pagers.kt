/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import contacts.core.Contacts
import contacts.core.isNotNullOrEmpty
import contacts.core.or
import contacts.core.util.phones
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.uuid
import java.util.*

@Stable
data class ChannelsPager(val list: String = "channels") : Pager<Chat>() {
    override suspend fun load(skip: Int, limit: Int): List<Chat> {
        return API.chats(skip, limit)
    }
}


@Stable
data class ContactsPager(val list: String = "contacts") : Pager<User>() {

    var requestContacts by mutableStateOf(true)
    var contacts = mutableStateListOf<String>()

    fun fetchContacts(): List<String> {
        val contacts =
            Contacts(InAppChat.shared.appContext).query()
                .where { Phone.Number.isNotNullOrEmpty() or Phone.NormalizedNumber.isNotNullOrEmpty() }
                .include {
                    setOf(
                        Phone.Number,
                        Phone.NormalizedNumber
                    )
                }
                .find()
        return contacts.flatMap { it.phones().map { it.normalizedNumber ?: it.number ?: "" } }
    }

    fun syncContacts() {
        op({
            val contacts = fetchContacts()
            if (!contacts.isEmpty())
                bg {
                    API.getContacts(contacts)
                }
        })
    }

    override suspend fun load(skip: Int, limit: Int): List<User> {
        return API.getUsers(skip, limit)
    }
}

@Stable
data class FavoritesPager(val list: String = "favorites") : Pager<Message>() {
    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.favorites(skip, limit)
    }
}

@Stable
data class RepliesPager(val message: Message) : Pager<Message>(message.id) {
    init {
        Chats.current.cache.repliesPagers[message.id] = this
    }

    fun setReplies(list: List<Message>) {
        items.removeAll { true }
        items.addAll(list)
        hasMore = false
    }

    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.getReplies(message.id, skip, limit)
    }

}

//@Stable
//data class ThreadPager(val id: String = uuid()) : Pager<Message>() {
//    override suspend fun load(skip: Int, limit: Int): List<Message> {
//        return API.getReplyChats(skip, limit)
//    }
//
//}
//
//@Stable
//data class UserSharedMedia(val user: User) : Pager<Message>() {
//    override suspend fun load(skip: Int, limit: Int): List<Message> {
//        return API.getSharedMedia(user.id)
//    }
//
//}
