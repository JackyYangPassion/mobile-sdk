/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.views.ContactRow
import io.inappchat.sdk.ui.views.Header
import io.inappchat.sdk.ui.views.PagerList
import io.inappchat.sdk.ui.views.Text
import io.inappchat.sdk.ui.views.radius

@Composable
fun ContactsView(scrollToTop: Int, openProfile: (User) -> Unit) {
    val onPermission = { havePermission: Boolean ->
        if (havePermission) {
            InAppChatStore.current.contacts.syncContacts()
        }
        InAppChatStore.current.contacts.requestContacts = false
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        onPermission(it)
    }
    val listState = rememberLazyListState()
    Column {
        Header(title = "My Contacts")
        PagerList(
            pager = InAppChatStore.current.contacts,
            scrollToTop = scrollToTop.toString(),
            header = {
                if (InAppChatStore.current.contacts.requestContacts) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column(
                            modifier =
                            Modifier
                                .radius(15.dp)
                                .background(theme.inverted.softBackground)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Find your friends",
                                iac = fonts.title3,
                                color = theme.inverted.text
                            )
                            Text(
                                "Sync your contacts to easily find people you know. Your contacts will only be to help you connect with friends.",
                                iac = fonts.body.copy(weight = FontWeight.Bold),
                                color = theme.inverted.text
                            )
                            Row(modifier = Modifier
                                .radius(13.dp)
                                .background(colors.background)
                                .clickable {
                                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                }
                                .padding(20.dp, 0.dp)
                            ) {
                                Text(
                                    text = "Sync",
                                    iac = fonts.body.copy(weight = FontWeight.Bold),
                                    color = colors.text
                                )
                            }
                        }
                    }
                }
            }) {
            ContactRow(user = it, modifier = Modifier.clickable { openProfile(it) })
        }
    }
    LaunchedEffect(key1 = scrollToTop, block = {
        if (scrollToTop > 0) {
            listState.animateScrollToItem(0)
        }
    })
}