@file:OptIn(ExperimentalForeignApi::class)

package ai.botstacks.sdk.utils

import cocoapods.MBFaker.MBFaker
import cocoapods.MBFaker.MBFakerCompany
import cocoapods.MBFaker.MBFakerLorem
import cocoapods.MBFaker.MBFakerName
import kotlinx.cinterop.ExperimentalForeignApi


actual typealias DataMock = MBFaker

actual fun DataMock.companyName(): String = MBFakerCompany.name().orEmpty()
actual fun DataMock.emoji(): String = ":)"
actual fun DataMock.funnyName(): String = MBFakerName.name().orEmpty()
actual fun DataMock.loremParagraph(): String = MBFakerLorem.paragraph().orEmpty()
actual fun DataMock.loremSentence(): String = MBFakerLorem.sentence().orEmpty()
actual fun DataMock.name(): String = MBFakerName.name().orEmpty()
actual fun DataMock.smileyEmoji(): String = ":)"
actual fun DataMock.username(): String = "user123"