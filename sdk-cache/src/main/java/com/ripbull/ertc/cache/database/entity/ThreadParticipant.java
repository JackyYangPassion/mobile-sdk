package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/** @author meeth */
@Entity(
    tableName = "thread_participant",
    primaryKeys = {"thread_id", "user_id"},
    foreignKeys =
        @ForeignKey(
            entity = Tenant.class,
            parentColumns = "id",
            childColumns = "tenant_id",
            onDelete = ForeignKey.CASCADE))
public class ThreadParticipant {

  @NonNull
  @ColumnInfo(name = "thread_id")
  private String threadId = "";

  @NonNull
  @ColumnInfo(name = "user_id")
  private String userId = "";
}
