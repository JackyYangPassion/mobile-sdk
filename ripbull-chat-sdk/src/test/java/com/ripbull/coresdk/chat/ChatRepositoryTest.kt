package com.ripbull.coresdk.chat

import com.ripbull.coresdk.chat.model.Media
import com.ripbull.coresdk.chat.model.Message
import com.ripbull.coresdk.core.type.ChatType
import com.ripbull.coresdk.core.type.MessageType
import org.junit.Test
import java.util.regex.*

/**
 * Created by DK on 01/06/20.
 */


class ChatRepositoryTest {
  val REX_PATTERN = ""
  private val messageWithText = Message(message = "Hey! whatt's up", chatType = ChatType.SINGLE)
  private val messageWithImage =
    Message(
      message = "Hey! whatt's up",
      media = Media("/local/image.png", MessageType.IMAGE),
      chatType = ChatType.SINGLE
    )
  private val messageWithMentions =
    Message(
      message = "@here Hey there! how're you? FYI,@Tokyo @Denver",
      media = Media("/local/image.png", MessageType.IMAGE),
      chatType = ChatType.SINGLE
    )

  @Test
  fun `send message with text`() {

  }

  @Test
  fun `send message with image`() {

  }

  @Test
  fun `send message with mentions`() {
    val compile = Pattern.compile("")

  }
}