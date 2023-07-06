/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.inappchat.sdk.fragment.FMessage
import io.inappchat.sdk.state.*
import io.inappchat.sdk.type.AttachmentType
import io.inappchat.sdk.type.ChatType
import io.inappchat.sdk.type.MemberRole
import io.inappchat.sdk.type.OnlineStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import net.datafaker.Faker
import kotlinx.datetime.Instant
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val faker = Faker()

fun genCurrentUser() = User.current ?: genU().let {
    User.current = it
    it
}

fun genU(): User {
    val u = User(uuid())
    u.username = faker.name().username()
    u.avatar = ift(Random.nextBoolean(), randomImage(), null)
    u.status = OnlineStatus.values().random()
    u.statusMessage = ift(Random.nextBoolean(), faker.lorem().sentence(), null)
    u.lastSeen = ift(
        Random.nextBoolean(),
        Clock.System.now()
            .minus(Random.nextLong(10000000).toDuration(DurationUnit.SECONDS)),
        null
    )
    u.displayName = faker.funnyName().name()
    return u
}

fun reqU() {
    if (Chats.current.cache.users.isEmpty()) {
        (0..100).map { genU() }
    }
}

fun reqT() {
    if (Chats.current.cache.chats.isEmpty()) {
        (0..100).map { genChat() }
    }
}

fun randomUsers() = randomAmount(Chats.current.cache.users.values).let {
    ift(
        it.isEmpty(),
        (0..30).map { genU() },
        it
    )
}

fun randomUser() = Chats.current.cache.users.values.randomOrNull() ?: genU()

fun genG(): Chat {
    val g = Chat(uuid(), ChatType.Group)
    g.name = faker.company().name()
    g.description = ift(chance(4, 5), faker.lorem().paragraph(), null)
    g.avatar = ift(Random.nextBoolean(), randomImage(), null)
    g._private = Random.nextBoolean()
    val members = randomUsers().let { ift(it.isEmpty(), random(40, ::genU), it) }
        .map {
            Member(it.id, g.id, Clock.System.now(), MemberRole.values().random())
        }
    if (Random.nextBoolean())
        g.invites.addAll(randomAmount(members).map { it.user })
    return g
}

fun genA(
    kind: AttachmentType = listOf(
        AttachmentType.video,
        AttachmentType.audio,
        AttachmentType.image,
        AttachmentType.file
    ).random()
): FMessage.Attachment {
    var url = when (kind) {
        AttachmentType.video ->
            "https://download.samplelib.com/mp4/sample-5s.mp4"

        AttachmentType.audio ->
            "https://file-examples.com/storage/fe0358100863d05afed02d2/2017/11/file_example_MP3_5MG.mp3"

        else -> randomImage()
    }
    return FMessage.Attachment(uuid(), kind, url, null, null, null, null, null, null, null, null)
}

fun bool() = Random.nextBoolean()

fun genM(
    chat: String = genChat().id,
    parent: String? = null,
    attachments: SnapshotStateList<FMessage.Attachment>? = null,
    user: User = randomUser()
): Message {
    val _attachments =
        attachments ?: ift(bool(), mutableStateListOf(genA()), null)
    val m = Message(
        uuid(), Clock.System.now().minus(Random.nextLong(100000L).seconds),
        user.id,
        parent,
        chat,
        _attachments ?: mutableStateListOf()
    )
    m.updateText(faker.lorem().paragraph())
    if (parent == null && chance(1, 5)) {
        m.replies.items.addAll((0 until Random.nextInt(10)).map { genM(m.chatID, m.id) })
    }
    if (chance(1, 4))
        m.reactions.addAll(
            random(
                10
            ) {
                faker.emoji().smiley() to (Chat.get(chat)?.let { randomAmount(it.members) }
                    ?.map { it.user_id }?.toMutableStateList() ?: mutableStateListOf())
            }
        )
    m.replyCount = m.replies.items.size
    m.favorite = chance(1, 5)
    m.sending = Random.nextBoolean()
    m.chat.addMessage(m)
    m.chat.items.sortByDescending { it.createdAt }
    m.chat.latest = m.chat.items.first()
    return m
}

fun genChat() = if (bool()) genG() else genDM()

fun genImageMessage(user: User = genU()) =
    genM(attachments = mutableStateListOf(genA(AttachmentType.image)), user = user)

fun genFileMessage() = genM(attachments = mutableStateListOf(genA(AttachmentType.file)))

fun genTextMessage(user: User = randomUser(), room: String = genG().id) = Message(
    uuid(), Clock.System.now().minus(Random.nextLong(100000L).seconds),
    user.id,
    null,
    room,
).apply {
    updateText(faker.lorem().paragraph())
    reactions.addAll(
        random(
            4,
            {
                (faker.emoji().smiley() to randomAmount(randomUsers()).map { it.id }
                    .toMutableStateList())
            })
    )
    currentReaction = reactions.firstOrNull()?.first
    replyCount = if (Random.nextBoolean()) Random.nextInt(20) else 0
}

fun genRepliesMessage() = genTextMessage(room = genG().id).apply {
    replies.items.addAll(random(4, { genTextMessage(room = this.chatID) }))
    replyCount = replies.items.size
}


fun genDM() =
    Chat(uuid(), ChatType.DirectMessage).apply {
        val u = genU()
        val c = genCurrentUser()
        members.add(Member(u.id, id, Clock.System.now(), MemberRole.Member))
        members.add(Member(c.id, id, Clock.System.now(), MemberRole.Member))
        items.addAll(
            random(
                10,
                { genTextMessage(room = id, user = u) })
        )
        items.sortByDescending { it.createdAt }
        latest = items.firstOrNull()
        if (!items.isEmpty())
            unreadCount = Random.nextInt(0, items.size)
    }

class SampleUser : PreviewParameterProvider<User> {
    override val values: Sequence<User> = (0..40).map {
        genU()
    }.asSequence()
}

class SampleChat : PreviewParameterProvider<Chat> {
    override val values: Sequence<Chat> = (0..3).map {
        genG()
    }.asSequence()
}

class SampleMessage : PreviewParameterProvider<Message> {
    override val values: Sequence<Message> = sequenceOf(genM())
}

class SampleFn : PreviewParameterProvider<Fn> {
    override val values: Sequence<Fn> = sequenceOf({})

}