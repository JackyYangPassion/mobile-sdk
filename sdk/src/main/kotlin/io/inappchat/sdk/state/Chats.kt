/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.*
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.op
import java.util.*
import kotlin.collections.set
import kotlin.properties.Delegates

@Stable
data class Chats(val id: String = UUID.randomUUID().toString()) {

  val groups = GroupsPager()
  val users = UserThreadsPager()
  val messages = ThreadsPager()
  val favorites = FavoritesPager()
  val settings = Settings()
  val network = ChannelsPager()
  val contacts = ContactsPager()
  val invites = mutableStateMapOf<String, MutableList<User>>()
  val cache = Caches()

  var fcmToken: String? = null

  var loading by mutableStateOf(false)
  var loggedIn by mutableStateOf(false)

  var currentUserID: String? by Delegates.observable(null) { _, _, newValue ->
    InAppChat.shared.prefs.edit().putString("user-id", newValue).apply()
  }

  var user by mutableStateOf<User?>(null)

  fun init() {
    Monitoring.setup(InAppChat.shared.appContext)
    API.init()
    settings.init()
  }

  fun load() {
    if (loading) return
    loading = true
    op({
      loadAsync()
    })
  }

  suspend fun loadAsync() {
    currentUserID ?: return
    val user = API.me()
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
      val users = this.invites[invite.groupId] ?: mutableListOf()
      val user = User.fetched(invite.by)
      if (!users.contains(user)) {
        users.add(user)
      }
      this.invites[invite.groupId] = users
    }
  }

  enum class List(val label: String) {
    groups("Channels"),
    users("Chat"),
    threads("Threads");

    companion object {
      fun forRoute(r: String?): List {
        if (r == null) return groups
        when (r) {
          "channels" -> groups
          "chats" -> users
          "threads" -> threads
        }
        return groups
      }
    }
  }

  fun count(list: List): Int =
    when (list) {
      List.users ->
        users.items.sumOf { it.unreadCount }
      List.groups ->
        groups.items.sumOf { it.unreadCount }
      List.threads ->
        0
    }

  var nextGif: ((String) -> Unit)? = null


  @Stable
  companion object {
    var current = Chats()
  }
}