/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Int.localDateTime() = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(toLong()),
    ZoneId.systemDefault()
)

fun String.localDateTime() = LocalDateTime.ofInstant(Instant.parse(this), ZoneId.systemDefault())