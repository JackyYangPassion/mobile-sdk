package io.inappchat.sdk.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import io.inappchat.sdk.state.Group
import io.inappchat.sdk.state.usernames
import io.inappchat.sdk.ui.theme.IAC

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
