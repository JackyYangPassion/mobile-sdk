/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.inappchat.sdk.API
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.Room
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.ui.views.Spinner
import io.inappchat.sdk.utils.Monitoring


@Composable
fun ChatRoute(
  gid: String? = null, uid: String? = null, mid: String? = null,
  openProfile: (User) -> Unit,
  openInvite: (Group) -> Unit,
  openEditGroup: (Group) -> Unit,
  openReply: (Message) -> Unit,
  back: () -> Unit
) {
  var room by remember {
    mutableStateOf<Room?>(gid?.let { Room.getByGroup(it) } ?: uid?.let { Room.getByUser(it) }
    ?: mid?.let { Message.Companion.get(it)?.room })
  }
  var notFound by remember {
    mutableStateOf(false)
  }
  LaunchedEffect(key1 = gid, block = {
    if (gid != null && room == null) {
      try {
        room = API.getGroupThread(gid)
      } catch (err: Exception) {
        Monitoring.error(err)
        notFound = true
      } catch (err: Error) {
        Monitoring.error(err)
        notFound = true
      }
    }
  })

  LaunchedEffect(key1 = uid, block = {
    if (uid != null && room == null) {
      try {
        room = API.getUserThread(uid)
      } catch (err: Exception) {
        Monitoring.error(err)
        notFound = true
      } catch (err: Error) {
        Monitoring.error(err)
        notFound = true
      }
    }
  })

  LaunchedEffect(key1 = mid, block = {
    if (mid != null && room == null) {
      try {
        val m = Message.get(mid) ?: API.getMessage(mid)
        room = Room.get(m.threadID) ?: API.getThread(m.threadID)
      } catch (err: Exception) {
        Monitoring.error(err)
        notFound = true
      } catch (err: Error) {
        Monitoring.error(err)
        notFound = true
      }
    }
  })

  room?.let {
    ChatRoom(
      room = it, openProfile = openProfile, openInvite = openInvite, openReply = openReply,
      openEdit = openEditGroup, back = back
    )
  } ?: if (notFound) NotFound(back = back) else Column {
    Header(title = "Chat")
    Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
      Spinner()
    }
  }
}