/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.ui.components

import ai.botstacks.sdk.state.Message
import ai.botstacks.sdk.state.location
import ai.botstacks.sdk.state.vcard
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.ui.BotStacks
import ai.botstacks.sdk.ui.BotStacks.colorScheme
import ai.botstacks.sdk.ui.BotStacks.dimens
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.components.internal.ImageRenderer
import ai.botstacks.sdk.ui.components.internal.MarkdownView
import ai.botstacks.sdk.ui.components.internal.VideoPlayer
import ai.botstacks.sdk.ui.resources.Res
import ai.botstacks.sdk.utils.IPreviews
import ai.botstacks.sdk.utils.genChatextMessage
import ai.botstacks.sdk.utils.genFileMessage
import ai.botstacks.sdk.utils.genImageMessage
import ai.botstacks.sdk.utils.ift
import ai.botstacks.sdk.utils.markdown
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
import androidx.compose.ui.unit.dp
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


        if (!message.attachments.isEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (attachment in message.attachments) {
                    when (attachment.type) {
                        AttachmentType.image -> ImageRenderer(
                            url = attachment.url,
                            contentDescription = "shared image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(dimens.imagePreviewSize.width.dp,)
                                .height(dimens.imagePreviewSize.height.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )

                        AttachmentType.video -> VideoPlayer(
                            url = attachment.url,
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

                        AttachmentType.location, AttachmentType.vcard -> MarkdownView(
                            modifier = Modifier
                                .padding(dimens.grid.x2),
                            content = attachment.location()?.markdown
                                ?: attachment.vcard()?.markdown()
                                ?: "No content",
                            isCurrentUser = message.user.isCurrent,
                        )

                        else -> {
                            MarkdownView(
                                modifier = Modifier
                                    .padding(dimens.grid.x2),
                                content = message.markdown,
                                isCurrentUser = message.user.isCurrent,
                            )
                        }
                    }
                }
            }
        }
        val ct = message.markdown
        if (ct.isNotEmpty()) {
            MarkdownView(
                modifier = Modifier
                    .padding(dimens.grid.x2),
                content = ct,
                isCurrentUser = message.user.isCurrent,
            )
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