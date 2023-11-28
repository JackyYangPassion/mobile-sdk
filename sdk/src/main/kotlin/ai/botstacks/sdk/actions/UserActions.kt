/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.actions

import ai.botstacks.sdk.API
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.op


fun User.block() {
    if (blocking) return
    blocking = true
    blocked = !blocked
    op({
        bg { API.block(id, blocked) }
        BotStacksChatStore.current.settings.setBlock(id, blocked)
        blocking = false
    }) {
        blocked = !blocked
        blocking = false
    }
}