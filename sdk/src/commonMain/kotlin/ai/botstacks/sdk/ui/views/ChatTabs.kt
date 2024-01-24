/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.utils.IPreviews

@Composable
private fun Tab(
    tab: BotStacksChatStore.List,
    selected: Boolean,
    unreadCount: Int,
    modifier: Modifier = Modifier,
    onPress: (BotStacksChatStore.List) -> Unit
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
                    color = if (selected) colorScheme.primary else colorScheme.caption,
                )
                Badge(count = unreadCount, modifier = Modifier.offset(10.dp, -5.dp))
            }
            if (selected) {
                Spacer(
                    modifier = Modifier
                        .background(colorScheme.primary, CircleShape)
                        .height(4.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ChatTabs(list: BotStacksChatStore.List, onSelect: (BotStacksChatStore.List) -> Unit) {
    Row(modifier = Modifier.height(44.dp)) {
        for (tab in BotStacksChatStore.List.values()) {
            Tab(
                tab = tab,
                selected = list == tab,
                unreadCount = BotStacksChatStore.current.count(tab),
                modifier = Modifier.weight(1f),
                onPress = onSelect
            )
        }
    }
}

@IPreviews
@Composable
fun ChatTabsPreview() {
    BotStacksChatContext {
        Column {
            ChatTabs(list = BotStacksChatStore.List.dms, onSelect = {})
            ChatTabs(list = BotStacksChatStore.List.groups, onSelect = {})
        }
    }
}
