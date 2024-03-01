/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.internal.actions

import ai.botstacks.sdk.internal.API
import ai.botstacks.sdk.internal.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.internal.utils.bg
import ai.botstacks.sdk.internal.utils.op


internal fun User.toggleBlock() {
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

internal fun User.toggleMute() {
    if (togglingMute) return
    togglingMute = true
    muted = !muted
    op({
        bg { API.mute(id, muted) }
        BotStacksChatStore.current.settings.setMuted(id, muted)
        togglingMute = false
    }) {
        muted = !muted
        togglingMute = false
    }
}