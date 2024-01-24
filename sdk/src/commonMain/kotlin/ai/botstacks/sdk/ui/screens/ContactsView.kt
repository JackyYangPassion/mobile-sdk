/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

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
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacks.theme
import ai.botstacks.sdk.ui.views.ContactRow
import ai.botstacks.sdk.ui.views.Header
import ai.botstacks.sdk.ui.views.PagerList
import ai.botstacks.sdk.ui.views.Text
import ai.botstacks.sdk.ui.views.radius

@Composable
fun ContactsView(scrollToTop: Int, openProfile: (User) -> Unit) {
    val onPermission = { havePermission: Boolean ->
        if (havePermission) {
            BotStacksChatStore.current.contacts.syncContacts()
        }
        BotStacksChatStore.current.contacts.requestContacts = false
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
            pager = BotStacksChatStore.current.contacts,
            scrollToTop = scrollToTop.toString(),
            header = {
                if (BotStacksChatStore.current.contacts.requestContacts) {
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
                                .background(colorScheme.background)
                                .clickable {
                                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                }
                                .padding(20.dp, 0.dp)
                            ) {
                                Text(
                                    text = "Sync",
                                    iac = fonts.body.copy(weight = FontWeight.Bold),
                                    color = colorScheme.text
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