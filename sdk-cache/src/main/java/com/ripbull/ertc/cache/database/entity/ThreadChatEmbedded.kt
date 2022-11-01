package com.ripbull.ertc.cache.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.ripbull.ertc.cache.database.entity.ChatReactionEntity
import com.ripbull.ertc.cache.database.entity.ChatThread

/**
 * Created by DK on 02/07/20.
 */
class ThreadChatEmbedded {

  @Embedded
  var chatThread: ChatThread? = null

  @Relation(parentColumn = "id", entityColumn = "chat_thread_msg_id")
  var reactionEntities: List<ChatReactionEntity>? = null

}
