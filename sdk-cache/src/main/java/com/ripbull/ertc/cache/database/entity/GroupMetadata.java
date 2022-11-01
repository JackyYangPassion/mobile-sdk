package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Created by DK on 02/01/19. */
@Entity(
    tableName = "group_metadata",
    indices = {@Index(value = "group_id")},
    foreignKeys =
        @ForeignKey(
            entity = Group.class,
            parentColumns = "group_id",
            childColumns = "group_id",
            onDelete = ForeignKey.CASCADE))
public class GroupMetadata {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  @ColumnInfo(name = "id")
  private int id;

  @NonNull
  @ColumnInfo(name = "group_id")
  private String groupId = "";

  @NonNull
  @ColumnInfo(name = "key")
  private String key = "";

  @NonNull
  @ColumnInfo(name = "value")
  private String value = "";

  public GroupMetadata(@NonNull String groupId, @NonNull String key, @NonNull String value) {
    this.groupId = groupId;
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
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(@NonNull String groupId) {
    this.groupId = groupId;
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
