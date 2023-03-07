/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import androidx.compose.runtime.Stable
import io.inappchat.sdk.state.user.User

@Stable
data class Thread(val id: String, val user: User? = null, val group: Group? = null) {
}