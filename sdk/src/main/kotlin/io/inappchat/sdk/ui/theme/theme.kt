package io.inappchat.sdk.ui.theme
/*
 * Copyright (c) 2023.
 */

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp


val preview = true
val darkMode = false

@Stable
class Theme {
    var fonts by mutableStateOf(Fonts())
    var light: Colors by mutableStateOf(Colors(true))
    var dark: Colors by mutableStateOf(Colors(false))
    var imagePreviewSize by mutableStateOf(Size(width = 89f, height = 76f))
    var videoPreviewSize by mutableStateOf(Size(width = 124f, height = 76f))
    var messageAlignment by mutableStateOf(Alignment.Start)
    var senderAlignment by mutableStateOf(Alignment.End)
    var bubbleRadius by mutableStateOf(7.5f)
    var bubblePadding by mutableStateOf(PaddingValues(6.dp))
    var assets by mutableStateOf(Assets())
    var isDark by mutableStateOf(false)

    @Stable
    val colors: Colors
        get() = if (isDark) dark else light

    @Stable
    val inverted: Colors
        get() = if (isDark) light else dark

    @Stable
    fun with(dark: Boolean): Theme {
        this.isDark = dark
        return this
    }

    fun fromOtherTheme(it: Theme) {
        this.light = it.light
        this.dark = it.dark
        this.imagePreviewSize = it.imagePreviewSize
        this.videoPreviewSize = it.videoPreviewSize
        this.messageAlignment = it.messageAlignment
        this.senderAlignment = it.senderAlignment
        this.bubbleRadius = it.bubbleRadius
        this.bubblePadding = it.bubblePadding
        this.assets = it.assets
    }
}