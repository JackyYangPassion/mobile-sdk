package com.ripbull.coresdk.fcm;

import com.ripbull.ertc.mqtt.listener.ReceivedMessage;

import java.util.Date;

public class FcmMessageImpl implements ReceivedMessage {

  FcmMessageImpl(String topic, String fcmMessage, String title, String body) {
    this.topic = topic;
    this.message = fcmMessage;
    this.title = title;
    this.body = body;
    this.timestamp = new Date();
  }

  private final String topic;
  private final String message;
  private final String title;
  private final String body;
  private final Date timestamp;

  @Override
  public String getTopic() {
    return topic;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public Date getTimestamp() {
    return this.timestamp;
  }

  @Override
  public String tile() {
    return this.title;
  }

  @Override
  public String body() {
    return this.body;
  }

  @Override
  public String toString() {
    return "ReceivedMessage{"
        + "topic='"
        + topic
        + '\''
        + ", message="
        + message
        + ", timestamp="
        + timestamp
        + '}';
  }
}
