package com.ripbull.ertc.mqtt.listener;

/** Created by DK on 16/03/19. */
public interface MqttEventHandler {
  void connectionLost(Throwable cause);

  void messageFromMQTT(String topic, ReceivedMessage message) throws Exception;
}
