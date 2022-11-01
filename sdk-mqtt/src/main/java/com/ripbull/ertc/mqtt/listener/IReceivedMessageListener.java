package com.ripbull.ertc.mqtt.listener;

public interface IReceivedMessageListener {
  void onMessageReceived(ReceivedMessage message);
}
