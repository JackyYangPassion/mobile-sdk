package ai.botstacks.sdk.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus


expect fun Instant.relativeTimeString(other: Instant = Clock.System.now()): String

fun Instant.minutesBetween(other: Instant): Int = other.minus(this, DateTimeUnit.MINUTE).toInt()
fun Instant.hoursBetween(other: Instant): Int = other.minus(this, DateTimeUnit.HOUR).toInt()
fun Instant.daysBetween(other: Instant): Int = other.minus(this, DateTimeUnit.DAY, TimeZone.currentSystemDefault()).toInt()