package ai.botstacks.sdk.internal.utils

import ai.botstacks.sdk.internal.utils.Platform

actual val Platform.isAndroid: Boolean
    get() = false

actual val Platform.usesCloseAffordanceOnSheets: Boolean
    get() = true

actual val Platform.shouldUseSwipeBack: Boolean
    get() = true
