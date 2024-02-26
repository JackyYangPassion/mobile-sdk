package ai.botstacks.sdk.internal.utils

internal actual val Platform.isAndroid: Boolean
    get() = false

internal actual val Platform.usesCloseAffordanceOnSheets: Boolean
    get() = true

internal actual val Platform.shouldUseSwipeBack: Boolean
    get() = true
