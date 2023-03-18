/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews

@Composable
private fun Tab(
  tab: Chats.List,
  selected: Boolean,
  unreadCount: Int,
  modifier: Modifier = Modifier,
  onPress: (Chats.List) -> Unit
) {
  Row(modifier = modifier.clickable { onPress(tab) }, horizontalArrangement = Arrangement.Center) {
    if (tab == Chats.List.groups) {
      Spacer(modifier = Modifier.weight(1f))
    }
    Column(
      verticalArrangement = Arrangement.spacedBy(2.dp),
      modifier = Modifier.width(IntrinsicSize.Max)
    ) {
      Text(
        tab.label,
        fonts.title2.copy(weight = FontWeight.Bold),
        color = if (selected) colors.primary else colors.caption,
      )
      if (selected) {
        Spacer(
          modifier = Modifier
            .background(colors.primary, CircleShape)
            .height(4.dp)
            .fillMaxWidth()
        )
      }
      if (unreadCount > 0) {
        Box(
          modifier = Modifier
            .width(0.dp)
            .height(20.dp)
        ) {
          Badge(count = unreadCount)
        }
      }
    }
    if (tab == Chats.List.threads) {
      Spacer(modifier = Modifier.weight(1f))
    }
  }
}

@Composable
fun ChatTabs(list: Chats.List, onSelect: (Chats.List) -> Unit) {
  Row(modifier = Modifier.height(44.dp)) {
    for (tab in Chats.List.values()) {
      Tab(
        tab = tab,
        selected = list == tab,
        unreadCount = Chats.current.count(tab),
        modifier = Modifier.weight(1f),
        onPress = onSelect
      )
    }
  }
}

@IPreviews
@Composable
fun ChatTabsPreview() {
  InAppChatContext {
    Column {
      ChatTabs(list = Chats.List.users, onSelect = {})
      ChatTabs(list = Chats.List.groups, onSelect = {})
      ChatTabs(list = Chats.List.threads, onSelect = {})
    }
  }
}
