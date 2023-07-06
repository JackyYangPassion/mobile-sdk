/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import io.inappchat.sdk.API
import io.inappchat.sdk.models.Participant
import io.inappchat.sdk.models.UpdateChatInput
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import java.io.File
import java.time.OffsetDateTime

fun Chat.dismissInvites() {
    if (invites.isEmpty()) return
    val og = invites.toList()
    invites.removeAll { true }
    op({
        bg {
            API.dismissInvites(id)
        }
    }) {
        invites.addAll(og)
    }
}

fun Chat.join() {
    if (joining) return
    joining = true
    val p = Participant(
        User.current!!.email,
        User.current!!.id,
        Participant.Role.user,
        OffsetDateTime.now()
    )
    participants.add(p)
    op({
        bg {
            if (!invites.isEmpty()) {
                API.acceptInvites(id)
            } else {
                API.joinChat(id)
            }
        }
        joining = false
    }) {
        participants.remove(p)
        joining = false
    }
}

fun Chat.leave() {
    if (!isMember || joining) return
    joining = true
    val p = participants.find { it.eRTCUserId == User.current?.id }
    if (p == null) return
    participants.remove(p)
    op({
        bg {
            API.leaveChat(id)
        }
        joining = false
    }) {
        participants.add(p)
        joining = false
    }
}

fun Chat.update(
    name: String?,
    description: String?,
    image: File?,
    _private: Boolean?,
    onComplete: () -> Unit = {}
) {
    if (updating) {
        return
    }
    updating = true
    val ogName = this.name
    val ogDescription = this.description
    val ogImage = this.avatar
    val ogPrivate = this._private
    name?.let {
        this.name = it
    }
    description?.let {
        this.description = it
    }
    image?.let {
        this.avatar = it.absolutePath
    }
    _private?.let {
        this._private = it
    }
    op({
        bg {
            API.updateChat(
                id,
                name = name,
                description = description,
                profilePic = image,
                chatType = _private?.let { if (it) UpdateChatInput.ChatType.private else UpdateChatInput.ChatType.public }
            )
        }
        updating = false
        onComplete()
    }) {
        this.name = ogName
        this.description = ogDescription
        avatar = ogImage
        this._private = ogPrivate
        updating = false
    }
}

fun Chat.delete() {
    op({
        bg { API.deleteChat(id) }
        Chats.current.cache.chats.remove(id)
        Chats.current.network.items.removeAll { it.id == id }
        io.inappchat.sdk.state.Chat.getByChat(id)?.let { t ->
            Chats.current.cache.chats.remove(t.id)
            Chats.current.chats.items.removeAll { t.id == it.id }
        }
        Chats.current.cache.chatsByChat.remove(id)
    })
}

fun Chat.invite(users: List<User>) {
    if (inviting) {
        return
    }
    inviting = true
    op({
        bg {
            API.invite(users.map { it.id }, id)
        }
        inviting = false
    }) {
        inviting = false
    }
}