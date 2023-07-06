/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.actions.invite
import io.inappchat.sdk.state.Chats
import io.inappchat.sdk.state.Chat
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.views.*
import io.inappchat.sdk.utils.IPreviews
import io.inappchat.sdk.utils.genG
import io.inappchat.sdk.utils.genU
import io.inappchat.sdk.utils.random

@Composable
fun InviteView(chat: Chat, back: () -> Unit) {
    val selected = remember {
        mutableStateListOf<User>()
    }

    Column {
        Header(title = "Invite to ${chat.name}", back = back)
        PagerList(pager = Chats.current.contacts, divider = true, modifier = Modifier.weight(1f)) {
            val isSelected = selected.contains(it)
            ContactRow(user = it, modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxWidth()
                .clickable { if (!isSelected) selected.add(it) else selected.remove(it) }) {
                Box(
                    modifier =
                    Modifier.circle(25.dp, if (isSelected) colors.primary else colors.caption),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = io.inappchat.sdk.R.drawable.check),
                        contentDescription = "Check mark",
                        tint = colors.background,
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
                    .circle(50.dp, colors.caption)
                    .clickable { back() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = io.inappchat.sdk.R.drawable.caret_left),
                    contentDescription = "back",
                    tint = colors.background,
                    modifier = Modifier.size(22.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .background(
                        if (selected.isNotEmpty()) colors.primary else colors.caption,
                        RoundedCornerShape(25.dp)
                    )
                    .clickable {
                        if (selected.isNotEmpty() && !chat.inviting) {
                            chat.invite(selected)
                            back()
                        }
                    }) {
                Text(text = "Invite Friends", iac = fonts.headline, color = colors.background)
            }
        }
    }
}

@IPreviews
@Composable
fun InvitePreview() {
    InAppChatContext {
        Chats.current.contacts.items.addAll(random(20, { genU() }))
        InviteView(chat = genG()) {

        }
    }
}