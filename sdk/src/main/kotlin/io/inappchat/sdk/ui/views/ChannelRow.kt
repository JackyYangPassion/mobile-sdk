package io.inappchat.sdk.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.inappchat.sdk.actions.dismissInvites
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.usernames
import io.inappchat.sdk.ui.theme.IAC
import io.inappchat.sdk.ui.theme.IACTheme
import io.inappchat.sdk.utils.annotated

@Composable
fun groupInvitesText(group: Group) =
    buildAnnotatedString {
        pushStyle(
            SpanStyle(
                color = IAC.colors.text,
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
                        Icons.Filled.Close,
                        contentDescription = "Dismiss Invites",
                        modifier = Modifier.size(16),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChannelRow(group: Group) {
    Column(modifier = Modifier.padding(16.dp, 0.dp)) {
        Column(
            modifier = Modifier
                .radius(15)
                .background(IAC.theme.colors.bubble)
        ) {
            InvitesHeader(group = group)
            Row(verticalAlignment = Alignment.CenterVertically) {
                group.avatar?.let {
                    GlideImage(
                        model = it,
                        contentDescription = "${group.name} image",
                        modifier = Modifier
                            .size(87)
                            .radius(15)
                    )
                } ?: GroupPlaceholder(
                    modifier = Modifier
                        .size(87)
                        .radius(15)
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(group.name.annotated(), IAC.theme.fonts.title3, maxLines = 1)
                        PrivacyPill(group._private)
                    }

                }
            }
        }
    }
}