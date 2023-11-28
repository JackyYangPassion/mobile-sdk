/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ai.botstacks.sdk.API
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.Spinner
import ai.botstacks.sdk.utils.Monitoring


@Composable
fun ChatRoute(
    gid: String? = null, uid: String? = null, mid: String? = null,
    openProfile: (User) -> Unit,
    openInvite: (Chat) -> Unit,
    openEditChat: (Chat) -> Unit,
    openReply: (Message) -> Unit,
    back: () -> Unit
) {
    println("Chat route with uid $uid")
    var chat by remember {
        mutableStateOf<Chat?>(gid?.let { Chat.get(it) } ?: uid?.let { Chat.getByUser(it) }
        ?: mid?.let { Message.Companion.get(it)?.chat })
    }
    var notFound by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = gid, block = {
        if (gid != null && chat == null) {
            try {
                chat = API.getChat(gid)
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
        if (uid != null && chat == null) {
            try {
                chat = API.dm(uid)
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
        if (mid != null && chat == null) {
            try {
                val m = Message.get(mid) ?: API.getMessage(mid)
                m?.let {
                    chat = Chat.get(it.chatID) ?: API.getChat(it.chatID)
                }
            } catch (err: Exception) {
                Monitoring.error(err)
                notFound = true
            } catch (err: Error) {
                Monitoring.error(err)
                notFound = true
            }
        }
    })

    chat?.let {
        ChatChat(
            chat = it, openProfile = openProfile, openInvite = openInvite, openReply = openReply,
            openEdit = openEditChat, back = back
        )
    } ?: if (notFound) NotFound(back = back) else Column {
        Header(title = "Chat")
        Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
            Spinner()
        }
    }
}