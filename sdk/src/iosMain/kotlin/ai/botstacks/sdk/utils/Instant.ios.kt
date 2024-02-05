package ai.botstacks.sdk.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSRelativeDateTimeFormatter
import platform.Foundation.NSRelativeDateTimeFormatterUnitsStyleAbbreviated

actual fun Instant.relativeTimeString(other: Instant): String {
    val formatter = NSRelativeDateTimeFormatter().apply {
        dateTimeStyle = NSRelativeDateTimeFormatterUnitsStyleAbbreviated
    }

    return formatter.localizedStringForDate(this.toNSDate(), other.toNSDate())
}