package com.ripbull.coresdk.typing.mapper;



/** Created by DK on 06/04/19. */

public class TypingIndicatorRecord {
  private String threadId;
  private String typingStatus;
  private String type;
  private String userId;
  private String name;

  public TypingIndicatorRecord(String threadId,String type, String typingStatus, String userId, String name) {
    this.threadId = threadId;
    this.type = type;
    this.typingStatus = typingStatus;
    this.userId = userId;
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public String getThreadId() {
    return threadId;
  }

  public String getTypingStatus() {
    return typingStatus;
  }

  public String getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }
}
