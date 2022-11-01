package com.ripbull.coresdk.core.type;

public enum AvailabilityStatus {
  AUTO("auto"),

  ONLINE("online"),
  AWAY("away"),
  INVISIBLE("invisible"),
  DND("dnd");

  private String status;

  AvailabilityStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
