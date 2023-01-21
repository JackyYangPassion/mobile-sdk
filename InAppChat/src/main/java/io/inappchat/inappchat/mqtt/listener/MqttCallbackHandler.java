package io.inappchat.inappchat.mqtt.listener;

import static io.inappchat.inappchat.mqtt.utils.Constants.TAG;

import android.content.Context;
import android.util.Log;
import io.inappchat.inappchat.mqtt.Connection;
import io.inappchat.inappchat.mqtt.Connections;
import io.inappchat.inappchat.mqtt.model.MqttMessageImpl;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements org.eclipse.paho.client.mqttv3.MqttCallback {

  private Context context;
  private String clientHandle;
  private MqttEventHandler callback;

  public MqttCallbackHandler(Context context, String clientHandle, MqttEventHandler callback) {
    this.context = context;
    this.clientHandle = clientHandle;
    this.callback = callback;
  }

  @Override
  public void connectionLost(Throwable cause) {
    if (cause != null) {
      Log.d(TAG, "Connection Lost: " + cause.getMessage());
      Connection c = Connections.getInstance(context).getConnection(clientHandle);
      if (c == null) return;
      c.addAction("Connection Lost");
      c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
      if (callback != null) {
        callback.connectionLost(cause);
      }
    }
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {

    // Get connection object associated with this object
    Connection c = Connections.getInstance(context).getConnection(clientHandle);
    c.messageArrived(topic, message);
    // get the string from strings.xml and format

    String messageString = new String(message.getPayload())
        + topic
        + ";qos:"
        + message.getQos()
        + ";retained:"
        + message.isRetained();

    //Log.i(TAG, messageString);

    // update client history
    c.addAction(messageString);
    if (callback != null) {
      callback.messageFromMQTT(topic, new MqttMessageImpl(topic, message));
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // Do nothing
  }
}
