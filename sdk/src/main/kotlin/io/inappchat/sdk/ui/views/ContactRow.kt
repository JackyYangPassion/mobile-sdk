/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.inappchat.sdk.R
import io.inappchat.sdk.models.AvailabilityStatus
import io.inappchat.sdk.state.User
import io.inappchat.sdk.ui.theme.IAC.colors
import io.inappchat.sdk.ui.theme.IAC.fonts
import io.inappchat.sdk.utils.SampleUser
import io.inappchat.sdk.utils.annotated
import io.inappchat.sdk.utils.ift

@Preview
@Composable
fun ContactRow(@PreviewParameter(SampleUser::class) user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = 16.dp, top = 12.dp, bottom = 12.dp
            )
            .height(84.dp)
            .fillMaxWidth()
    ) {
        Avatar(url = user.avatar, size = 60.0)
        Space(8f)
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.usernameFb.annotated(),
                    iac = fonts.title3,
                    maxLines = 1,
                    color = colors.text
                )
                if (user.haveContact) {
                    Image(
                        painter = painterResource(id = R.drawable.address_book_fill),
                        contentDescription = "contact",
                        modifier = Modifier.size(18),
                        colorFilter = ColorFilter.tint(Color(0x488AC7))
                    )
                }
            }
            Text(
                text = user.status.value.capitalize(Locale.current).annotated(),
                iac = fonts.body,
                color = ift(
                    user.status == AvailabilityStatus.online,
                    colors.primary,
                    colors.caption
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}