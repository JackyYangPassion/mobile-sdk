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
    InAppChat.scope.launch(context, start, block)
}

private val EmptyFn: () -> Unit = {}

fun op(block: suspend CoroutineScope.() -> Unit, onError: () -> Unit = EmptyFn) = launch {
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

suspend fun <T> bg(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)
