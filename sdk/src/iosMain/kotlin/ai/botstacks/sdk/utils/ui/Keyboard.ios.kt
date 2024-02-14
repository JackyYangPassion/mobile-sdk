package ai.botstacks.sdk.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.ObjCAction
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIKeyboardWillHideNotification
import platform.UIKit.UIKeyboardWillShowNotification

@Composable
actual fun keyboardAsState(): State<Boolean> {
    val keyboardState = remember { mutableStateOf(false) }

    DisposableEffect(LocalUIViewController.current) {
        NSNotificationCenter.defaultCenter.addObserver(
            this,
            selector = NSSelectorFromString("keyboardWillDisappear"),
            name = UIKeyboardWillHideNotification,
            `object` = null,
        )
        NSNotificationCenter.defaultCenter.addObserver(
            this,
            selector = NSSelectorFromString("keyboardWillAppear"),
            name = UIKeyboardWillShowNotification,
            `object` = null,
        )

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(this)
        }
    }

    @ObjCAction
    fun keyboardWillDisappear() {
        keyboardState.value = false
    }

    @ObjCAction
    fun keyboardWillAppear() {
        keyboardState.value = true
    }

    return keyboardState
}