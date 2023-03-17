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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
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
import io.inappchat.sdk.ui.IAC.theme
import io.inappchat.sdk.ui.InAppChatContext
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
      ?: message.markdown
      val openUrl = LocalUriHandler.current
      val textColor = if (message.user.isCurrent) colors.senderText else colors.bubbleText
      MarkdownViewComposable(
        modifier = Modifier
          .padding(theme.bubblePadding),
        content = ct,
        config = MarkdownConfig(
          isLinksClickable = true,
          isImagesClickable = false,
          isScrollEnabled = false,
          colors = HashMap<String, Color>().apply {
            this[MarkdownConfig.CHECKBOX_COLOR] = Color.Black
            this[MarkdownConfig.LINKS_COLOR] = colors.primary
            this[MarkdownConfig.TEXT_COLOR] = textColor
            this[MarkdownConfig.HASH_TEXT_COLOR] = colors.primary
            this[MarkdownConfig.CODE_BACKGROUND_COLOR] = Color.Gray
            this[MarkdownConfig.CODE_BLOCK_TEXT_COLOR] = Color.White
          }
        )
      ) { link, type ->
        when (type) {
          MarkdownConfig.IMAGE_TYPE -> {} // Image Clicked
          MarkdownConfig.LINK_TYPE -> {
            openUrl.openUri(link)
          } // Link Clicked
        }
      }
//      MarkdownText(
//        markdown = ct,
//        color = ift(message.user.isCurrent, colors.senderText, colors.bubbleText),
//        annotationStyle = MarkdownTextDefaults.style.copy(
//          linkStyle = MarkdownTextDefaults.linkStyle.copy(
//            color = colors.primary
//          )
//        ),
//        flavour = MarkdownFlavour.Github
//      )
//      Markdown(
//        content = ct,
//        colors = MarkdownDefaults.markdownColors(textColor = colors.bubbleText, colorByType = {
//          when (it) {
//            MarkdownElementTypes.AUTOLINK, MarkdownElementTypes.LINK_TEXT, MarkdownElementTypes.LINK_LABEL, MarkdownElementTypes.LINK_TEXT, MarkdownElementTypes.INLINE_LINK -> clrs.primary
//            else -> if(message.user.isCurrent) clrs.senderText else  clrs.bubbleText
//          }
//        }),
//        typography = MarkdownDefaults.markdownTypography(body1 = TextStyle(fontSize =  fnts.body.size, fontWeight = fnts.body.weight, fontFamily = fnts.body.family), body2 = TextStyle(fontFamily = fnts.body.family, fontWeight = fnts.body.weight, fontSize = fnts.body.size))
//      )
    }
  }
}

@IPreviews
@Composable
fun MessageContentPreview() {
  InAppChatContext {
    Column {
      MessageContent(message = genImageMessage())
      MessageContent(message = genFileMessage())
      MessageContent(message = genContactMessage())
      MessageContent(message = genLocationMessage())
      MessageContent(message = genTextMessage())
    }
  }
}