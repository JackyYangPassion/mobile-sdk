/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ai.botstacks.sdk.actions.dismissInvites
import ai.botstacks.sdk.actions.join
import ai.botstacks.sdk.actions.leave
import ai.botstacks.sdk.state.Chat
import ai.botstacks.sdk.state.usernames
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Drawables
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.annotated
import ai.botstacks.sdk.utils.genG

@Composable
fun chatInvitesText(chat: Chat) =
    buildAnnotatedString {
        pushStyle(
            SpanStyle(
                color = Color.White,
                fontSize = BotStacks.fonts.body1.size,
                fontWeight = FontWeight.Bold,
                fontFamily = BotStacks.fonts.body1.family
            )
        )
        append(chat.invites.usernames())
        pop()
        append(" invited you to join!")
    }

@Composable
fun InvitesHeader(chat: Chat) {
    if (chat.invites.isNotEmpty()) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFC74848),
                            Color(0xFFE34141)
                        ),
                    )
                )
                .padding(10.dp)
                .defaultMinSize(minHeight = 40.dp)
                .fillMaxWidth()
        ) {
            Text(
                chatInvitesText(chat = chat),
                BotStacks.fonts.body1,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier
                .clickable { chat.dismissInvites() }
                .requiredSize(30.dp)
                .size(30.dp), contentAlignment = Alignment.Center) {
                Icon(
                    painter = Drawables.X,
                    contentDescription = "Dismiss Invites",
                    modifier = Modifier.size(16),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChannelRow(chat: Chat, onClick: (Chat) -> Unit) {
    Column(modifier = Modifier
        .padding(16.dp, 6.dp)
        .clickable { onClick(chat) }) {
        Column(
            modifier = Modifier
                .radius(15)
                .background(colorScheme.bubble)
                .fillMaxWidth()
        ) {
            InvitesHeader(chat = chat)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(103.dp)
                    .padding(8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(87)
                        .radius(15)
                ) {
                    chat.image?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "${chat.name} image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: ChatPlaceholder(modifier = Modifier.fillMaxSize())
                }
                Space(8f)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val configuration = LocalConfiguration.current
                        val screenWidth = configuration.screenWidthDp
                        Text(
                            chat.displayName.annotated(),
                            BotStacks.fonts.h3,
                            color = colorScheme.text,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.heightIn(0.dp, (screenWidth * 0.5).dp)
                        )
                        Space()
                        PrivacyPill(chat._private)
                    }
                    Text(
                        text = (chat.description ?: "").annotated(),
                        iac = BotStacks.fonts.body1,
                        color = colorScheme.caption,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChatCount(count = chat.members.size)
                        Spacer(modifier = Modifier.weight(1f))
                        if (!chat._private || (chat.isMember || chat.invites.isNotEmpty())) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .size(32.dp)
                                    .background(
                                        if (chat.isMember) colorScheme.softBackground else colorScheme.primary,
                                        CircleShape
                                    )
                                    .clickable(onClick = {
                                        if (chat.isMember) {
                                            chat.leave()
                                        } else {
                                            chat.join()
                                        }
                                    })
                                    .border(0.dp, Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    painter = Drawables.Plus,
                                    contentDescription = "Join chat",
                                    modifier = Modifier.fillMaxSize(0.67f),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@IPreviews
@Composable
fun ChannelRowPreview() {
    BotStacksChatContext {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ChannelRow(chat = genG(), onClick = {})
            ChannelRow(chat = genG(), onClick = {})
        }
    }
}