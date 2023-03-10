/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.actions

import io.inappchat.sdk.API
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.User
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op


fun User.block() {
    if (blocking) return
    blocking = true
    blocked = !blocked
    op({
        bg { API.block(id, blocked) }
        Chats.current.settings.setBlock(id, blocked)
        blocking = false
    }) {
        blocked = !blocked
        blocking = false
    }
}