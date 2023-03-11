/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.serpro69.kfaker.Faker
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.models.Participant
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.User
import java.time.LocalDateTime
import java.time.ZoneOffset

val faker = Faker()


private fun genU(): User {
    val u = User(uuid())
    u.email = faker.internet.email()
    u.username = faker.random.randomString()
    u.avatar = ift(faker.random.nextBoolean(), randomImage(), null)
    u.status = AvailabilityStatus.values().random()
    u.statusMessage = ift(faker.random.nextBoolean(), faker.lorem.words(), null)
    u.lastSeen = ift(
        faker.random.nextBoolean(),
        LocalDateTime.now().minusSeconds(faker.random.nextLong(10000000)),
        null
    )
    u.displayName = faker.funnyName.name()
    return u
}

private fun genG(): Group {
    if (Chats.current.cache.users.isEmpty()) {
        (0..100).map { genU() }
    }
    val g = Group(uuid())
    g.name = faker.company.name()
    g.description = ift(faker.random.nextBoolean(), faker.lorem.words(), null)
    g.avatar = ift(faker.random.nextBoolean(), randomImage(), null)
    g._private = faker.random.nextBoolean()
    val members = randomAmount(Chats.current.cache.users.values)
    g.participants.addAll(members.map {
        Participant(
            it.email,
            it.id,
            ift(faker.random.nextBoolean(), Participant.Role.admin, Participant.Role.user),
            LocalDateTime.now().minusSeconds(faker.random.nextLong(1000000))
                .atOffset(ZoneOffset.UTC)
        )
    })
    if (faker.random.nextBoolean())
        g.invites.addAll(randomAmount(members))
    return g
}

class SampleUser : PreviewParameterProvider<User> {
    override val values: Sequence<User> = (0..40).map {
        genU()
    }.asSequence()
}

class SampleGroup : PreviewParameterProvider<Group> {
    override val values: Sequence<Group> = (0..20).map {
        genG()
    }.asSequence()
}

clas