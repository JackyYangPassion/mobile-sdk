package com.ripbull.coresdk.core.type;

/** Created by DK on 25/11/18. */
public enum ChatType {
  SINGLE("single"),
  GROUP("group"),
  CHAT_THREAD("chat_thread"),
  SINGLE_CHAT_THREAD("single_chat_thread"),
  GROUP_CHAT_THREAD("group_chat_thread");

  private String type;

  ChatType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
