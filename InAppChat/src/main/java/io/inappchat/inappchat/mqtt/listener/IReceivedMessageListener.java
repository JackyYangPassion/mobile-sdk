package io.inappchat.inappchat.mqtt.listener;

public interface IReceivedMessageListener {
  void onMessageReceived(ReceivedMessage message);
}
