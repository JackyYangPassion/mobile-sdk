/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.InAppChat
import io.inappchat.sdk.R
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genCurrentUser

@Composable
fun MyProfile(
  openProfile: () -> Unit,
  openFavorites: () -> Unit,
  openNotificationSettings: () -> Unit,
  back: () -> Unit
) {
  val scrollState = rememberScrollState()
  var logoutDialogue by remember { mutableStateOf(false) }
  Column(
    modifier = Modifier
      .fillMaxSize(1f)
      .verticalScroll(scrollState),
  ) {
    Header(title = "My Profile", back = back)
    Space(16f)
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
      Avatar(url = User.current?.avatar, 150.0)
      Space()
      Text(
        text = User.current?.usernameFb ?: "",
        iac = fonts.body,
        color = colors.text,
        textAlign = TextAlign.Center,
        modifier = Modifier.widthIn(max = 150.dp)
      )
    }
    SimpleRow(icon = R.drawable.user_fill, text = "Profile") {
      openProfile()
    }
    SimpleRow(icon = R.drawable.star_fill, text = "Favorites") {
      openFavorites()
    }
    SimpleRow(icon = R.drawable.bell_simple_fill, text = "Manage Notifications") {
      openNotificationSettings()
    }
    SimpleRow(icon = R.drawable.door_fill, text = "Logout") {
      logoutDialogue = true
    }
    GrowSpacer()
  }
  if (logoutDialogue) {
    AlertDialog(
      onDismissRequest = { logoutDialogue = false },
      buttons = {
        Row(modifier = Modifier.height(44.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { logoutDialogue = false }) {
            Text(text = "No", iac = fonts.headline, color = colors.text)
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { InAppChat.logout() }
          ) {
            Text(text = "Log out", iac = fonts.headline, color = colors.primary)
          }
        }
      },
      title = {
        Text(
          text = "Are you sure you want to logout?",
          iac = fonts.headline,
          color = colors.text
        )
      },
      backgroundColor = colors.background
    )
  }
}

@IPreviews
@Composable
fun MyProfilePreview() {
  genCurrentUser()
  InAppChatContext {
    MyProfile(
      openProfile = { /*TODO*/ },
      openFavorites = { /*TODO*/ },
      openNotificationSettings = { /*TODO*/ }) {

    }
  }
}