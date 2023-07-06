/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.apollographql.apollo3.api.Optional
import contacts.core.Contacts
import contacts.core.equalToIgnoreCase
import contacts.core.util.emails
import contacts.core.util.nameList
import contacts.core.util.phones
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.type.AttachmentInput
import io.inappchat.sdk.type.AttachmentType
import java.util.*

fun bundleUrl() = InAppChat.shared.appContext.packageName
fun uuid() = UUID.randomUUID().toString()

typealias Fn = () -> Unit

fun <A> ift(cond: Boolean, a: A, b: A) = if (cond) a else b

@Composable
fun String.annotated() = AnnotatedString(this)


fun android.location.Location.attachment(): AttachmentInput {
    return AttachmentInput(
        longitude = Optional.present(this.longitude),
        latitude = Optional.present(this.latitude),
        id = uuid(),
        type = AttachmentType.location,
        url = "data"
    )
}