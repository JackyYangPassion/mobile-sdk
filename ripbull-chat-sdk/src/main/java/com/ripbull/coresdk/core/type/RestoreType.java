package com.ripbull.coresdk.core.type;

public enum RestoreType {
  THREAD("thread"),
  CHAT("chat");

  private String status;

  RestoreType(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
