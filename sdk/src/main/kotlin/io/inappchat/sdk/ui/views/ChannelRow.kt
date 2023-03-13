/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.inappchat.sdk.R
import io.inappchat.sdk.actions.dismissInvites
import io.inappchat.sdk.actions.join
import io.inappchat.sdk.actions.leave
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.usernames
import io.inappchat.sdk.ui.theme.IAC
import io.inappchat.sdk.ui.theme.IAC.colors
import io.inappchat.sdk.ui.theme.IAC.theme
import io.inappchat.sdk.ui.theme.IACColors
import io.inappchat.sdk.utils.SampleGroup
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.ift

@Composable
fun groupInvitesText(group: Group) =
    buildAnnotatedString {
        pushStyle(
            SpanStyle(
                color = colors.text,
                fontSize = IAC.fonts.body.size,
                fontWeight = FontWeight.Bold,
                fontFamily = IAC.fonts.body.family
            )
        )
        append(group.invites.usernames())
        pop()
        append(" invited you to join!")
    }

@Composable
fun InvitesHeader(group: Group) {
    if (group.invites.isNotEmpty()) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFC74848),
                            Color(0xFFE34141)
                        ),
                    )
                )
                .padding(7.5.dp)
                .defaultMinSize(minHeight = 40.dp)
        ) {
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(10.dp)) {
                Text(groupInvitesText(group = group), IAC.fonts.body, color = Color.White)
                Space()
                Center(modifier = Modifier
                    .clickable { group.dismissInvites() }
                    .size(30)) {
                    Icon(
                        painter = painterResource(id = R.drawable.x),
                        contentDescription = "Dismiss Invites",
                        modifier = Modifier.size(16),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChannelRow(@PreviewParameter(SampleGroup::class) group: Group) {
    Column(modifier = Modifier.padding(16.dp, 0.dp)) {
        Column(
            modifier = Modifier
                .radius(15)
                .background(theme.colors.bubble)
                .fillMaxWidth()
        ) {
            InvitesHeader(group = group)
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
                    group.avatar?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "${group.name} image",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.fillMaxSize()
                        )
                    } ?: GroupPlaceholder(modifier = Modifier.fillMaxSize())
                }
                Space(8f)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(group.name.annotated(), theme.fonts.title3, maxLines = 1)
                        Space()
                        PrivacyPill(group._private)
                    }
                    Text(
                        text = (group.description ?: "").annotated(),
                        iac = theme.fonts.body,
                        color = colors.caption,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GroupCount(count = group.participants.size)
                        Spacer(modifier = Modifier.weight(1f))
                        if (!group._private || (group.isMember || group.invites.isNotEmpty())) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .size(32.dp)
                                    .background(
                                        if (group.isMember) IACColors.current.softBackground else colors.primary,
                                        CircleShape
                                    )
                                    .clickable(onClick = {
                                        if (group.isMember) {
                                            group.leave()
                                        } else {
                                            group.join()
                                        }
                                    })
                                    .border(0.dp, Color.Transparent, CircleShape)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = "Join group",
                                    modifier = Modifier.fillMaxSize(0.67f),
                                    tint = ift(
                                        group.isMember,
                                        colors.softBackground,
                                        colors.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(
//    showSystemUi = true,
//    showBackground = true
//) // Apparently, adding these two lines seems to do the magic here
//@Composable
//fun ChannelRowPreview() {
//    ChannelRow(group = genG())
//}