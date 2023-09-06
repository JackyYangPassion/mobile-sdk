/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import android.net.Uri
import androidx.compose.runtime.toMutableStateList
import io.inappchat.sdk.API
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.SendingMessage
import io.inappchat.sdk.state.User
import io.inappchat.sdk.type.AttachmentInput
import io.inappchat.sdk.type.AttachmentType
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.uuid


fun Message.react(emoji: String) {
    if (reacting) return
    reacting = true
    val og = reactions.toList().map { it.first to it.second.toList() }
    val ogCurrent = currentReaction
    io.inappchat.sdk.utils.react(User.current!!.id, emoji, reactions)
    op({
        bg {
            API.react(id, emoji)
        }
        reacting = false
    }) {
        reactions.removeAll { true }
        reactions.addAll(og.map { it.first to it.second.toMutableStateList() })
        currentReaction = ogCurrent
        reacting = false
    }
}


fun Message.toggleFavorite() {
    if (favoriting) return
    favoriting = true
    favorite = !favorite
    op({
        bg {
            API.favorite(id, favorite)
        }
        favoriting = false
    }) {
        favorite = !favorite
        favoriting = false
    }
}

fun Message.editText(newText: String) {
    if (editingText) return
    editingText = true
    val og = text
    text = newText
    op({
        bg {
            API.editMessageText(id, newText)
        }
        editingText = false
    }) {
        text = og
        editingText = false
    }
}

fun Message.delete() {
    parent?.replies?.items?.remove(this)
    chat.items.remove(this)
    InAppChatStore.current.cache.messages.remove(id)
    op({
        bg { API.deleteMessage(id) }
    })
}

fun Uri.imageAttachment() = AttachmentInput(
    url = this.toString(),
    type = AttachmentType.image,
    id = uuid()
)

fun SendingMessage.retry() {
    if (failed) {
        failed = false
        msg.chat.send(this)
    }
}