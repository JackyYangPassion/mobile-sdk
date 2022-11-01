package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

/** Created by Vik on 21/11/19.  */
@Entity(
  tableName = "eKeys",
  indices = [Index(value = ["keyId", "deviceId", "ertcUserId"], unique = true)]
)
data class EKeyTable @JvmOverloads constructor(
  @PrimaryKey(autoGenerate = true) var id : Int = 0,

  @ColumnInfo(name = "keyId")
  var keyId :String? = null,

  @ColumnInfo(name = "deviceId")
  var deviceId :String? = null,

  @ColumnInfo(name = "publicKey")
  var publicKey :String? = null,

  @ColumnInfo(name = "privateKey")
  var privateKey :String? = null,

  @ColumnInfo(name = "ertcUserId")
  var ertcUserId :String? = null,

  @ColumnInfo(name = "tenant_id")
  var tenantId :String? = null,

  @ColumnInfo(name = "time")
  var time : Long = System.currentTimeMillis()
)
