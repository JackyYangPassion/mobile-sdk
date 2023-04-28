/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import contacts.core.Contacts
import contacts.core.equalToIgnoreCase
import contacts.core.util.emails
import contacts.core.util.nameList
import contacts.core.util.phones
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.models.Contact
import io.inappchat.sdk.models.Email
import io.inappchat.sdk.models.Location
import io.inappchat.sdk.models.PhoneNumber
import java.util.*

fun bundleUrl() = InAppChat.shared.appContext.packageName
fun uuid() = UUID.randomUUID().toString()

typealias Fn = () -> Unit

fun <A> ift(cond: Boolean, a: A, b: A) = if (cond) a else b

@Composable
fun String.annotated() = AnnotatedString(this)

fun contactUriToContact(uri: Uri): Contact {
  Log.v("IAC", "Contact URI ${uri.toString()}")
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
      Name.GivenName
    )
  }.find()
  return result.map {
    Contact(
      it.displayNamePrimary ?: (it.nameList()
        .map { it.displayName ?: (it.givenName ?: "" + it.familyName ?: "") }).joinToString(","),
      it.phones().toList()
        .map { PhoneNumber(it.normalizedNumber ?: it.number ?: "", it.type?.name) },
      it.emails().toList().map { Email(it.address ?: "", it.type?.name ?: "") }
    )
  }.first()
}

fun android.location.Location.toLocation(): Location {
  return Location(this.longitude.toBigDecimal(), this.latitude.toBigDecimal())
}