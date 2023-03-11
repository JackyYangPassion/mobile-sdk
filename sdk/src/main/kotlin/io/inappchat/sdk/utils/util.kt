/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import io.inappchat.sdk.InAppChat
import java.util.UUID

fun bundleUrl() = InAppChat.appContext.packageName
fun uuid() = UUID.randomUUID().toString()

typealias Fn = () -> Unit