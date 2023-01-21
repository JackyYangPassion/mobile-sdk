package io.inappchat.inappchat.group.mapper;

import io.inappchat.inappchat.user.mapper.UserRecord;
import java.io.Serializable;
import java.util.List;

public class GroupRecord implements Serializable {

  public static final String TYPE_PRIVATE = "private";
  public static final String TYPE_PUBLIC = "public";
  public static final String STATUS_ACTIVE = "active";
  public static final String STATUS_DELETED = "deleted";
  public static final String STATUS_FROZEN = "frozen";
  public static final String STATUS_DEACTIVATED = "deactivated";
  private String groupId;
  private String tenantId = "";
  private String threadId = "";
  private String name = "";
  private String groupType = "";
  private String loginType = "";
  private String groupPic = "";
  private String groupThumb = "";
  private String groupDesc = "";
  private long loginTimestamp;
  private List<UserRecord> groupUsers;
  private boolean isNotInGroup = false;
  private int participantsCount = 0;
  private String groupStatus = "";

  public GroupRecord(
      String groupName, String groupDesc, String groupPicPath, List<UserRecord> groupMembers) {
    this.name = groupName;
    this.groupDesc = groupDesc;
    this.groupPic = groupPicPath;
    this.groupUsers = groupMembers;
  }
  public GroupRecord(){

  }
  public GroupRecord(
      String groupId,
      String tenantId,
      String name,
      String loginType,
      String groupPic,
      String groupThumb,
      String groupDesc,
      long loginTimestamp,
      List<UserRecord> groupUsers,
      String groupType,
      String threadId,
      boolean isNotInGroup,
      int participantsCount,
      String groupStatus
  ) {
    this.groupId = groupId;
    this.tenantId = tenantId;
    this.name = name;
    this.loginType = loginType;
    this.groupPic = groupPic;
    this.groupThumb = groupThumb;
    this.groupDesc = groupDesc;
    this.loginTimestamp = loginTimestamp;
    this.groupUsers = groupUsers;
    this.groupType = groupType;
    this.threadId = threadId;
    this.isNotInGroup = isNotInGroup;
    this.participantsCount = participantsCount;
    this.groupStatus = groupStatus;
  }

  public String getGroupId() {
    return groupId;
  }

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

  public List<UserRecord> getGroupUsers() {
    return groupUsers;
  }

  public String getGroupType() {
    return groupType;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGroupType(String groupType) {
    this.groupType = groupType;
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

  public void setGroupUsers(List<UserRecord> groupUsers) {
    this.groupUsers = groupUsers;
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public boolean isNotInGroup() {
    return isNotInGroup;
  }

  public void setNotInGroup(boolean notInGroup) {
    isNotInGroup = notInGroup;
  }

  public int getParticipantsCount() {
    return participantsCount;
  }

  public void setParticipantsCount(int participantsCount) {
    this.participantsCount = participantsCount;
  }

  public String getGroupStatus() {
    return groupStatus;
  }

  public void setGroupStatus(String groupStatus) {
    this.groupStatus = groupStatus;
  }
}
