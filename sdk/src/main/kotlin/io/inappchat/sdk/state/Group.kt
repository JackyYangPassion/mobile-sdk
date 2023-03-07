/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import io.inappchat.sdk.API
import io.inappchat.sdk.extensions.contains
import io.inappchat.sdk.models.APIGroup
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.Participant


fun Participant.user(): User = User.get(this)

@Stable
data class Group(val id: String) {

    init {
        Chats.current.cache.groups[id] = this
    }

    var name by mutableStateOf("")
    var avatar by mutableStateOf<String?>(null)
    var description by mutableStateOf<String?>(null)
    val participants = mutableStateListOf<Participant>()
    var _private by mutableStateOf(false)
    val invites = mutableStateListOf<User>()
    val isAdmin: Boolean
        get() =
            participants.contains { it.eRTCUserId == User.current?.id && it.role == Participant.Role.admin }
    val path: String
        get() = "/group/$id"

    val editPath: String
        get() = "/group/$id/edit"

    val invitePath: String
        get() = "/group/$id/invite"

    @Stable
    val admins: List<User>
        get() = participants.filter { it.role == Participant.Role.admin }.map { it.user() }

    @Stable
    val onlineNotAdminUsers: List<User>
        get() = participants.filter { it.role != Participant.Role.admin }.map { it.user() }
            .filter { it.status != AvailabilityStatus.offline }

    @Stable
    val offlineNotAdminUsers: List<User>
        get() = participants.filter { it.role != Participant.Role.admin }.map { it.user() }
            .filter { it.status != AvailabilityStatus.online }


    constructor(group: APIGroup) : this(group.groupId) {
        update(group)
    }

    fun update(group: APIGroup) {
        this.name = group.name
        this.description = group.description
        this.avatar = group.profilePic ?: group.profilePicThumb
        this._private = group.groupType == APIGroup.GroupType.private
        group.participants?.let {
            this.participants.removeAll { true }
            this.participants.addAll(it)
        }
    }

    var updating by mutableStateOf(false)
    var joining by mutableStateOf(false)
    var inviting by mutableStateOf(false)

    companion object {
        fun get(id: String) = Chats.current.cache.groups[id]

        fun get(group: APIGroup): Group {
            val g = get(group.groupId)
            if (g != null) {
                g.update(group)
                return g
            }
            return Group(group)
        }
    }
}