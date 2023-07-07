/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import com.apollographql.apollo3.api.Optional
import io.inappchat.sdk.API
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Member
import io.inappchat.sdk.state.User
import io.inappchat.sdk.type.MemberRole
import io.inappchat.sdk.type.UpdateGroupInput
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import kotlinx.datetime.Clock
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
    if (joining || isMember) return
    joining = true
    val p = Member(
        user_id = User.current!!.id,
        chat_id = id,
        created_at = Clock.System.now(),
        role = MemberRole.Member,
    )
    members.add(p)
    op({
        bg {
            API.joinChat(id)
        }
        joining = false
    }) {
        members.remove(p)
        joining = false
    }
}

fun Chat.leave() {
    if (!isMember || joining) return
    joining = true
    if (!isMember) return
    val membership = this.membership
    membership?.let { members.remove(it) }
    op({
        bg {
            API.leaveChat(id)
        }
        joining = false
        Chats.current.memberships.removeIf { it.chat_id == membership?.chat_id }
    }) {
        membership?.let { members.add(it) }
        joining = false
    }
}

fun Chat.update(
    name: String?,
    description: String?,
    image: String?,
    _private: Boolean?,
    onComplete: () -> Unit = {}
) {
    if (updating) {
        return
    }
    updating = true
    val ogName = this.name
    val ogDescription = this.description
    val ogImage = this.image
    val ogPrivate = this._private
    name?.let {
        this.name = it
    }
    description?.let {
        this.description = it
    }
    image?.let {
        this.image = it
    }
    _private?.let {
        this._private = it
    }
    op({
        bg {
            API.updateChat(
                UpdateGroupInput(
                    id = id,
                    name = Optional.present(name),
                    description = Optional.present(description),
                    image = Optional.present(image),
                    _private = Optional.present(_private)
                )
            )
        }
        updating = false
        onComplete()
    }) {
        this.name = ogName
        this.description = ogDescription
        this.image = ogImage
        this._private = ogPrivate
        updating = false
    }
}

fun Chat.delete() {
    op({
        bg { API.deleteChat(id) }
        friend?.let { Chats.current.cache.chatsByUID.remove(it.id) }
        Chats.current.cache.chats.remove(id)
        Chats.current.network.items.removeAll { it.id == id }
        Chats.current.memberships.removeAll { it.chat_id == id }
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