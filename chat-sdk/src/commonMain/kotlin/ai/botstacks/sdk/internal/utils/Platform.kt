package ai.botstacks.sdk.internal.utils

internal object Platform
internal expect val Platform.isAndroid: Boolean
internal expect val Platform.usesCloseAffordanceOnSheets: Boolean

// on iOS, this would be true and on Android/Desktop, false depending on navigation system on device
internal expect val Platform.shouldUseSwipeBack: Boolean