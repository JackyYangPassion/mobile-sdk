package com.ripbull.ertc.cache.database.entity

import androidx.room.ColumnInfo

/**
 * Created by DK on 2019-08-14.
 */

data class Location(
  @ColumnInfo(name = "address") var address: String?, @ColumnInfo(name = "latitude") var latitude: Double?, @ColumnInfo(
    name = "longitude"
  ) var longitude: Double?
)
