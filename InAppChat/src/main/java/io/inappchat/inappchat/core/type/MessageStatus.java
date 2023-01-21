package io.inappchat.inappchat.core.type;

/** Created by DK on 25/11/18. */
public enum MessageStatus {
  NONE("none"),
  SENDING("Sending"),
  UPLOADING("uploading"),
  SENT("Sent"),
  DELIVERED("delivered"),
  SEEN("seen"),
  FAILED("Failed");

  private String status;

  MessageStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
