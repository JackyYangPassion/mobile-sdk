package com.ripbull.ertc.cache.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "profanity_filter",
  indices = [Index(value = ["tenant_id"])],
  foreignKeys = [ForeignKey(
    entity = Tenant::class,
    parentColumns = ["id"],
    childColumns = ["tenant_id"],
    onDelete = ForeignKey.CASCADE
  )]
)

data class ProfanityFilter(
  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "keyword") var keyword : String,
  @ColumnInfo(name = "tenant_id") var tenantId : String? = null,
  @ColumnInfo(name = "regex") var regex : String? = null,
  @ColumnInfo(name = "action_type") var actionType : String? = null
)
