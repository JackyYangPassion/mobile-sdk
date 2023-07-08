package io.inappchat.sdk.state

import io.inappchat.sdk.CoreSubscription
import io.inappchat.sdk.MeSubscription
import io.inappchat.sdk.fragment.FChat
import io.inappchat.sdk.fragment.FMember
import io.inappchat.sdk.fragment.FMessage
import io.inappchat.sdk.fragment.FUser

fun Chats.onCreateMessage(it: FMessage) {
    val chat = Chat.get(it.chat_id)
    chat?.let { chat ->
        chat.addMessage(Message.get(it))
    }
}

fun Chats.onCreateMember(it: FMember) {
    val chat = Chat.get(it.chat_id) ?: return

}

fun Chats.onCreateUser(it: FUser) {

}

fun Chats.onCreateChat(it: FChat) {

}

fun Chats.onCoreEvent(event: CoreSubscription.Core) {
    event.onDeleteEvent?.let {

    }
    event.onEntityEvent?.let {
        it.entity.onChat?.let {

        }
    }
}

fun Chats.onMeEvent(event: MeSubscription.Me) {

}