package io.inappchat.inappchat.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

;

import io.inappchat.inappchat.mqtt.listener.ActionListener;
import io.inappchat.inappchat.mqtt.listener.MqttCallbackHandler;
import io.inappchat.inappchat.mqtt.listener.MqttEventHandler;
import io.inappchat.inappchat.mqtt.listener.MqttTraceCallback;
import io.inappchat.inappchat.mqtt.model.Subscription;
import io.inappchat.inappchat.mqtt.utils.Constants;
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
