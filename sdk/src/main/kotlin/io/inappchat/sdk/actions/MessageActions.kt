/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import io.inappchat.sdk.API
import io.inappchat.sdk.models.MessageStatus
import io.inappchat.sdk.models.Reaction
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import java.lang.Integer.max

fun Message.decr(emoji: String) {
    val i = reactions.indexOfFirst { it.emojiCode == emoji }
    if (i < 0) return
    val r = reactions[i].copy(
        count = max(reactions[i].count - 1, 0),
        users = reactions[i].users.filter { it != User.current?.id })
    reactions.removeAt(i)
    if (!r.users.isEmpty()) {
        reactions.add(i, r)
    }
}


fun Message.updateStatus(status: MessageStatus = MessageStatus.seen) {
    if (user.isCurrent) return
    if (thread?.group != null) return
    if (status != MessageStatus.seen) {
        val og = status
        this.status = MessageStatus.seen
        op({
            bg {
                API.updateMessageStatus(id, MessageStatus.seen)
            }
        }) {
            this.status = og
        }
    }
}

fun Message.react(emoji: String) {
    if (reacting) return
    reacting = true
    val isSet = currentReaction != emoji
    val og = reactions.toList()
    val ogCurrent = currentReaction
    if (isSet) {
        val i = reactions.indexOfFirst { it.emojiCode == emoji }
        if (i > -1) {
            val r = reactions[i]
            val rn = r.copy(count = r.count + 1, users = r.users + User.current!!.id)
            reactions[i] = rn
        } else {
            reactions.add(Reaction(emoji, 1, listOf(User.current!!.id)))
        }
        currentReaction?.let { decr(it) }
        currentReaction = emoji
        Chats.current.settings.onReaction(emoji)
    } else {
        decr(emoji)
        currentReaction = null
    }
    op({
        bg {
            if (isSet) API.react(id, emoji)
            else API.unreact(id, emoji)
        }
        reacting = false
    }) {
        reactions.removeAll { true }
        reactions.addAll(og)
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
    thread?.items?.remove(this)
    Chats.current.cache.messages.remove(id)
    op({
        bg { API.deleteMessage(id) }
    })
}