/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.inappchat.sdk.models.*
import io.inappchat.sdk.state.*
import net.datafaker.Faker
import java.time.LocalDateTime
import java.time.ZoneOffset

val faker = Faker()


private fun genU(): User {
    val u = User(uuid())
    u.email = faker.internet().emailAddress()
    u.username = faker.name().username()
    u.avatar = ift(faker.random().nextBoolean(), randomImage(), null)
    u.status = AvailabilityStatus.values().random()
    u.statusMessage = ift(faker.random().nextBoolean(), faker.lorem().sentence(), null)
    u.lastSeen = ift(
        faker.random().nextBoolean(),
        LocalDateTime.now().minusSeconds(faker.random().nextLong(10000000)),
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
    if (Chats.current.cache.threads.isEmpty()) {
        (0..100).map { genT() }
    }
}

fun randomUsers() = randomAmount(Chats.current.cache.users.values)
fun randomUser() = Chats.current.cache.users.values.random()

fun genG(): Group {
    val g = Group(uuid())
    g.name = faker.company().name()
    g.description = ift(chance(4, 5), faker.lorem().paragraph(), null)
    g.avatar = ift(faker.random().nextBoolean(), randomImage(), null)
    g._private = faker.random().nextBoolean()
    val members = randomUsers().let { ift(it.isEmpty(), random(40, ::genU), it) }
    g.participants.addAll(members.map {
        Participant(
            it.email,
            it.id,
            ift(faker.random().nextBoolean(), Participant.Role.admin, Participant.Role.user),
            LocalDateTime.now().minusSeconds(faker.random().nextLong(1000000))
                .atOffset(ZoneOffset.UTC)
        )
    })
    if (faker.random().nextBoolean())
        g.invites.addAll(randomAmount(members))
    return g
}

fun genA(): Attachment {
    val kind = AttachmentKind.values().random()
    var url = when (kind) {
        AttachmentKind.video ->
            "https://download.samplelib.com/mp4/sample-5s.mp4"
        AttachmentKind.audio ->
            "https://file-examples.com/storage/fe0358100863d05afed02d2/2017/11/file_example_MP3_5MG.mp3"
        AttachmentKind.image, AttachmentKind.file -> randomImage()
    }
    return Attachment(url, kind)
}

fun bool() = faker.random().nextBoolean()

fun genL() = ift(
    bool(),
    Location(address = faker.address().fullAddress()),
    Location(
        latitude = faker.random().nextInt(-90, 90).toBigDecimal() * faker.random().nextFloat()
            .toBigDecimal(),
        longitude = faker.random().nextInt(-180, 180).toBigDecimal() * faker.random().nextFloat()
            .toBigDecimal()
    )
)

fun genC() = Contact(
    name = faker.funnyName().name(),
    numbers = ift(
        bool(),
        random(
            faker.random().nextInt(5),
            { PhoneNumber(faker.phoneNumber().phoneNumber(), listOf("work", "home").random()) }),
        null
    ),
    emails = ift(
        bool(),
        random(
            faker.random().nextInt(5),
            { Email(faker.internet().emailAddress(), listOf("work", "home").random()) }),
        null
    )
)

fun genM(thread: String = genT().id): Message {
    reqU()
    reqT()
    val m = Message(
        uuid(), LocalDateTime.now().minusSeconds(faker.random().nextLong(100000L)),
        randomUser().id,
        ift(faker.random().nextBoolean(), genM(thread).id, null),
        thread,
        ift(bool(), genA(), null),
        location = ift(bool(), genL(), null),
        contact = ift(bool(), genC(), null)
    )
    m.text = faker.lorem().word()
    if (chance(1, 5)) {
        m.replies.items.addAll((0 until faker.random().nextInt(10)).map { genM(m.id) })
    }
    if (chance(1, 4))
        m.reactions.addAll(
            random(
                10,
                { Reaction(faker.slackEmoji().emoji(), faker.random().nextInt(1, 10), listOf()) })
        )
    m.replyCount = m.replies.items.size
    m.favorite = chance(1, 5)
    return m
}

fun genT(): Room {
    val u = ift(bool(), genU(), null)
    val g = u?.let { null } ?: genG()
    val r = Room(
        uuid(),
        u,
        g
    )
    r.latest = ift(bool(), genM(r.id), null)
    return r
}

class SampleUser : PreviewParameterProvider<User> {
    override val values: Sequence<User> = (0..40).map {
        genU()
    }.asSequence()
}

class SampleGroup : PreviewParameterProvider<Group> {
    override val values: Sequence<Group> = (0..3).map {
        genG()
    }.asSequence()
}

class SampleMessage : PreviewParameterProvider<Message> {
    override val values: Sequence<Message> = (0..40).map { genM() }.asSequence()
}

class SampleFn : PreviewParameterProvider<Fn> {
    override val values: Sequence<Fn> = sequenceOf({})

}