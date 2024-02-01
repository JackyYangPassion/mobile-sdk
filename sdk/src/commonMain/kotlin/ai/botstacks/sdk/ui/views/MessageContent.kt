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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.yazantarifi.compose.library.MarkdownConfig
import com.yazantarifi.compose.library.MarkdownViewComposable
import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.location
import ai.botstacks.sdk.state.markdown
import ai.botstacks.sdk.state.vcard
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.ui.theme.LocalBotStacksMarkdownConfig
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genFileMessage
import ai.botstacks.sdk.utils.genImageMessage
import ai.botstacks.sdk.utils.genChatextMessage
import ai.botstacks.sdk.utils.ift
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun MessageContent(message: Message, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                ift(
                    message.user.isCurrent,
                    colorScheme.primary,
                    colorScheme.message
                ),
                BotStacks.shapes.small,
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
                                .width(dimens.imagePreviewSize.width.dp,)
                                .height(dimens.imagePreviewSize.height.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )

                        AttachmentType.video -> VideoPlayer(
                            uri = attachment.url.toUri(),
                            modifier = Modifier
                                .width(dimens.videoPreviewSize.width.dp)
                                .height(dimens.videoPreviewSize.height.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )

                        AttachmentType.audio -> AudioPlayer(
                            url = attachment.url
                        )

                        AttachmentType.file -> Image(
                            painter = painterResource(Res.Drawables.Filled.FileArrowDown),
                            contentDescription = "File",
                            colorFilter = ColorFilter.tint(
                                ift(
                                    message.user.isCurrent,
                                    colorScheme.onPrimary,
                                    colorScheme.onMessage
                                )
                            ),
                            modifier = Modifier.size(64)
                        )

                        AttachmentType.location, AttachmentType.vcard -> MarkdownViewComposable(
                            modifier = Modifier.padding(dimens.grid.x2),
                            content = attachment.location()?.markdown ?: attachment.vcard()
                                ?.markdown()
                            ?: "No content",
                            config = LocalBotStacksMarkdownConfig.current(message.user.isCurrent),
                            onLinkClickListener = { link, type ->
                                when (type) {
                                    MarkdownConfig.IMAGE_TYPE -> {} // Image Clicked
                                    MarkdownConfig.LINK_TYPE -> {
                                        openUrl.openUri(link)
                                    } // Link Clicked
                                }
                            }
                        )

                        else -> {}
                    }
                }
            }
        }
        val ct = message.markdown
        if (ct.isNotEmpty()) {
            MarkdownViewComposable(
                modifier = Modifier
                    .padding(dimens.grid.x2),
                content = ct,
                config = LocalBotStacksMarkdownConfig.current(message.user.isCurrent),
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