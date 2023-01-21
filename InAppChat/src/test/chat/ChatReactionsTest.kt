package io.inappchat.inappchat.chat

import com.ripbull.ertc.cache.database.entity.ChatReactionEntity
import org.junit.Assert
import org.junit.Test

/**
 * Created by DK on 25/06/20.
 */
class ChatReactionsTest {

  private val listOfChatReactions = arrayListOf<ChatReactionEntity>(
    ChatReactionEntity(
      1,
      "U+1F600",
      "5e9eb02c66a35d0009917fe8",
      "5e9dcde61d23950153e82c00",
      "5ea941668f4b15000850290f"
    ),
    ChatReactionEntity(
      2,
      "U+1F600",
      "5e9eb02c66a35d0009917fe9",
      "5e9dcde61d23950153e82c10",
      "5ea941668f4b150008502911"
    ),
    ChatReactionEntity(
      3,
      "U+1F600",
      "5e9eb02c66a35d0009917fe9",
      "5e9dcde61d23950153e82c11",
      "5ea941668f4b150008502911"
    )
  )

  @Test
  fun `group by chat reactions and validate the count`() {
    val groupByEmoji = listOfChatReactions.groupBy { it.unicode }
    val list = groupByEmoji["U+1F600"]
    val useChatIdList = list?.groupBy { it.userChatId }?.keys?.toList()

    Assert.assertEquals(1,groupByEmoji.keys.size)
    Assert.assertEquals(1,groupByEmoji.values.size)

    Assert.assertEquals(3,useChatIdList?.size)
    Assert.assertEquals("5e9dcde61d23950153e82c00",useChatIdList?.get(0))
    Assert.assertEquals("5e9dcde61d23950153e82c10",useChatIdList?.get(1))
  }

}
