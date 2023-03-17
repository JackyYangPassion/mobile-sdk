/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.inappchat.sdk.API
import io.inappchat.sdk.extensions.contains
import io.inappchat.sdk.models.APIUser
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.Participant
import io.inappchat.sdk.utils.instant
import io.inappchat.sdk.utils.op
import java.time.Instant

@Stable
data class User(
  override val id: String
) : Identifiable {
  var email by mutableStateOf("")
  var username by mutableStateOf("")
  var displayName by mutableStateOf<String?>("")
  var avatar by mutableStateOf<String?>(null)
  var lastSeen by mutableStateOf<Instant?>(null)
  var status by mutableStateOf(AvailabilityStatus.offline)
  var statusMessage by mutableStateOf<String?>(null)
  var blocked by mutableStateOf(false)
  var haveContact: Boolean by mutableStateOf(false)

  constructor(user: APIUser, blocked: Boolean = false, haveContact: Boolean = false) : this(
    user.eRTCUserId
  ) {
    this.email = user.appUserId
    this.username = user.name ?: ""
    this.displayName = user.name
    this.avatar = user.profilePic ?: user.profilePicThumb
    this.lastSeen = user.loginTimeStamp?.instant()
    this.status = user.availabilityStatus ?: AvailabilityStatus.offline
    this.statusMessage = user.profileStatus
    this.blocked = blocked
    this.haveContact = haveContact
  }

  constructor(participant: Participant) : this(
    participant.eRTCUserId
  ) {
    this.email = participant.appUserId
  }

  init {
    Chats.current.cache.users[id] = this
    fetch()
  }

  fun update(user: APIUser) {
    if (email.isBlank()) {
      email = user.appUserId
    }
    user.name?.let { username = it }
    (user.profilePic ?: user.profilePicThumb)?.let { avatar = it }
    user.loginTimeStamp?.let { lastSeen = it.instant() }
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
  val haveChatWith: Boolean get() = Chats.current.users.items.contains { it.user?.id == id }

  @Stable
  val usernameFb: String get() = if (!username.isBlank()) username else displayName ?: email

  @Stable
  val displayNameFb: String get() = displayName ?: if (!username.isBlank()) username else email

  @Stable
  val isCurrent: Boolean get() = current?.id == id

  var blocking by mutableStateOf(false)

  @Stable
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

    fun fetched(id: String) =
      get(id) ?: User(id)

  }
}

fun Collection<User>.usernames(): String {
  val uns = this.toList().slice(0..Math.min(size, 2)).map { it.usernameFb }
  if (uns.size < 3) {
    return uns.joinToString(" and ")
  } else if (uns.size == 3) {
    return "${uns[0]}, ${uns[1]} and ${uns[2]}"
  } else {
    return "${uns[0]}, ${uns[1]} and ${size - 3} others"
  }
}