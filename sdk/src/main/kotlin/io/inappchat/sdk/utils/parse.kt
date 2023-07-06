/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import android.text.format.DateUtils
import kotlinx.datetime.Instant

fun Int.instant() =
    Instant.ofEpochMilli(toLong())

fun String.instant() = Instant.parse(this)

fun Instant.timeAgo() =
    DateUtils.getRelativeTimeSpanString(
        this.toEpochMilli(),
        System.currentTimeMillis(),
        0,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()