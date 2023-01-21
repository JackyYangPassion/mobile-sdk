package io.inappchat.inappchat.cache.database.entity

import androidx.room.ColumnInfo

/**
 * Created by DK on 2019-08-14.
 */

data class EmailContact(
  @ColumnInfo(name = "type") var type: String?, @ColumnInfo(name = "email") var email: String?
)
