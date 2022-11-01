package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by DK on 2019-08-14.
 */
@Entity(
  tableName = "single_thread_meta_data",
  indices = [Index(value = ["thread_msg_id"])],
  foreignKeys = [ForeignKey(
    entity = Thread::class,
    parentColumns = ["id"],
    childColumns = ["thread_msg_id"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.CASCADE
  )]
)
data class SingleThreadMetadata(
  @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
  @ColumnInfo(name = "thread_msg_id") var threadMsgId: String?,
  @ColumnInfo(name = "key") var key: String?,
  @ColumnInfo(name = "value") var value: String?

)