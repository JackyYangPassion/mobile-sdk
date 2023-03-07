/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import io.inappchat.sdk.state.user.User

@Stable
data class Caches(
    val users: MutableMap<String, User> = mutableMapOf(),
    val messages: MutableMap<String, Message> = mutableMapOf(),
    val groups: MutableMap<String, Group> = mutableMapOf(),
    val threads: MutableMap<String, Thread> = mutableMapOf(),
    val threadsByUID: MutableMap<String, Thread> = mutableMapOf(),
    val threadsByGroup: MutableMap<String, Thread> = mutableMapOf()
) {
}