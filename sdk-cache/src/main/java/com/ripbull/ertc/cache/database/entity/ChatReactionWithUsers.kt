package com.ripbull.ertc.cache.database.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by DK on 05/08/20.
 */
class ChatReactionWithUsers {
  @Embedded
  var chatReactionEntity: ChatReactionEntity? = null

  @Relation(parentColumn = "user_chat_id", entityColumn = "user_chat_id")
  var users: List<User>? = null
}