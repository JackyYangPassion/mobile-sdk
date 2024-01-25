package ai.botstacks.sdk.ui.theme

/*
 * Copyright (c) 2023.
 */

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

val LocalBotStacksDayNightColorScheme = staticCompositionLocalOf {
    DayNightColorScheme(
        lightColors(),
        darkColors()
    )
}

val LocalBotStacksColorScheme = staticCompositionLocalOf { lightColors() }

data class DayNightColorScheme(
    val day: Colors,
    val night: Colors,
) {
    fun colors(isDark: Boolean) = if (isDark) night else day
}

@Stable
class Colors(
    isDark: Boolean,
    bubble: Color,
    bubbleText: Color,
    senderBubble: Color,
    senderText: Color,
    senderUsername: Color,
    text: Color,
    username: Color,
    timestamp: Color,
    primary: Color,
    button: Color,
    background: Color,
    destructive: Color,
    softBackground: Color,
    caption: Color,
    unread: Color,
    _public: Color,
    _private: Color,
    border: Color,
    ripple: Color,
) {
    var isDark by mutableStateOf(isDark, structuralEqualityPolicy())
        internal set
    var bubble by mutableStateOf(bubble, structuralEqualityPolicy())
        internal set
    var bubbleText by mutableStateOf(bubbleText, structuralEqualityPolicy())
        internal set
    var senderBubble by mutableStateOf(senderBubble, structuralEqualityPolicy())
        internal set
    var senderText by mutableStateOf(senderText, structuralEqualityPolicy())
        internal set
    var senderUsername by mutableStateOf(senderUsername, structuralEqualityPolicy())
        internal set
    var text by mutableStateOf(text, structuralEqualityPolicy())
        internal set
    var username by mutableStateOf(username, structuralEqualityPolicy())
        internal set
    var timestamp by mutableStateOf(timestamp, structuralEqualityPolicy())
        internal set
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var button by mutableStateOf(button, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var destructive by mutableStateOf(destructive, structuralEqualityPolicy())
        internal set
    var softBackground by mutableStateOf(softBackground, structuralEqualityPolicy())
        internal set
    var caption by mutableStateOf(caption, structuralEqualityPolicy())
        internal set
    var unread by mutableStateOf(unread, structuralEqualityPolicy())
        internal set
    var _public by mutableStateOf(_public, structuralEqualityPolicy())
        internal set
    var _private by mutableStateOf(_private, structuralEqualityPolicy())
        internal set
    var border by mutableStateOf(border, structuralEqualityPolicy())
        internal set
    var ripple by mutableStateOf(ripple, structuralEqualityPolicy())
        internal set

    /** Returns a copy of this ColorScheme, optionally overriding some of the values. */
    fun copy(
        bubble: Color = this.bubble,
        bubbleText: Color = this.bubbleText,
        senderBubble: Color = this.senderBubble,
        senderText: Color = this.senderText,
        senderUsername: Color = this.senderUsername,
        text: Color = this.text,
        username: Color = this.username,
        timestamp: Color = this.timestamp,
        primary: Color = this.primary,
        button: Color = this.button,
        background: Color = this.background,
        destructive: Color = this.destructive,
        softBackground: Color = this.softBackground,
        caption: Color = this.caption,
        unread: Color = this.unread,
        _public: Color = this._public,
        _private: Color = this._private,
        border: Color = this.border,
        ripple: Color = this.ripple,
    ): Colors = Colors(
        isDark = this.isDark,
        bubble = bubble,
        bubbleText = bubbleText,
        senderBubble = senderBubble,
        senderText = senderText,
        senderUsername = senderUsername,
        text = text,
        username = username,
        timestamp = timestamp,
        primary = primary,
        button = button,
        background = background,
        destructive = destructive,
        softBackground = softBackground,
        caption = caption,
        unread = unread,
        _public = _public,
        _private = _private,
        border = border,
        ripple = ripple
    )
}

fun lightColors(
    bubble: Color = Color(0xFFF0F0F0),
    bubbleText: Color = Color(0xFF1C1C1C),
    senderBubble: Color = Color(0xFFE5ECFF),
    senderText: Color = Color(0xFF202127), // Color(hex: 0xE3E3E3)
    senderUsername: Color = Color(0xFF000000),
    text: Color = BotStacksColorPalette.Dark._900,
    username: Color = Color(0xFF2D3237),
    timestamp: Color = Color(0xFF71869C),
    primary: Color = Color(0xff0091ff),
    button: Color = Color(0xFFF0F0F0),
    background: Color = LightPalette._900,
    destructive: Color = Color(0xFFC74848),
    softBackground: Color = Color(0xFFD4D4D4),
    caption: Color = Color(0x502C2C2C),
    unread: Color = Color(0xFFC74848),
    _public: Color = Color(0xFF4B48C7),
    _private: Color = Color(0xFF488AC7),
    border: Color = Color(0xFF1B1B1B),
    ripple: Color = BotStacksColorPalette.Primary._200,
) = Colors(
    isDark = false,
    bubble = bubble,
    bubbleText = bubbleText,
    senderBubble = senderBubble,
    senderText = senderText,
    senderUsername = senderUsername,
    text = text,
    username = username,
    timestamp = timestamp,
    primary = primary,
    button = button,
    background = background,
    destructive = destructive,
    softBackground = softBackground,
    caption = caption,
    unread = unread,
    _public = _public,
    _private = _private,
    border = border,
    ripple = ripple
)

fun darkColors(
    bubble: Color = Color(0xFF2B2B2B),
    bubbleText: Color = Color(0xFFE3E3E3),
    senderBubble: Color = Color(0xFFE5ECFF),
    senderText: Color = Color(0xFF202127), // Color(hex: 0xE3E3E3)
    senderUsername: Color = Color(0xFFFFFFFF),
    text: Color = BotStacksColorPalette.Light._600,
    username: Color = Color(0xFFE3E3E3),
    timestamp: Color = Color(0x4DE3E3E3),
    primary: Color = Color(0xff0091ff),
    button: Color = Color(0xFF2B2B2B),
    background: Color = DarkPalette._900,
    destructive: Color = Color(0xFFC74848),
    softBackground: Color = Color(0xFF2B2B2B),
    caption: Color = Color(0x50E3E3E3),
    unread: Color = Color(0xFFC74848),
    _public: Color = Color(0xFF4B48C7),
    _private: Color = Color(0xFF488AC7),
    border: Color = Color(0xFFE3E3E3),
    ripple: Color = BotStacksColorPalette.Dark._600
) = Colors(
    isDark = true,
    bubble = bubble,
    bubbleText = bubbleText,
    senderBubble = senderBubble,
    senderText = senderText,
    senderUsername = senderUsername,
    text = text,
    username = username,
    timestamp = timestamp,
    primary = primary,
    button = button,
    background = background,
    destructive = destructive,
    softBackground = softBackground,
    caption = caption,
    unread = unread,
    _public = _public,
    _private = _private,
    border = border,
    ripple = ripple
)