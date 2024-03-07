package ai.botstacks.sample.kmp.ui

import androidx.compose.runtime.staticCompositionLocalOf

expect class ToastManager {
    fun toast(message: String)
}

val LocalToastManager = staticCompositionLocalOf<ToastManager?> { null }