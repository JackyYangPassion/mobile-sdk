package io.inappchat.inappchat.chat

import io.inappchat.inappchat.chat.model.Media
import io.inappchat.inappchat.chat.model.Message
import io.inappchat.inappchat.core.type.ChatType
import io.inappchat.inappchat.core.type.MessageType
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