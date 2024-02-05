package ai.botstacks.sdk.utils

import android.text.format.DateUtils
import kotlinx.datetime.Instant

actual fun Instant.relativeTimeString(other: Instant): String {
    return DateUtils.getRelativeTimeSpanString(
        this.toEpochMilliseconds(),
        other.toEpochMilliseconds(),
        0,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}