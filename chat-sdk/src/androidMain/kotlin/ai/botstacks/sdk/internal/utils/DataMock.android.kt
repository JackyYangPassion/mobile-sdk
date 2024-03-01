package ai.botstacks.sdk.internal.utils

import net.datafaker.Faker

internal actual fun DataMock.companyName(): String = Faker().company().name()
internal actual fun DataMock.emoji(): String = Faker().emoji().toString()
internal actual fun DataMock.funnyName(): String = Faker().funnyName().name()
internal actual fun DataMock.loremParagraph(): String = Faker().lorem().paragraph()
internal actual fun DataMock.loremSentence(): String = Faker().lorem().sentence()
internal actual fun DataMock.name(): String = Faker().name().name()
internal actual fun DataMock.smileyEmoji(): String = Faker().emoji().smiley()
internal actual fun DataMock.username(): String = Faker().name().username()