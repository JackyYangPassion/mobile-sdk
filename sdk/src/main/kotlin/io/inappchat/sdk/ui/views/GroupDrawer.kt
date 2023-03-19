/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genG


@Composable
fun GroupDrawerHeader(group: Group) {
  Column {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(64.dp, 24.dp, 64.dp, 24.dp)
    ) {
      Space(24f)
      Avatar(url = group.avatar, 70.0, true)
      Space(12f)
      Text(group.name, fonts.title2, color = colors.text)
      Text(group.description ?: "", fonts.body, color = colors.caption)
      Space(26f)
      Divider(color = colors.text.copy(alpha = 0.1f))
    }
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(start = 16.dp)
    ) {
      Text(text = "All Members", iac = fonts.headline, color = colors.text)
      Space(14f)
      Image(
        painter = painterResource(id = R.drawable.users_three_fill),
        contentDescription = "member count",
        colorFilter = ColorFilter.tint(colors.caption),
        modifier = Modifier.size(16)
      )
      Space()
      Text(group.participants.size.toString(), fonts.caption, color = colors.caption)
    }
  }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun GroupDrawer(
  group: Group?, open: Boolean, hide: () -> Unit, openEdit: (Group) -> Unit,
  openInvite: (Group) -> Unit,
  openProfile: (User) -> Unit,
  back: () -> Unit,
  content: @Composable () -> Unit
) {
  if (group == null) return content()
  val state = rememberModalBottomSheetState(
    ModalBottomSheetValue.Hidden, skipHalfExpanded = true
  )
  LaunchedEffect(key1 = open, block = {
    if (open) {
      state.show()
    } else {
      state.hide()
    }
  })
  ModalBottomSheetLayout(
    sheetState = state,
    sheetBackgroundColor = IAC.colors.background,
    sheetContentColor = IAC.colors.text,
    scrimColor = IAC.colors.caption,
    sheetContent = {
      Box(contentAlignment = Alignment.BottomCenter) {
        Column {
          GroupDrawerHeader(group = group)
          val headers = mapOf(
            "Admins" to group.admins,
            "Online" to group.onlineNotAdminUsers,
            "Offline" to group.offlineNotAdminUsers
          )
          LazyColumn {
            headers.forEach { (name, users) ->
              stickyHeader {
                Text(
                  text = name.uppercase(),
                  iac = fonts.caption.copy(weight = FontWeight.Bold),
                  color = colors.caption,
                  modifier = Modifier.padding(top = 24.dp)
                )
              }
              items(users, { it.id }) {
                ContactRow(user = it, modifier = Modifier.clickable {
                  hide()
                  openProfile(it)
                })
              }
            }
          }
        }
        GroupDrawerButtons(
          group = group,
          openEdit = openEdit,
          openInvite = openInvite,
          dismiss = hide,
          back = back
        )
      }
    },
    content = content
  )
}

@IPreviews
@Composable
fun GroupDrawerPreview() {
  InAppChatContext {
    var open by remember { mutableStateOf(false) }
    GroupDrawer(group = genG(), open, {}, {}, {}, {}, {}) {
      ClickableText(
        text = "hello",
        iac = fonts.body,
        color = colors.text,
        onClick = { open = true })
    }
  }
}