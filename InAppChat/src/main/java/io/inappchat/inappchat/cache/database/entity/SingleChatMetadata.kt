package io.inappchat.inappchat.cache.database.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by DK on 2019-08-14.
 */
@Entity(
  tableName = "single_chat_meta_data",
  indices = [Index(value = ["message_id"])],
  foreignKeys = [ForeignKey(
    entity = Thread::class,
    parentColumns = ["id"],
    childColumns = ["message_id"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
  )]
)

data class SingleChatMetadata(
  @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
  @ColumnInfo(name = "message_id") var messageId: String?,
  @ColumnInfo(name = "key") var key: String?,
  @ColumnInfo(name = "value") var value: String?

)