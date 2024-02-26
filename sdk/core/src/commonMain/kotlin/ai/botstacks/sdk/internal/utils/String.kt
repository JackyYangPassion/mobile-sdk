package ai.botstacks.sdk.internal.utils

import kotlinx.datetime.Instant

internal fun String.instant() = Instant.parse(this)