package ai.botstacks.sdk.internal.utils

import androidx.compose.runtime.snapshots.SnapshotStateList

internal fun <T> SnapshotStateList<T>.removeIf(predicate: (T) -> Boolean) {
    val removals = filter { predicate(it) }
    removeAll(removals)
}