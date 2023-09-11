package io.inappchat.sdk.state

import android.util.Log
import io.inappchat.sdk.CoreSubscription
import io.inappchat.sdk.MeSubscription
import io.inappchat.sdk.fragment.FChat
import io.inappchat.sdk.fragment.FMember
import io.inappchat.sdk.fragment.FMessage
import io.inappchat.sdk.fragment.FUser
import io.inappchat.sdk.type.DeleteEntity
import io.inappchat.sdk.type.EntityEventType

fun InAppChatStore.onCreateMessage(it: FMessage) {
    val chat = Chat.get(it.chat_id)
    chat?.let { chat ->
        chat.addMessage(Message.get(it))
    }
}

fun InAppChatStore.onCreateMember(it: FMember) {
    val chat = Chat.get(it.chat_id) ?: return

}

fun InAppChatStore.onCreateUser(it: FUser) {

}

fun InAppChatStore.onCreateChat(it: FChat) {

}

fun InAppChatStore.onCoreEvent(event: CoreSubscription.Core) {
    Log.v("Events", "Got Core Event ${event}")
    event.onDeleteEvent?.fDelete?.let {
        when (it.kind) {
            DeleteEntity.Message -> {
                Message.get(it.id)?.let {
                    cache.messages.remove(it.id)
                    it.chat.items.remove(it)
                    favorites.items.remove(it)
                }
            }

            DeleteEntity.Device -> {
                null
            }

            DeleteEntity.Chat -> {
                Chat.get(it.id)?.let {
                    it.membership?.let {
                        memberships.remove(it)
                    }
                    network.items.remove(it)
                    cache.chats.remove(it.id)
                    if (it.isDM) {
                        it.friend?.let {
                            cache.chatsByUID.remove(it.id)
                        }
                    }
                }
            }

            else -> null
        }
    }
    event.onEntityEvent?.fEntity?.let {
        val isUpdate = it.type == EntityEventType.Update
        it.entity.onChat?.fChat?.let {
            val chat = Chat.get(it)
            if (!isUpdate)
                network.items.add(chat)
        }
        it.entity.onUser?.fUser?.let {
            val user = User.get(it)
            if (!isUpdate)
                contacts.items.add(user)
        }
        it.entity.onMember?.fMember?.let {
            Chat.get(it.chat_id)?.let { chat ->
                val member = Member.get(it)
                if (!isUpdate) {
                    chat.members.add(member)
                } else {
                    chat.members.indexOfFirst { it.user_id == member.user_id }.let {
                        chat.members[it] = member
                    }
                }
            }
        }
        it.entity.onMessage?.fMessage?.let {
            println("On new Message")
            Chat.get(it.chat_id)?.let { chat ->
                println("Have chat for message")
                val message = Message.get(it)
                if (!chat.items.contains(message)) {
                    chat.items.add(0, message)
                    if (Chat.currentlyViewed != message.chatID) {
                        chat.unreadCount += 1
                    }
                }
                if (!isUpdate && (chat.latest == null || chat.latest!!.createdAt < message.createdAt)) {
                    chat.latest = message
                }
            }
        }
    }
}

fun InAppChatStore.onMeEvent(event: MeSubscription.Me) {
    Log.v("Events", "Got Me Event ${event}")
    event.onInviteEvent?.let {
        val chat = Chat.get(it.fInvite.to.fChat)
        val user = User.get(it.fInvite.by.fUser)
        chat.invites.add(user)
        if (!InAppChatStore.current.network.items.contains(chat)) {
            InAppChatStore.current.network.items.add(chat)
        }
    }
    event.onReactionEvent?.let {
        Message.get(it.fReaction.message.fMessage)
    }
    event.onReplyEvent?.let {
        Message.get(it.fReply.message.fMessage)
    }
}