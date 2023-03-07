/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state.user

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.inappchat.sdk.API
import io.inappchat.sdk.extensions.contains
import io.inappchat.sdk.models.APIUser
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.Participant
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.utils.localDateTime
import io.inappchat.sdk.utils.op
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "email")
    @NonNull
    var email: String,

    @ColumnInfo(name = "username")
    @NonNull
    var username: String,

    @ColumnInfo(name = "display_name")
    var displayName: String?,

    @ColumnInfo(name = "avatar")
    var avatar: String?,

    @ColumnInfo(name = "last_seen")
    var lastSeen: LocalDateTime?,

    @ColumnInfo(name = "status")
    var status: AvailabilityStatus,

    @ColumnInfo(name = "status_message")
    var statusMessage: String?,

    @ColumnInfo(name = "blocked")
    var blocked: Boolean,

    @ColumnInfo(name = "haveContact")
    var haveContact: Boolean
) : Parcelable {

    constructor(user: APIUser, blocked: Boolean = false, haveContact: Boolean = false) : this(
        user.eRTCUserId,
        user.appUserId,
        user.name ?: "",
        user.name,
        user.profilePic ?: user.profilePicThumb,
        user.loginTimeStamp?.localDateTime(),
        user.availabilityStatus ?: AvailabilityStatus.offline,
        user.profileStatus,
        blocked, haveContact
    )

    constructor(participant: Participant) : this(
        participant.eRTCUserId,
        participant.appUserId,
        "",
        null,
        null,
        null,
        AvailabilityStatus.offline,
        null,
        false, false
    )

    init {
        fetch()
    }

    fun update(user: APIUser) {
        if (email.isBlank()) {
            email = user.appUserId
        }
        user.name?.let { username = it }
        (user.profilePic ?: user.profilePicThumb)?.let { avatar = it }
        user.loginTimeStamp?.let { lastSeen = it.localDateTime() }
        user.profileStatus?.let { statusMessage = it }
        user.availabilityStatus?.let { status = it }
    }

    fun fetch() {
        op({
            API.getUser(id)
        })
    }

    val path: String get() = "/user/$id"
    val chatPath: String get() = "/user/$id/chat"

    @Stable
    val haveChatWith: Boolean get() = Chats.current.users.contains { it.user?.id == id }

    @Stable
    val usernameFb: String get() = if (!username.isBlank()) username else displayName ?: email

    @Stable
    val displayNameFb: String get() = displayName ?: if (!username.isBlank()) username else email

    @Stable
    val isCurrent: Boolean get() = current?.id == id

    var blocking by mutableStateOf(false)

    companion object {
        var current by mutableStateOf<User?>(null)

        fun get(id: String): User? {
            return Chats.current.cache.users[id]
        }

        fun get(participant: Participant): User {
            val user: User? = get(participant.eRTCUserId)
            if (user != null) {
                return user
            }
            return User(participant)
        }

        fun get(user: APIUser): User {
            val u = get(user.eRTCUserId)
            if (u != null) {
                u.update(user)
                return u
            }
            return User(user)
        }
    }
}
