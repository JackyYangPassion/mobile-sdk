package io.inappchat.inappchat.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(
    tableName = "thread",
    indices = {@Index(value = "tenant_id")},
    foreignKeys =
        @ForeignKey(
            entity = Tenant.class,
            parentColumns = "id",
            childColumns = "tenant_id",
            onDelete = ForeignKey.CASCADE))
public class Thread implements Serializable {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "id")
  private String id = "";

  @NonNull
  @ColumnInfo(name = "name")
  private String name = "";

  @NonNull
  @ColumnInfo(name = "type")
  private String type = "";

  @NonNull
  @ColumnInfo(name = "tenant_id")
  private String tenantId = "";

  @NonNull
  @ColumnInfo(name = "sender_chat_id")
  private String senderChatId = ""; //me - sender

  @NonNull
  @ColumnInfo(name = "recipient_chat_id")
  private String recipientChatId = ""; //you - receiver

  @NonNull
  @ColumnInfo(name = "message_count")
  private int messageCount;

  @NonNull
  @ColumnInfo(name = "read")
  private int read; // 0/1

  @NonNull
  @ColumnInfo(name = "unread_count")
  private int unReadCount;

  @NonNull
  @ColumnInfo(name = "creation_date")
  private long creationDate;

  @NonNull
  @ColumnInfo(name = "has_deleted")
  private int hasDeleted; // 0/1

  @NonNull
  @ColumnInfo(name = "creator_tenant_id")
  private String creatorTenantId = "";

  @NonNull
  @ColumnInfo(name = "creator_user_id")
  private String creatorUserId = "";

  @NonNull
  @ColumnInfo(name = "sender_user_id")
  private String senderUserId = "";

  @NonNull
  @ColumnInfo(name = "recipient_user_id")
  private String recipientUserId = "";

  @NonNull
  @ColumnInfo(name = "updated_at")
  private long updatedAt;

  @ColumnInfo(name = "notification_settings")
  private String notificationSettings;

  @ColumnInfo(name = "valid_till")
  private long validTill;

  @ColumnInfo(name = "valid_till_value")
  private String validTillValue;

  public Thread(
      @NonNull String id,
      @NonNull String name,
      @NonNull String type,
      @NonNull String tenantId,
      @NonNull String senderChatId,
      @NonNull String recipientChatId,
      @NonNull int messageCount,
      @NonNull int read,
      @NonNull int unReadCount,
      @NonNull long creationDate,
      @NonNull int hasDeleted,
      @NonNull String creatorTenantId,
      @NonNull String creatorUserId,
      @NonNull String senderUserId,
      @NonNull String recipientUserId,
      @NonNull long updatedAt,
      String notificationSettings,
      long validTill,
      String validTillValue) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.tenantId = tenantId;
    this.senderChatId = senderChatId;
    this.recipientChatId = recipientChatId;
    this.messageCount = messageCount;
    this.read = read;
    this.unReadCount = unReadCount;
    this.creationDate = creationDate;
    this.hasDeleted = hasDeleted;
    this.creatorTenantId = creatorTenantId;
    this.creatorUserId = creatorUserId;
    this.senderUserId = senderUserId;
    this.recipientUserId = recipientUserId;
    this.updatedAt = updatedAt;
    this.notificationSettings = notificationSettings;
    this.validTill = validTill;
    this.validTillValue = validTillValue;
  }

  @NonNull
  public String getSenderUserId() {
    return senderUserId;
  }

  public void setSenderUserId(@NonNull String senderUserId) {
    this.senderUserId = senderUserId;
  }

  @NonNull
  public String getRecipientUserId() {
    return recipientUserId;
  }

  public void setRecipientUserId(@NonNull String recipientUserId) {
    this.recipientUserId = recipientUserId;
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getType() {
    return type;
  }

  public void setType(@NonNull String type) {
    this.type = type;
  }

  @NonNull
  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(@NonNull String tenantId) {
    this.tenantId = tenantId;
  }

  @NonNull
  public String getSenderChatId() {
    return senderChatId;
  }

  public void setSenderChatId(@NonNull String senderChatId) {
    this.senderChatId = senderChatId;
  }

  @NonNull
  public String getRecipientChatId() {
    return recipientChatId;
  }

  public void setRecipientChatId(@NonNull String recipientChatId) {
    this.recipientChatId = recipientChatId;
  }

  public int getMessageCount() {
    return messageCount;
  }

  public void setMessageCount(int messageCount) {
    this.messageCount = messageCount;
  }

  public int getRead() {
    return read;
  }

  public void setRead(int read) {
    this.read = read;
  }

  public int getUnReadCount() {
    return unReadCount;
  }

  public void setUnReadCount(int unReadCount) {
    this.unReadCount = unReadCount;
  }

  public long getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public int getHasDeleted() {
    return hasDeleted;
  }

  public void setHasDeleted(int hasDeleted) {
    this.hasDeleted = hasDeleted;
  }

  @NonNull
  public String getCreatorTenantId() {
    return creatorTenantId;
  }

  public void setCreatorTenantId(@NonNull String creatorTenantId) {
    this.creatorTenantId = creatorTenantId;
  }

  @NonNull
  public String getCreatorUserId() {
    return creatorUserId;
  }

  public void setCreatorUserId(@NonNull String creatorUserId) {
    this.creatorUserId = creatorUserId;
  }

  public long getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getNotificationSettings() {
    return notificationSettings;
  }

  public void setNotificationSettings(String notificationSettings) {
    this.notificationSettings = notificationSettings;
  }

  public long getValidTill() {
    return validTill;
  }

  public void setValidTill(long validTill) {
    this.validTill = validTill;
  }

  public String getValidTillValue() {
    return validTillValue;
  }

  public void setValidTillValue(String validTillValue) {
    this.validTillValue = validTillValue;
  }
}
