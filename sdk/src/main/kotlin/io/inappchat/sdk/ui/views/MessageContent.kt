/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.yazantarifi.compose.library.MarkdownConfig
import com.yazantarifi.compose.library.MarkdownViewComposable
import io.inappchat.sdk.R
import io.inappchat.sdk.state.AttachmentKind
import io.inappchat.sdk.state.Message
import io.inappchat.sdk.state.markdown
import io.inappchat.sdk.ui.IAC.colors
import io.inappchat.sdk.ui.IAC.fonts
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.utils.*

@Composable
fun MessageContent(message: Message, modifier: Modifier = Modifier) {
  Column(
    modifier = modifier.background(
      ift(
        message.user.isCurrent,
        colors.senderBubble,
        colors.bubble
      ),
      RoundedCornerShape(theme.bubbleRadius.dp)
    )
  ) {
    if (message.attachment != null) {
      when (message.attachment.kind) {
        AttachmentKind.image -> AsyncImage(
          model = message.attachment.url,
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
          uri = message.attachment.url.toUri(),
          modifier = Modifier
            .width(theme.videoPreviewSize.width.dp)
            .height(theme.videoPreviewSize.height.dp)
        )
        AttachmentKind.audio -> AudioPlayer(
          url = message.attachment.url
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
    } else {
      val ct = message.location?.markdown() ?: message.contact?.markdown()
      ?: message.markdown ?: "hello"
      Text(text = ct, iac = fonts.body)
      MarkdownViewComposable(
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp),
        content = ct,
        config = MarkdownConfig(
          isLinksClickable = true,
          isImagesClickable = true,
          isScrollEnabled = false
        )
      ) { link, type ->
        // Callback on Click on Images, Links To Redirect to Image Viewer or WebView Screen
      }
    }
  }
}

@IPreviews
@Composable
fun MessageContentPreview() {
  Column {
    MessageContent(message = genImageMessage())
    MessageContent(message = genFileMessage())
    MessageContent(message = genContactMessage())
    MessageContent(message = genLocationMessage())
    MessageContent(message = genTextMessage())
  }
}