package io.inappchat.inappchat.core.type;

/** Created by DK on 25/11/18. */
public enum MessageType {
  TEXT("text"),
  CONTACT("contact"),
  LOCATION("location"),
  IMAGE("image"),
  AUDIO("audio"),
  VIDEO("video"),
  STICKER("sticker"),
  FILE("file"),
  SMILE("smile"),
  GIPHY("gify"),
  GIF("gif");

  private String type;

  MessageType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
