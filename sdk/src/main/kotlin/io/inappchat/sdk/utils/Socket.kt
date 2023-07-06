/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import androidx.core.net.toUri
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt3.message.auth.Mqtt3SimpleAuth
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscription
import io.inappchat.sdk.API
import io.inappchat.sdk.API.authToken
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.actions.setNotifications
import io.inappchat.sdk.extensions.contains
import io.inappchat.sdk.infrastructure.Serializer
import io.inappchat.sdk.m
import io.inappchat.sdk.models.*
import io.inappchat.sdk.sha256
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.User
import kotlinx.coroutines.future.await
import java.time.OffsetDateTime

object Socket {

    object Topics {
        val chat = "chat"
        val typing = "typingStatus"
        val read = "msgReadStatus"
        val availability = "availabilityStatus"
    }

    private lateinit var client: Mqtt3AsyncClient
    var server: String? = null
    var apiKey: String? = null


    fun init(server: String, apiKey: String) {
        this.server = server
        this.apiKey = apiKey
        if (User.current?.id == null || authToken == null) return

    }

    val clientId: String
        get() =
            "${User.current?.id ?: ""}:${
                API.deviceId
            }:android"

    fun createClient() {
        val server = this.server
        if (server == null) return

        val username = InAppChat.shared.namespace
        val ts = (System.currentTimeMillis()).toInt()
        val signature = "$apiKey~${bundleUrl()}~$ts".sha256()
        val password = "$signature:$ts:${authToken!!}"
        val uri = server.toUri()
        client =
            Mqtt3Client.builder().identifier(clientId).serverHost(uri.host!!).serverPort(uri.port)
                .simpleAuth(
                    Mqtt3SimpleAuth.builder().username(username)
                        .password(password.encodeToByteArray())
                        .build()
                ).willPublish(
                    Mqtt3Publish.builder().topic("disconnect/clients").payload(
                        clientId
                            .toByteArray()
                    ).qos(MqttQos.EXACTLY_ONCE)
                        .retain(true).build()
                )
                .buildAsync()
    }

    fun connect() {
        async {
            createClient()
            client.connect().await()
            client.subscribe(
                Mqtt3Subscribe.builder().addSubscriptions(
                    subscriptions
                ).build()
            ).await()
            client.publishes(MqttGlobalPublishFilter.ALL, Socket::event)
        }
    }

    suspend fun disconnect() {
        client.disconnect().await()
    }


    fun event(event: Mqtt3Publish) {
        val eventName = event.topic.levels.first()
        val eventType = Event.EventType.valueOf(eventName)
        when (eventType) {
            Event.EventType.availabilityStatus -> {
                val r = Serializer.moshi.adapter(AvailabilityEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.chat -> {
                val r = Serializer.moshi.adapter(NewMessageEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.chatReaction -> {
                val r = Serializer.moshi.adapter(ReactionEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.chatUpdated -> {
                val r = Serializer.moshi.adapter(ChatUpdateEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.msgReadStatus -> {
                val r = Serializer.moshi.adapter(MsgReadEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.typingStatus -> {
                val r = Serializer.moshi.adapter(TypingEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.updateMessage -> {
                val r = Serializer.moshi.adapter(UpdateMessageEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.userSelfUpdate -> {
                val r = Serializer.moshi.adapter(UserSelfUpdateEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.availabilityStatus -> {
                val r = Serializer.moshi.adapter(AvailabilityEvent::class.java)
                    .fromJson(event.payloadAsBytes.decodeToString())
                on(r!!)
            }

            Event.EventType.chatReportUpdated -> {}

        }
    }


    val subscriptions: List<Mqtt3Subscription> = arrayOf(
        "${Topics.chat}:${clientId}",
        "${Topics.typing}:${clientId}",
        "${Topics.availability}:${clientId}",
        "${Topics.read}:${clientId}",
        "chatUpdated:${clientId}",
        "chatReaction:${clientId}",
        "userSelfUpdated:${clientId}",
        "tenantConfigUpdated:${clientId}",
        "chatSettingUpdated:${clientId}",
        "announcement:${clientId}",
        "chatUpdate:${clientId}",
        "userDbUpdated:${clientId}",
        "chatReportUpdated:${clientId}",
    ).map {
        Mqtt3Subscription.builder().topicFilter(it).qos(MqttQos.EXACTLY_ONCE)
            .build()
    }

    fun on(event: AvailabilityEvent) {
        User.get(event.eRTCUserId)?.status = event.availabilityStatus
    }

    fun on(event: NewMessageEvent) {
        val m = event.message.m()
        m.room?.let {
            if (!it.items.contains(m)) {
                it.items.add(0, m)
            }
        }
    }

    fun on(event: ReactionEvent) {
        Message.get(event.msgUniqueId)?.let { msg ->
            val i = msg.reactions.indexOfFirst { it.emojiCode == event.emojiCode }
            if (i > -1) {
                var users = msg.reactions[i].users.toMutableList()
                if (event.action == "set") {
                    if (!users.contains(event.eRTCUserId))
                        users.add(event.eRTCUserId)
                } else {
                    users.remove(event.eRTCUserId)
                }
                msg.reactions[i] = Reaction(event.emojiCode, event.totalCount.toInt(), users)
            } else {
                msg.reactions.add(Reaction(event.emojiCode, 1, listOf(event.eRTCUserId)))
            }
        }
    }

    fun on(event: ChatUpdateEvent) {
        Chat.get(event.chatId)?.let { chat ->
            for (ev in event.eventList) {
                when (ev.eventType) {
                    ChatUpdateEventItem.EventType.adminDismissed,
                    ChatUpdateEventItem.EventType.adminMade -> {
                        ev.eventData.eventTriggeredOnUserList?.let {
                            for (u in it) {
                                val newRole =
                                    if (ev.eventType == ChatUpdateEventItem.EventType.adminMade) Participant.Role.admin else Participant.Role.user
                                val i =
                                    chat.participants.indexOfFirst { it.eRTCUserId == u.eRTCUserId }
                                if (i > -1) {
                                    val p = chat.participants[i].copy(role = newRole)
                                    chat.participants.removeAt(i)
                                    chat.participants.add(i, p)
                                } else {
                                    chat.participants.add(
                                        Participant(
                                            u.appUserId,
                                            u.eRTCUserId,
                                            newRole,
                                            joinedAtDate = OffsetDateTime.now()
                                        )
                                    )
                                }
                            }
                        }
                    }

                    ChatUpdateEventItem.EventType.participantsAdded -> {
                        ev.eventData.eventTriggeredOnUserList?.let {
                            for (u in it) {
                                chat.participants.add(
                                    Participant(
                                        u.appUserId,
                                        u.eRTCUserId, Participant.Role.user, OffsetDateTime.now()
                                    )
                                )
                            }
                        }
                    }

                    ChatUpdateEventItem.EventType.participantsRemoved -> {
                        ev.eventData.eventTriggeredOnUserList?.let {
                            for (u in it) {
                                chat.participants.removeAll { it.eRTCUserId == u.eRTCUserId }
                            }
                        }
                    }

                    ChatUpdateEventItem.EventType.created -> {
                        ev.eventData.changeData?.let { change ->
                            val g = Chat(change.chatId!!.new)
                            g.name = change.name!!.new
                            g.description = change.description?.new
                            g.avatar = change.profilePic?.new
                            g._private =
                                change.chatType?.new == ChatUpdatEventChangeDataChatType.New.private
                        }
                    }

                    ChatUpdateEventItem.EventType.descriptionChanged -> {
                        ev.eventData.changeData?.description?.let {
                            chat.description = it.new
                        }
                    }

                    ChatUpdateEventItem.EventType.descriptionChanged -> {
                        ev.eventData.changeData?.chatType?.let {
                            chat._private =
                                it.new == ChatUpdatEventChangeDataChatType.New.private
                        }
                    }

                    ChatUpdateEventItem.EventType.nameChange -> {
                        ev.eventData.changeData?.name?.let {
                            chat.name = it.new
                        }
                    }

                    ChatUpdateEventItem.EventType.profilePicChanged -> {
                        ev.eventData.changeData?.profilePic?.let {
                            chat.avatar = it.new
                        }
                    }

                    ChatUpdateEventItem.EventType.profilePicRemoved -> {
                        chat.avatar = null
                    }

                    ChatUpdateEventItem.EventType.chatTypeChanged -> {
                        ev.eventData.changeData?.chatType?.let {
                            chat._private =
                                it.new == ChatUpdatEventChangeDataChatType.New.private
                        }
                    }
                }
            }
        }
    }

    fun on(event: MsgReadEvent) {
        Message.get(event.msgUniqueId)?.let {
            it.status = MessageStatus.valueOf(event.msgReadStatus.value)
        }
    }

    fun on(event: TypingEvent) {
        if (event.appUserId == User.current?.email) {
            io.inappchat.sdk.state.Chat.get(event.eRTCUserId)?.let {
                if (event.typingStatusEvent == TypingEvent.TypingStatusEvent.on) {
                    if (!it.typingUsers.contains { it.id == event.eRTCUserId }) {
                        it.typingUsers.add(User.fetched(event.eRTCUserId))
                    } else {
                    }
                } else {
                    it.typingUsers.removeIf { it.id == event.eRTCUserId }
                }
            }
        } else {
            event.chatId?.let {
                io.inappchat.sdk.state.Chat.getByChat(it)?.let {
                    if (event.typingStatusEvent == TypingEvent.TypingStatusEvent.on) {
                        var typing = it.typingUsers
                        if (!typing.contains { it.id == event.eRTCUserId }) {
                            typing.add(User.fetched(event.eRTCUserId))
                        } else {
                        }
                    } else {
                        User.get(event.eRTCUserId)?.let { u ->
                            it.typingUsers.remove(u)
                        }
                    }
                }
            }
        }
    }

    fun on(event: UserSelfUpdateEvent) {
        if (event.eRTCUserId == User.current?.id) {
            for (ev in event.eventList) {
                when (ev.eventType) {
                    SelfUpdateItem.EventType.availabilityStatusChanged ->
                        ev.eventData.availabilityStatus?.let {
                            Chats.current.settings.setAvailability(it, true)
                        }

                    SelfUpdateItem.EventType.notificationSettingChangedGlobal ->
                        ev.eventData.notificationSettings?.let {
                            Chats.current.settings.setNotifications(it.allowFrom, true)
                        }

                    SelfUpdateItem.EventType.notificationSettingsChangedChat ->
                        ev.eventData.notificationSettings?.let { setting ->
                            ev.eventData.chatId?.let { io.inappchat.sdk.state.Chat.get(it) }
                                ?.let { chat ->
                                    chat.setNotifications(setting.allowFrom, true)
                                }
                        }

                    SelfUpdateItem.EventType.userBlockedStatusChanged ->
                        ev.eventData.targetUser?.eRTCUserId?.let {
                            val blocked =
                                ev.eventData.blockedStatus == UserSelfUpdateEventData.BlockedStatus.blocked
                            Chats.current.settings.setBlock(it, blocked)
                            User.get(it)?.let {
                                it.blocked = blocked
                            }
                        }
                }
            }
        }
    }

    fun on(event: UpdateMessageEvent) {
        Message.get(event.msgUniqueId)?.let {
            Chats.current.cache.messages.remove(it.id)
            it.room?.items?.remove(it)
            Chats.current.cache.repliesPagers[event.msgUniqueId]?.let { r ->

                r.items.remove(it)
            }
        }
    }

}