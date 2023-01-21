package io.inappchat.inappchat.cache.database.entity

import androidx.room.ColumnInfo

/**
 * Created by DK on 2019-08-14.
 */

data class PhoneContact(
  @ColumnInfo(name = "type") var type: String?, @ColumnInfo(name = "number") var number: String?
)
