/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.runtime.*
import io.inappchat.sdk.state.Group
import java.io.File

@Stable
data class CreateGroupState(val id: String = "") {
    var file by mutableStateOf<File?>(null)
    var image: String? = null
    var name by mutableStateOf<String>("")
    var description by mutableStateOf<String?>(null)
    var _private by mutableStateOf(false)

    constructor(group: Group) : this(group.id) {
        this.name = group.name
        this.description = group.description
        this._private = group._private
        this.image = group.avatar
    }

    companion object {
        var current: CreateGroupState? = null
    }
}

@Composable
fun CreateGroup(group: Group?, openChat: (Group) -> Unit, _back: () -> Unit) {
    val back = {
        CreateGroupState.current = null
        _back()
    }
    val state = remember {
        val s = group?.let { CreateGroupState(it) } ?: CreateGroupState()
        CreateGroupState.current = null
        s
    }

}