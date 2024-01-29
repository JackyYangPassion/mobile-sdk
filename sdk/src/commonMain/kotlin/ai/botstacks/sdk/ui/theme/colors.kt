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
    primary: Color,
    onPrimary: Color,
    header: Color,
    onHeader: Color,
    background: Color,
    onBackground: Color,
    surface: Color,
    onSurface: Color,
    onSurfaceVariant: Color,
    border: Color,
    message: Color,
    onMessage: Color,
    chatInput: Color,
    onChatInput: Color,
    caption: Color,
    success: Color,
    onSuccess: Color,
    error: Color,
    onError: Color,
    ripple: Color,
) {
    var isDark by mutableStateOf(isDark, structuralEqualityPolicy())
        internal set
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var header by mutableStateOf(header, structuralEqualityPolicy())
        internal set
    var onHeader by mutableStateOf(onHeader, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        internal set
    var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
        internal set
    var onSurfaceVariant by mutableStateOf(onSurfaceVariant, structuralEqualityPolicy())
        internal set
    var border by mutableStateOf(border, structuralEqualityPolicy())
        internal set
    var message by mutableStateOf(message, structuralEqualityPolicy())
        internal set
    var onMessage by mutableStateOf(onMessage, structuralEqualityPolicy())
        internal set
    var chatInput by mutableStateOf(chatInput, structuralEqualityPolicy())
        internal set
    var onChatInput by mutableStateOf(onChatInput, structuralEqualityPolicy())
        internal set
    var caption by mutableStateOf(caption, structuralEqualityPolicy())
        internal set
    var success by mutableStateOf(success, structuralEqualityPolicy())
        internal set
    var onSuccess by mutableStateOf(onSuccess, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var onError by mutableStateOf(onError, structuralEqualityPolicy())
        internal set

    var ripple by mutableStateOf(ripple, structuralEqualityPolicy())
        internal set

    /** Returns a copy of this ColorScheme, optionally overriding some of the values. */
    fun copy(
        primary: Color = this.primary,
        onPrimary: Color = this.onPrimary,
        header: Color = this.header,
        onHeader: Color = this.onHeader,
        background: Color = this.background,
        onBackground: Color = this.onBackground,
        surface: Color = this.surface,
        onSurface: Color = this.onSurface,
        onSurfaceVariant: Color = this.onSurfaceVariant,
        border: Color = this.border,
        message: Color = this.message,
        onMessage: Color = this.onMessage,
        chatInput: Color = this.chatInput,
        onChatInput: Color = this.onChatInput,
        caption: Color = this.caption,
        success: Color = this.success,
        onSuccess: Color = this.onSuccess,
        error: Color = this.error,
        onError: Color = this.onError,
        ripple: Color = this.ripple,
    ): Colors = Colors(
        isDark = this.isDark,
        primary = primary,
        onPrimary = onPrimary,
        header = header,
        onHeader = onHeader,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        onSurfaceVariant = onSurfaceVariant,
        border = border,
        message = message,
        onMessage = onMessage,
        chatInput = chatInput,
        onChatInput = onChatInput,
        caption = caption,
        success = success,
        onSuccess = onSuccess,
        error = error,
        onError = onError,
        ripple = ripple,
    )
}

fun lightColors(
    primary: Color = BotStacksColorPalette.Primary._800,
    onPrimary: Color = BotStacksColorPalette.Light._900,
    header: Color = BotStacksColorPalette.Primary._100,
    onHeader: Color = BotStacksColorPalette.Dark._900,
    background: Color = BotStacksColorPalette.Light._900,
    onBackground: Color = BotStacksColorPalette.Dark._600,
    surface: Color = BotStacksColorPalette.Light._500,
    onSurface: Color = BotStacksColorPalette.Dark._900,
    onSurfaceVariant: Color = BotStacksColorPalette.Light._100,
    border: Color = BotStacksColorPalette.Light._500,
    message: Color = BotStacksColorPalette.Light._700,
    onMessage: Color = BotStacksColorPalette.Dark._900,
    chatInput: Color = BotStacksColorPalette.Light._600,
    onChatInput: Color = BotStacksColorPalette.Dark._900,
    caption: Color = BotStacksColorPalette.Dark._100,
    success: Color = BotStacksColorPalette.Green._800,
    onSuccess: Color = BotStacksColorPalette.Dark._400,
    error: Color = BotStacksColorPalette.Red._800,
    onError: Color = Color(0xFF202023),
    ripple: Color = BotStacksColorPalette.Dark._100,
) = Colors(
    isDark = false,
    primary = primary,
    onPrimary = onPrimary,
    header = header,
    onHeader = onHeader,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    border = border,
    message = message,
    onMessage = onMessage,
    chatInput = chatInput,
    onChatInput = onChatInput,
    caption = caption,
    success = success,
    onSuccess = onSuccess,
    error = error,
    onError = onError,
    ripple = ripple
)

fun darkColors(
    primary: Color = BotStacksColorPalette.Primary._700,
    onPrimary: Color = BotStacksColorPalette.Light._900,
    header: Color = BotStacksColorPalette.Dark._900,
    onHeader: Color = BotStacksColorPalette.Light._900,
    background: Color = BotStacksColorPalette.Dark._800,
    onBackground: Color = BotStacksColorPalette.Light._600,
    surface: Color = BotStacksColorPalette.Dark._700,
    onSurface: Color = BotStacksColorPalette.Light._900,
    onSurfaceVariant: Color = BotStacksColorPalette.Dark._100,
    border: Color = BotStacksColorPalette.Dark._400,
    message: Color = BotStacksColorPalette.Dark._500,
    onMessage: Color = BotStacksColorPalette.Light._900,
    chatInput: Color = BotStacksColorPalette.Dark._500,
    onChatInput: Color = BotStacksColorPalette.Light._600,
    caption: Color = BotStacksColorPalette.Dark._100,
    success: Color = BotStacksColorPalette.Green._700,
    onSuccess: Color = BotStacksColorPalette.Dark._400,
    error: Color = BotStacksColorPalette.Red._700,
    onError: Color = Color(0xFF29292D),
    ripple: Color = BotStacksColorPalette.Light._500,
) = Colors(
    isDark = true,
    primary = primary,
    onPrimary = onPrimary,
    header = header,
    onHeader = onHeader,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    onSurfaceVariant = onSurfaceVariant,
    border = border,
    message = message,
    onMessage = onMessage,
    chatInput = chatInput,
    onChatInput = onChatInput,
    caption = caption,
    success = success,
    onSuccess = onSuccess,
    error = error,
    onError = onError,
    ripple = ripple
)