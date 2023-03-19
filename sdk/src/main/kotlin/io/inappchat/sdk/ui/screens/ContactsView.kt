/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.views.*

@Composable
fun ContactsView(scrollToTop: Int, openProfile: (User) -> Unit) {
  val listState = rememberLazyListState()
  Column {
    Header(title = "My Contacts")
    PagerList(pager = Chats.current.contacts, scrollToTop = scrollToTop.toString(), header = {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .background(theme.inverted.softBackground)
          .radius(15.dp)
      ) {
        Text(text = "Find your friends", iac = fonts.title3, color = theme.inverted.text)
        Text(
          "Sync your contacts to easily find people you know. Your contacts will only be to help you connect with friends.",
          iac = fonts.body.copy(weight = FontWeight.Bold),
          color = theme.inverted.text
        )
        Row(modifier = Modifier
          .clickable {
            Chats.current.contacts.requestContacts = false
            Chats.current.contacts.fetchContacts()
          }
          .padding(20.dp, 0.dp)
          .background(colors.background)
          .radius(13.dp)) {
          Text(text = "Sync", iac = fonts.body.copy(weight = FontWeight.Bold), color = colors.text)
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