/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.actions.block
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.type.OnlineStatus
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.ui.views.*
import ai.botstacks.sdk.utils.*
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileView(user: User, back: () -> Unit, openChat: (User) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
            modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize(1f)
    ) {
        Header(title = user.username, icon = { Avatar(url = user.avatar) }, onBackClick = back)
        Space(16f)
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
        ) {
            Avatar(url = user.avatar,  size = AvatarSize.Large,)
            Space()
            Text(text = user.displayNameFb, fontStyle = fonts.body2, color = colorScheme.onBackground)
            Box(
                    modifier = Modifier
                            .padding(12.dp, 5.dp)
                            .radius(30.dp)
                            .background(colorScheme.surface),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = if (user.status == OnlineStatus.Online) "Online" else user.lastSeen?.timeAgo()
                                ?: "",
                        fontStyle = fonts.body1,
                        color = if (user.status == OnlineStatus.Online) colorScheme.primary else colorScheme.caption
                )
            }

            if (!user.isCurrent) {
                Divider(color = colorScheme.border)
                SimpleRow(
                        icon = Res.Drawables.Filled.PaperPlaneTilt,
                        text = "Send a Chat",
                        iconPrimary = true
                ) {
                    openChat(user)
                }
            }
//            Row(
//                    Modifier
//                            .padding(top = 8.dp, start = 8.dp)
//                            .fillMaxWidth()
//            ) {
//                Text("Shared Media", iac = fonts.body, color = colors.text)
//            }
//            val listState = rememberLazyListState()
//            LazyRow(
//                    state = listState, modifier = Modifier
//                    .padding(start = 16.dp, top = 8.dp)
//                    .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
////                itemsIndexed(
////                        user.sharedMedia.items,
////                        key = { index, item -> item.id }) { index, item ->
////                    MessageContent(message = item)
////                }
//            }
            Space(24f)
            Divider(color = colorScheme.caption)
            if (!user.isCurrent) {
                SimpleRow(
                        icon = if (user.blocked) Res.Drawables.Filled.LockSimpleOpen else Res.Drawables.Filled.Lock,
                        text = if (user.blocked) "Unblock ${user.username}" else "Block ${user.username}",
                        iconPrimary = user.blocked,
                        destructive = !user.blocked
                ) {
                    user.block()
                }
            }
            GrowSpacer()
        }
    }
}

@IPreviews
@Composable
fun ProfileViewPreview() {
    val u = genU()
    val ms = random(10, { genImageMessage(user = u) })
//    u.sharedMedia.items.addAll(ms)
    BotStacksChatContext {
        ProfileView(user = u, back = { /*TODO*/ }, openChat = {})
    }
}