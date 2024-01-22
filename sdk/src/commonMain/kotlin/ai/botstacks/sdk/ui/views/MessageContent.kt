/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.yazantarifi.compose.library.MarkdownConfig
import com.yazantarifi.compose.library.MarkdownViewComposable
import ai.botstacks.sdk.R
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.location
import ai.botstacks.sdk.state.markdown
import ai.botstacks.sdk.state.vcard
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.ui.IAC.colors
import ai.botstacks.sdk.ui.IAC.theme
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genFileMessage
import ai.botstacks.sdk.utils.genImageMessage
import ai.botstacks.sdk.utils.genChatextMessage
import ai.botstacks.sdk.utils.ift

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageContent(message: Message, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                ift(
                    message.user.isCurrent,
                    colors.senderBubble,
                    colors.bubble
                ),
                RoundedCornerShape(theme.bubbleRadius.dp)
            )
            .clipToBounds()
    ) {

        val openUrl = LocalUriHandler.current
        if (!message.attachments.isEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (attachment in message.attachments) {
                    when (attachment.type) {
                        AttachmentType.image -> AsyncImage(
                            model = attachment.url,
                            contentDescription = "shared image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(
                                    theme.imagePreviewSize.width.dp,
                                )
                                .height(
                                    theme.imagePreviewSize.height.dp
                                )
                                .clip(RoundedCornerShape(15.dp))
                        )

                        AttachmentType.video -> VideoPlayer(
                            uri = attachment.url.toUri(),
                            modifier = Modifier
                                .width(theme.videoPreviewSize.width.dp)
                                .height(theme.videoPreviewSize.height.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )

                        AttachmentType.audio -> AudioPlayer(
                            url = attachment.url
                        )

                        AttachmentType.file -> Image(
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

                        AttachmentType.location, AttachmentType.vcard -> MarkdownViewComposable(
                            modifier = Modifier.padding(theme.bubblePadding),
                            content = attachment.location()?.markdown ?: attachment.vcard()
                                ?.markdown()
                            ?: "No content",
                            config = theme.markdownConfig(message.user.isCurrent),
                            onLinkClickListener = { link, type ->
                                when (type) {
                                    MarkdownConfig.IMAGE_TYPE -> {} // Image Clicked
                                    MarkdownConfig.LINK_TYPE -> {
                                        openUrl.openUri(link)
                                    } // Link Clicked
                                }
                            }
                        )

                        else -> null
                    }
                }
            }
        }
        val ct = message.markdown
        if (!ct.isNullOrEmpty()) {
            MarkdownViewComposable(
                modifier = Modifier
                    .padding(theme.bubblePadding),
                content = ct,
                config = theme.markdownConfig(message.user.isCurrent)
            ) { link, type ->
                when (type) {
                    MarkdownConfig.IMAGE_TYPE -> {} // Image Clicked
                    MarkdownConfig.LINK_TYPE -> {
                        openUrl.openUri(link)
                    } // Link Clicked
                }
            }
        }
    }
}

@IPreviews
@Composable
fun MessageContentPreview() {
    BotStacksChatContext {
        Column {
            MessageContent(message = genImageMessage())
            MessageContent(message = genFileMessage())
            MessageContent(message = genChatextMessage())
        }
    }
}