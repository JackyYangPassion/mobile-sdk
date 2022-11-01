package com.ripbull.ertc.cache.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(
    tableName = "group",
    indices = {@Index(value = "group_id"), @Index(value = "tenant_id")},
    foreignKeys =
        @ForeignKey(
            entity = Tenant.class,
            parentColumns = "id",
            childColumns = "tenant_id",
            onDelete = ForeignKey.CASCADE))
public class Group implements Serializable {

  @PrimaryKey
  @NonNull
  @ColumnInfo(name = "group_id")
  private String groupId;

  @NonNull
  @ColumnInfo(name = "tenant_id")
  private String tenantId = "";

  @NonNull
  @ColumnInfo(name = "thread_id")
  private String threadId = "";

  @ColumnInfo(name = "group_type")
  private String groupType = "";

  @ColumnInfo(name = "name")
  private String name = "";

  @ColumnInfo(name = "login_type")
  private String loginType = "";

  @ColumnInfo(name = "group_pic")
  private String groupPic = "";

  @ColumnInfo(name = "group_thumb")
  private String groupThumb = "";

  @ColumnInfo(name = "group_desc")
  private String groupDesc = "";

  @ColumnInfo(name = "login_timestamp")
  private long loginTimestamp;

  @ColumnInfo(name = "group_users")
  private ArrayList<User> groupUsers;

  @ColumnInfo(name = "joined")
  private Integer joined;

  @ColumnInfo(name = "participants_count")
  private Integer participantsCount;

  @ColumnInfo(name = "group_status")
  private String groupStatus = "active";

  @NonNull
  public String getGroupId() {
    return groupId;
  }

  @NonNull
  public String getTenantId() {
    return tenantId;
  }

  public String getName() {
    return name;
  }

  public String getLoginType() {
    return loginType;
  }

  public String getGroupPic() {
    return groupPic;
  }

  public String getGroupThumb() {
    return groupThumb;
  }

  public String getGroupDesc() {
    return groupDesc;
  }

  public long getLoginTimestamp() {
    return loginTimestamp;
  }

  public ArrayList<User> getGroupUsers() {
    return groupUsers;
  }

  public void setGroupId(@NonNull String groupId) {
    this.groupId = groupId;
  }

  public void setTenantId(@NonNull String tenantId) {
    this.tenantId = tenantId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }

  public void setGroupPic(String groupPic) {
    this.groupPic = groupPic;
  }

  public void setGroupThumb(String groupThumb) {
    this.groupThumb = groupThumb;
  }

  public void setGroupDesc(String groupDesc) {
    this.groupDesc = groupDesc;
  }

  public void setLoginTimestamp(long loginTimestamp) {
    this.loginTimestamp = loginTimestamp;
  }

  public void setGroupUsers(ArrayList<User> groupUsers) {
    this.groupUsers = groupUsers;
  }

  public String getGroupType() {
    return groupType;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
  }

  @NonNull
  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(@NonNull String threadId) {
    this.threadId = threadId;
  }

  public Integer getJoined() {
    return joined;
  }

  public void setJoined(Integer joined) {
    this.joined = joined;
  }

  public Integer getParticipantsCount() {
    return participantsCount;
  }

  public void setParticipantsCount(Integer participantsCount) {
    this.participantsCount = participantsCount;
  }

  public String getGroupStatus() {
    return groupStatus;
  }

  public void setGroupStatus(String groupStatus) {
    this.groupStatus = groupStatus;
  }
}
