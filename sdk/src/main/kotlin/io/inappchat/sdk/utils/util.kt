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
import ezvcard.Ezvcard
import ezvcard.VCard
import ezvcard.property.Email
import ezvcard.property.FormattedName
import ezvcard.property.StructuredName
import ezvcard.property.Telephone
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


fun contactUriToVCard(uri: Uri): VCard {
    Log.v("IAC", "Contact URI $uri")
    val result = Contacts(InAppChat.shared.appContext).query().where {
        Contact.Id.equalToIgnoreCase(uri.lastPathSegment!!)
    }.include {
        listOf(
                Phone.Number,
                Phone.NormalizedNumber,
                Phone.Type,
                Email.Type,
                Email.Address,
                Name.DisplayName,
                Name.FamilyName,
                Name.GivenName,
        )
    }.find()
    return result.map {
        VCard().apply {
            it.displayNamePrimary?.let {
                addFormattedName(FormattedName(it))
            }
            it.displayNameAlt?.let {
                addFormattedNameAlt(FormattedName(it))
            }
            it.nameList().forEach {
                structuredName = StructuredName().apply {
                    given = it.givenName
                    family = it.familyName
                    it.prefix?.let { prefixes.add(it) }
                    it.suffix?.let { suffixes.add(it) }
                }
            }
            it.phones().toList().forEach {
                addTelephoneNumber(Telephone(it.normalizedNumber ?: it.number
                ?: it.primaryValue).apply {
                    it.type?.let {
                        this.addParameter("type", it.name)
                    }
                })
            }
            it.emails().toList().map {
                addEmail(Email(it.address ?: it.primaryValue).apply {
                    it.type?.let {
                        this.addParameter("type", it.name)
                    }
                })
            }
        }
    }.first()
}

fun VCard.attachment(): AttachmentInput {
    return AttachmentInput(
            url = "data",
            type = AttachmentType.vcard,
            data = Optional.present(Ezvcard.write(this).go()),
            id = uuid()
    )
}

fun android.location.Location.attachment(): AttachmentInput {
    return AttachmentInput(
            longitude = Optional.present(this.longitude),
            latitude = Optional.present(this.latitude),
            id = uuid(),
            type = AttachmentType.location,
            url = "data"
    )
}