/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.state

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import contacts.core.*
import contacts.core.util.phones
import io.inappchat.InAppChat.Example.Manifest
import io.inappchat.sdk.API
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.models.Contact
import io.inappchat.sdk.utils.op
import java.util.UUID

@Stable
data class ChannelsPager(val id: String = UUID.randomUUID().toString()) : Pager<Group>() {
    override suspend fun load(isRefresh: Boolean): List<Group> {
        return API.getGroups(skip(isRefresh), pageSize)
    }
}


@Stable
data class ContactsPager(val id: String = UUID.randomUUID().toString()) : Pager<User>() {

    var requestContacts by mutableStateOf(true)
    var contacts = mutableStateListOf<String>()

    fun fetchContacts(): List<String> {
        val contacts =
            Contacts(InAppChat.appContext).query()
                .where { Phone.Number.isNotNullOrEmpty() or Phone.NormalizedNumber.isNotNullOrEmpty() }
                .include {
                    setOf(
                        Phone.Number,
                        Phone.NormalizedNumber
                    )
                }
                .find()
        return contacts.flatMap { it.phones().map { it.normalizedNumber ?: it.number ?: "" } }
    }

    override var isSinglePage = true

    override suspend fun load(isRefresh: Boolean): List<User> {
        val contacts = fetchContacts()
        return API.syncContacts(contacts)
    }

}