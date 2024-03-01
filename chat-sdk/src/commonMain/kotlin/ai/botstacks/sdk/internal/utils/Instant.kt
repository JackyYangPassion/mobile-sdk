package ai.botstacks.sdk.internal.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime


internal expect fun Instant.relativeTimeString(): String
internal expect fun Instant.relativeTimeString(other: Instant): String

internal fun Instant.minutesBetween(other: Instant): Int = other.minus(this, DateTimeUnit.MINUTE).toInt()
internal fun Instant.hoursBetween(other: Instant): Int = other.minus(this, DateTimeUnit.HOUR).toInt()
internal fun Instant.daysBetween(other: Instant): Int = other.minus(this, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toInt()

internal expect fun Instant.format(format: String): String