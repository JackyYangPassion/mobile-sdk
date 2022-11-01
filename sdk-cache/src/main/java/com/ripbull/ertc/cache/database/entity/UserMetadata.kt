package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Created by DK on 02/01/19.  */
@Entity(
  tableName = "user_metadata",
  indices = [Index(value = ["user_id"])],
  foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = ["id"],
    childColumns = ["user_id"],
    onDelete = ForeignKey.CASCADE
  )]
)
data class UserMetadata @JvmOverloads constructor(
  @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id : Int,
  @ColumnInfo(name = "user_id") var userId : String? = null,
  @ColumnInfo(name = "key") var key : String? = null,
  @ColumnInfo(name = "value") var value : String? = null
)