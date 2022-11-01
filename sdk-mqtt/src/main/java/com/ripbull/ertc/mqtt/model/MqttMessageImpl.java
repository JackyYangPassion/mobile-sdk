package com.ripbull.ertc.mqtt.model;

import com.ripbull.ertc.mqtt.listener.ReceivedMessage;
import java.util.Date;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageImpl implements ReceivedMessage {

  public MqttMessageImpl(String topic, MqttMessage mqttMessage) {
    this.topic = topic;
    this.message = new String(mqttMessage.getPayload());
    this.timestamp = new Date();
  }

  private final String topic;
  private final String message;
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
    return timestamp;
  }

  @Override
  public String tile() {
    return null;
  }

  @Override
  public String body() {
    return null;
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
