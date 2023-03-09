/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap

@Stable
data class Caches(
    val users: SnapshotStateMap<String, User> = mutableStateMapOf(),
    val messages: SnapshotStateMap<String, Message> = mutableStateMapOf(),
    val groups: SnapshotStateMap<String, Group> = mutableStateMapOf(),
    val threads: SnapshotStateMap<String, Thread> = mutableStateMapOf(),
    val threadsByUID: SnapshotStateMap<String, Thread> = mutableStateMapOf(),
    val threadsByGroup: SnapshotStateMap<String, Thread> = mutableStateMapOf(),
    val repliesPagers: SnapshotStateMap<String, RepliesPager> = mutableStateMapOf(),
    val threadFetches: MutableSet<String> = mutableSetOf()
) {
}