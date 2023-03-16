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
import io.inappchat.sdk.utils.uuid
import java.util.*

@Stable
data class ChannelsPager(val id: String = UUID.randomUUID().toString()) : Pager<Group>() {
    override suspend fun load(skip: Int, limit: Int): List<Group> {
        return API.groups(skip, limit)
    }
}


@Stable
data class ContactsPager(val id: String = UUID.randomUUID().toString()) : Pager<User>() {

    var requestContacts by mutableStateOf(true)
    var contacts = mutableStateListOf<String>()

    fun fetchContacts(): List<String> {
        val contacts =
            Contacts(InAppChat.appContext).query()
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

    override var isSinglePage = true

    override suspend fun load(skip: Int, limit: Int): List<User> {
        val contacts = fetchContacts()
        return API.getContacts(contacts)
    }
}

@Stable
data class FavoritesPager(val id: String = uuid()) : Pager<Message>() {
    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.favorites(skip, limit)
    }
}

@Stable
data class GroupsPager(val id: String = uuid()) : Pager<Room>() {
    override suspend fun load(skip: Int, limit: Int): List<Room> {
        return API.getJoinedGroupThreads(skip, limit)
    }

}

@Stable
data class RepliesPager(val message: Message) : Pager<Message>() {
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

@Stable
data class ThreadsPager(val id: String = uuid()) : Pager<Message>() {
    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.getReplyThreads(skip, limit)
    }

}

@Stable
data class UserSharedMedia(val user: User) : Pager<Message>() {
    override suspend fun load(skip: Int, limit: Int): List<Message> {
        return API.getSharedMedia(user.id)
    }

}

@Stable
data class UserThreadsPager(val id: String = uuid()) : Pager<Room>() {
    override suspend fun load(skip: Int, limit: Int): List<Room> {
        return API.getJoinedUserThreads(skip, limit)
    }
}