/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.actions.invite
import ai.botstacks.sdk.state.BotStacksChatStore
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genG
import ai.botstacks.sdk.utils.genU
import ai.botstacks.sdk.utils.random
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun InviteView(chat: Chat, back: () -> Unit, openChat: (Chat) -> Unit) {
    val selected = remember {
        mutableStateListOf<User>()
    }

    Column {
        Header(title = "Invite to ${chat.name}", onBackClicked = back)
        PagerList(
            pager = BotStacksChatStore.current.contacts,
            divider = true,
            modifier = Modifier.weight(1f)
        ) {
            val isSelected = selected.contains(it)
            ContactRow(user = it, modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxWidth()
                .clickable { if (!isSelected) selected.add(it) else selected.remove(it) }) {
                Box(
                    modifier =
                    Modifier.circle(25.dp, if (isSelected) colorScheme.primary else colorScheme.caption),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.Drawables.Outlined.Check),
                        contentDescription = "Check mark",
                        tint = colorScheme.background,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(20.dp, 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .circle(50.dp, colorScheme.caption)
                    .clickable { back() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.Drawables.Outlined.CaretLeft),
                    contentDescription = "back",
                    tint = colorScheme.background,
                    modifier = Modifier.size(22.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .background(
                        if (selected.isNotEmpty()) colorScheme.primary else colorScheme.caption,
                        RoundedCornerShape(25.dp)
                    )
                    .clickable {
                        if (selected.isNotEmpty() && !chat.inviting) {
                            chat.invite(selected)
                            back()
                            if (CreateChatState.newChats.contains(chat.id)) {
                                CreateChatState.newChats.remove(chat.id)
                                openChat(chat)
                            }
                        }
                    }) {
                Text(text = "Invite Friends", fontStyle = fonts.body2, color = colorScheme.background)
            }
        }
    }
}

@IPreviews
@Composable
fun InvitePreview() {
    BotStacksChatContext {
        BotStacksChatStore.current.contacts.items.addAll(random(20, { genU() }))
        InviteView(chat = genG(), {}) {

        }
    }
}