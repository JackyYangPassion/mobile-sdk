package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "thread_user_link",
    indices = {@Index(value = "thread_id")},
    foreignKeys =
        @ForeignKey(
            entity = Thread.class,
            parentColumns = "id",
            childColumns = "thread_id",
            onDelete = ForeignKey.CASCADE))
public class ThreadUserLink {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "id")
  private int id;

  @NonNull
  @ColumnInfo(name = "sender_id")
  private String senderId = "";

  @NonNull
  @ColumnInfo(name = "recipient_id")
  private String recipientId = "";

  @NonNull
  @ColumnInfo(name = "thread_id")
  private String threadId = "";

  public ThreadUserLink(
      @NonNull String senderId, @NonNull String recipientId, @NonNull String threadId) {
    this.senderId = senderId;
    this.recipientId = recipientId;
    this.threadId = threadId;
  }

  @NonNull
  public int getId() {
    return id;
  }

  public void setId(@NonNull int id) {
    this.id = id;
  }

  @NonNull
  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(@NonNull String senderId) {
    this.senderId = senderId;
  }

  @NonNull
  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(@NonNull String recipientId) {
    this.recipientId = recipientId;
  }

  @NonNull
  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(@NonNull String threadId) {
    this.threadId = threadId;
  }
}
