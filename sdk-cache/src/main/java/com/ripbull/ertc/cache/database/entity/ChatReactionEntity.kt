package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by DK on 13/06/20.
 */
@Entity(
  tableName = "chat_reaction",
  indices = [Index(value = ["id", "chat_msg_id","chat_thread_msg_id"], unique = true),
    Index(value = ["chat_msg_id", "unicode", "user_chat_id"], unique = true),
    Index(value = ["chat_thread_msg_id", "unicode", "user_chat_id"], unique = true)],
  foreignKeys = [ForeignKey(
    entity = SingleChat::class,
    parentColumns = ["id"],
    childColumns = ["chat_msg_id"],
    onDelete = ForeignKey.CASCADE
  ),ForeignKey(
    entity = ChatThread::class,
    parentColumns = ["id"],
    childColumns = ["chat_thread_msg_id"],
    onDelete = ForeignKey.CASCADE
  )]
) // @Index(value = { "recipient_id", "sender_id" })

data class ChatReactionEntity @JvmOverloads constructor(
  @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")  val id: Long = 0,
  @ColumnInfo(name = "unicode") var unicode: String,
  @ColumnInfo(name = "thread_id") var threadId: String?,
  @ColumnInfo(name = "user_chat_id") var userChatId: String?,
  //Non -overridden attributes
  @ColumnInfo(name = "chat_msg_id") var chatMsgId: String? = null, // //foreign key constraint, this is single_chat/thread generated local id.
  @ColumnInfo(name = "chat_thread_msg_id") var chatThreadMsgId: String? = null,
  @ColumnInfo(name = "total_count") var totalCount: Int? = 0
)

