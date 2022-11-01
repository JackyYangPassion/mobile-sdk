package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Created by DK on 02/01/19. */
@Entity(
    tableName = "thread_meta_data",
    indices = {@Index(value = "thread_id")},
    foreignKeys =
        @ForeignKey(
            entity = Thread.class,
            parentColumns = "id",
            childColumns = "thread_id",
            onDelete = ForeignKey.CASCADE))
public class ThreadMetadata {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "id")
  private int id;

  @NonNull
  @ColumnInfo(name = "thread_id")
  private String threadId = "";

  @NonNull
  @ColumnInfo(name = "key")
  private String key = "";

  @NonNull
  @ColumnInfo(name = "value")
  private String value = "";

  public ThreadMetadata(
      int id, @NonNull String threadId, @NonNull String key, @NonNull String value) {
    this.id = id;
    this.threadId = threadId;
    this.key = key;
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(@NonNull String threadId) {
    this.threadId = threadId;
  }

  @NonNull
  public String getKey() {
    return key;
  }

  public void setKey(@NonNull String key) {
    this.key = key;
  }

  @NonNull
  public String getValue() {
    return value;
  }

  public void setValue(@NonNull String value) {
    this.value = value;
  }
}
