/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import io.inappchat.sdk.R
import io.inappchat.sdk.state.AttachmentKind
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.markdown
import io.inappchat.sdk.ui.theme.IAC.colors
import io.inappchat.sdk.ui.theme.IAC.richTextStyle
import io.inappchat.sdk.ui.theme.IAC.theme
import io.inappchat.sdk.utils.SampleMessage
import io.inappchat.sdk.utils.ift

@Preview
@Composable
fun MessageContent(@PreviewParameter(SampleMessage::class) message: Message) {
    Column(
        modifier = Modifier.background(
            ift(
                message.user.isCurrent,
                colors.senderBubble,
                colors.bubble
            ),
            RoundedCornerShape(theme.bubbleRadius.dp)
        )
    ) {
        message.attachment?.let {
            when (it.kind) {
                AttachmentKind.image -> AsyncImage(
                    model = it.url,
                    contentDescription = "shareed image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(
                            theme.imagePreviewSize.width.dp,
                        )
                        .height(
                            theme.imagePreviewSize.height.dp
                        )
                )
                AttachmentKind.video -> VideoPlayer(
                    uri = it.url.toUri(),
                    modifier = Modifier
                        .width(theme.videoPreviewSize.width.dp)
                        .height(theme.videoPreviewSize.height.dp)
                )
                AttachmentKind.audio -> AudioPlayer(
                    uri = it.url.toUri()
                )
                AttachmentKind.file -> Image(
                    painter = painterResource(id = R.drawable.file_arrow_down_fill),
                    contentDescription = "File",
                    colorFilter = ColorFilter.tint(
                        ift(
                            message.user.isCurrent,
                            theme.colors.senderText,
                            theme.colors.bubbleText
                        )
                    ),
                    modifier = Modifier.size(64)
                )
            }
        } ?: RichText(
            modifier = Modifier.padding(theme.bubblePadding),
            style = richTextStyle
        ) {
            Markdown(
                content = message.location?.markdown() ?: message.contact?.markdown()
                ?: message.markdown
            )
        }
    }
}