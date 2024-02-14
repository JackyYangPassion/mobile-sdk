/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ai.botstacks.sdk.actions.react
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.User
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.BotStacks.fonts
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.internal.Pressable
import ai.botstacks.sdk.utils.*
import kmp_template.sdk.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageView(
    message: Message,
    onPressUser: (User) -> Unit,
    onLongPress: (Message) -> Unit,
    onClick: ((Message) -> Unit)? = null
) {
    if (message.user.blocked) {
        return
    }
    val current = message.user.isCurrent
    val align = if (current) Alignment.End else Alignment.Start

    Column(
        horizontalAlignment = align,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onClick?.invoke(message)
                },
                onLongClick = {
                    onLongPress(message)
                })
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(10.dp, 5.dp)
                .fillMaxWidth(0.8f)
        ) {
            if (align == Alignment.Start) {
                Favorite(message.favorite)
                Avvy(message.user.avatar) { onPressUser(message.user) }
                Content(msg = message, modifier = Modifier.weight(1f))
            } else {
                Content(msg = message, modifier = Modifier.weight(1f))
                Avvy(message.user.avatar) { onPressUser(message.user) }
                Favorite(message.favorite)
            }
        }
    }
}

@IPreviews
@Composable
fun MessageViewPreview() {
    BotStacksChatContext {
        Column {
            MessageView(message = genChatextMessage(genU()), onPressUser = {}, onLongPress = {})
            MessageView(
                message = genChatextMessage(genCurrentUser()),
                onPressUser = {},
                onLongPress = {})
        }
    }
}

@Composable
fun ReplyCount(count: Int) {
    if (count > 0) {
        Text(
            text = "$count replies",
            fontStyle = fonts.body1,
            color = colorScheme.primary,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Favorite(favorite: Boolean) {
    if (favorite) {
        Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(Res.drawable.star),
                contentDescription = "favorite",
                tint = colorScheme.primary,
                modifier = Modifier.size(20),
            )
        }
    }
}

@Composable
fun Spinner(loading: Boolean) {
    if (loading) {
        CircularProgressIndicator(color = colorScheme.primary, modifier = Modifier.size(20))
    }
}

@Composable
fun Avvy(url: String?, onClick: Fn) {
    Pressable(onClick = onClick) {
        println("User avatar $url")
        Avatar(url = url)
    }
}

@Composable
fun Reactions(msg: Message, modifier: Modifier = Modifier) {
    for (reactions in msg.reactions.chunked(5)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
            for (reaction in reactions) {
                ClickableText(
                    text = "${reaction.first} ${
                        reaction.second
                            .size
                    }",
                    onClick = { msg.react(reaction.first) },
                    iac = fonts.body1,
                    color = colorScheme.onBackground,
                    modifier = Modifier
                        .radius(36.dp)
                        .background(Color.Transparent)
                        .border(
                            2.dp,
                            if (msg.currentReaction == reaction.first) colorScheme.primary else Color.Transparent,
                            RoundedCornerShape(36.dp)
                        )
                        .padding(dimens.grid.x2)
                )
            }
        }
    }
}

@Composable
fun Content(msg: Message, modifier: Modifier = Modifier) {
    val align = if (msg.user.isCurrent) Alignment.End else Alignment.Start
    Column(
        horizontalAlignment = align, verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        MessageTop(msg)
        MessageContent(message = msg)
        Reactions(msg)
        ReplyCount(count = msg.replyCount)
    }
}

@Composable
fun MessageTop(msg: Message) {
    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = msg.user.username,
            fontStyle = fonts.body1,
            color = ift(msg.user.isCurrent, colorScheme.primary, colorScheme.onMessage),
            modifier = Modifier
                .requiredSizeIn(maxWidth = 120.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(text = msg.createdAt.relativeTimeString(), fontStyle = fonts.caption2, color = colorScheme.caption)
    }
}