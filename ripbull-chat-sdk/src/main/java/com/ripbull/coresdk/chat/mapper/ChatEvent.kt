package com.ripbull.coresdk.chat.mapper


import com.ripbull.coresdk.core.type.EventType
import io.reactivex.functions.Predicate

/**
 * Created by DK on 05/08/20.
 */

data class ChatEvent(
  var eventType: EventType? = null,
  val threadId: String? = null,
  var messageRecord: MessageRecord? = null,
  val chatReactionRecord: ChatReactionRecord? = null,
  val threadMetadata: ThreadMessageMetadata? = null
) {
  companion object {
    @JvmStatic
    fun filterType(type: EventType): Predicate<ChatEvent>? {
      return Predicate { networkEvent: ChatEvent -> networkEvent.eventType === type }
    }
  }
}