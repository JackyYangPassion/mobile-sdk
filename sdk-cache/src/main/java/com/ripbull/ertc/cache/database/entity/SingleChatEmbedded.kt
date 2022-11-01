package com.ripbull.ertc.cache.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/** Created by DK on 10/03/19.  */
class SingleChatEmbedded {

  @Embedded
  var singleChat: SingleChat? = null

  @Relation(parentColumn = "id", entityColumn = "parent_msg_id")
  var listChatThread: List<ChatThread>? = null

  @Relation(parentColumn = "id", entityColumn = "chat_msg_id")
  var reactionEntities: List<ChatReactionEntity>? = null

}
