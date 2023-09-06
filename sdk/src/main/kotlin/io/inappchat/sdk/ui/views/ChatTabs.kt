/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.state.InAppChatStore
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.utils.IPreviews

@Composable
private fun Tab(
    tab: InAppChatStore.List,
    selected: Boolean,
    unreadCount: Int,
    modifier: Modifier = Modifier,
    onPress: (InAppChatStore.List) -> Unit
) {
    Row(
        modifier = modifier.clickable { onPress(tab) },
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f, true)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GrowSpacer()
            Box(contentAlignment = Alignment.TopEnd) {
                Text(
                    tab.label,
                    fonts.title2.copy(weight = FontWeight.Bold),
                    color = if (selected) colors.primary else colors.caption,
                )
                Badge(count = unreadCount, modifier = Modifier.offset(10.dp, -5.dp))
            }
            if (selected) {
                Spacer(
                    modifier = Modifier
                        .background(colors.primary, CircleShape)
                        .height(4.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ChatTabs(list: InAppChatStore.List, onSelect: (InAppChatStore.List) -> Unit) {
    Row(modifier = Modifier.height(44.dp)) {
        for (tab in InAppChatStore.List.values()) {
            Tab(
                tab = tab,
                selected = list == tab,
                unreadCount = InAppChatStore.current.count(list),
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
            ChatTabs(list = InAppChatStore.List.dms, onSelect = {})
            ChatTabs(list = InAppChatStore.List.groups, onSelect = {})
        }
    }
}
