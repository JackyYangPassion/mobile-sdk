package com.ripbull.coresdk.core.type

enum class ChatEventType(val type: String) {
  INCOMING("incoming"),
  OUTGOING("outgoing"),
  CHAT_META_DATA("chat_meta_data")
}