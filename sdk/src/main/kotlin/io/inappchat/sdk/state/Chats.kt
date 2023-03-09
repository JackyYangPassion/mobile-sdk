/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import io.inappchat.sdk.API
import java.util.UUID

@Stable
data class Chats(val id: String = UUID.randomUUID().toString()) {
    val cache = Caches()
    val users = mutableListOf<Thread>()
    val groups = mutableListOf<Thread>()
    val blocked = mutableListOf<User>()
    val settings = Settings()

    fun init() {
        API.init()
        settings.init()
    }

    @Stable
    companion object {
        var current = Chats()
    }
}