package com.ripbull.ertc.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

;

import com.ripbull.ertc.mqtt.listener.ActionListener;
import com.ripbull.ertc.mqtt.listener.MqttCallbackHandler;
import com.ripbull.ertc.mqtt.listener.MqttEventHandler;
import com.ripbull.ertc.mqtt.listener.MqttTraceCallback;
import com.ripbull.ertc.mqtt.model.Subscription;
import com.ripbull.ertc.mqtt.utils.Constants;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/** Created by DK on 15/03/19. */

public interface MqttManager {

  void setEventHandler(MqttEventHandler eventHandler);

  void setMqttServer(String mqttServer);

  MqttEventHandler getEventHandler();

  void setConnection(MqttEventHandler eventHandler);

  void createConnection(String tenantId, String chatUserId, String mqttServer,
      String userName, String password, String deviceId);

  void subscribe(ConnectionModel connectionModel, Connection connection, String topic);

  void unsubscribe(Subscription subscription);

  void publish(String topic, String message);

  void removeConnectionAndSubscription();

  void removeSubscription();

}
