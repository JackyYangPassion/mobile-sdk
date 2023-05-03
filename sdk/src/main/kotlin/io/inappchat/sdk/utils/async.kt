/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import io.inappchat.sdk.InAppChat
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun launch(
    context: CoroutineContext = Dispatchers.Main,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) {
    InAppChat.shared.scope.launch(context, start, block)
}

private val EmptyFn: () -> Unit = {}

fun op(
    block: suspend CoroutineScope.() -> Unit,
    onError: () -> Unit = EmptyFn,
    context: CoroutineContext = Dispatchers.Main
) = launch(context) {
    try {
        block()
    } catch (err: Exception) {
        Monitoring.error(err)
        onError.invoke()
    } catch (err: Error) {
        Monitoring.error(err)
        onError.invoke()
    }
}

fun op(block: suspend CoroutineScope.() -> Unit, onError: () -> Unit = EmptyFn) =
    op(block, onError, Dispatchers.Main)


suspend fun <T> bg(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)
fun opbg(block: suspend CoroutineScope.() -> Unit, onError: () -> Unit = EmptyFn) =
    op(block, onError, Dispatchers.IO)

fun <T : Unit> async(block: suspend CoroutineScope.() -> T) =
    launch(Dispatchers.IO, block = block)

