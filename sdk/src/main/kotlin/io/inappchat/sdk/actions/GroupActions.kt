/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import io.inappchat.sdk.API
import io.inappchat.sdk.models.Participant
import io.inappchat.sdk.models.UpdateGroupInput
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.launch
import io.inappchat.sdk.utils.op
import java.io.File
import java.time.OffsetDateTime

fun Group.dismissInvites() {
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

fun Group.join() {
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
                API.joinGroup(id)
            }
        }
        joining = false
    }) {
        participants.remove(p)
        joining = false
    }
}

fun Group.leave() {
    if (!isMember || joining) return
    joining = true
    val p = participants.find { it.eRTCUserId == User.current?.id }
    if (p == null) return
    participants.remove(p)
    op({
        bg {
            API.leaveGroup(id)
        }
        joining = false
    }) {
        participants.add(p)
        joining = false
    }
}

fun Group.update(
    name: String?,
    description: String?,
    image: File?,
    _private: Boolean?
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
            API.updateGroup(
                id,
                name = name,
                description = description,
                profilePic = image,
                groupType = _private?.let { if (it) UpdateGroupInput.GroupType.private else UpdateGroupInput.GroupType.public }
            )
        }
        updating = false
    }) {
        this.name = ogName
        this.description = ogDescription
        avatar = ogImage
        this._private = ogPrivate
        updating = false
    }
}

fun Group.delete() {
    op({
        bg { API.deleteGroup(id) }
        Chats.current.cache.groups.remove(id)
        Chats.current.network.items.removeAll { it.id == id }
        io.inappchat.sdk.state.Thread.getByGroup(id)?.let { t ->
            Chats.current.cache.threads.remove(t.id)
            Chats.current.groups.items.removeAll { t.id == it.id }
        }
        Chats.current.cache.threadsByGroup.remove(id)
    })
}

fun Group.invite(users: List<User>) {
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