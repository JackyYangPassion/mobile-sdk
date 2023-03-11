/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import io.inappchat.sdk.InAppChat
import java.util.UUID

fun bundleUrl() = InAppChat.appContext.packageName
fun uuid() = UUID.randomUUID().toString()

typealias Fn = () -> Unit


@Composable
fun String.annotated() = AnnotatedString(this)