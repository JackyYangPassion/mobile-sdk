package com.ripbull.ertc.mqtt.listener;

import android.content.Context;
import android.util.Log;
import com.ripbull.ertc.mqtt.Connection;
import com.ripbull.ertc.mqtt.Connections;
import com.ripbull.ertc.mqtt.model.Subscription;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ActionListener implements IMqttActionListener {

  private static final String TAG = "ActionListener";

  public enum Action {
    /** Connect Action */
    CONNECT,
    /** Disconnect Action */
    DISCONNECT,
    /** Subscribe Action */
    SUBSCRIBE,
    /** Publish Action */
    PUBLISH
  }

  private Context context;
  private final Action action;
  private final String[] additionalArgs;
  private final Connection connection;
  private final String clientHandle;

  public ActionListener(Context context, Action action, Connection connection,
      String... additionalArgs) {
    this.context = context;
    this.action = action;
    this.connection = connection;
    this.clientHandle = connection.handle();
    this.additionalArgs = additionalArgs;
  }

  @Override
  public void onSuccess(IMqttToken asyncActionToken) {
    switch (action) {
      case CONNECT:
        connect();
        break;
      case DISCONNECT:
        disconnect();
        break;
      case SUBSCRIBE:
        subscribe();
        break;
      case PUBLISH:
        publish();
        break;
    }
  }

  private void publish() {
    System.out.print("Published");
  }

  private void subscribe() {
    System.out.print("Subscribed");
  }

  private void disconnect() {
    System.out.print("Disconnect");
  }

  private void connect() {
    System.out.print("Connected");
    Connection c = Connections.getInstance(context).getConnection(clientHandle);
    c.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED);
    c.addAction("Client Connected");
    Log.i(TAG, c.handle() + " connected.");
    try {

      ArrayList<Subscription> subscriptions = connection.getSubscriptions();
      for (Subscription sub : subscriptions) {
        Log.i(TAG, "Auto-subscribing to: " + sub.getTopic() + "@ QoS: " + sub.getQos());
        connection.getClient().subscribe(sub.getTopic(), sub.getQos());
      }
    } catch (MqttException ex) {
      Log.e(TAG, "Failed to Auto-Subscribe: " + ex.getMessage());
    }
  }

  @Override
  public void onFailure(IMqttToken token, Throwable exception) {
    switch (action) {
      case CONNECT:
        connect(exception);
        break;
      case DISCONNECT:
        disconnect(exception);
        break;
      case SUBSCRIBE:
        subscribe(exception);
        break;
      case PUBLISH:
        publish(exception);
        break;
    }
  }

  private void publish(Throwable exception) {
    if (exception != null) System.out.print("Publish failed : " + exception.getLocalizedMessage());
  }

  private void subscribe(Throwable exception) {
    if (exception != null) {
      System.out.print("Subscription failed : " + exception.getLocalizedMessage());
    }
  }

  private void disconnect(Throwable exception) {
    System.out.println("Disconnect Failed - an error occured");
    Connection c = Connections.getInstance(context).getConnection(clientHandle);
    c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
    c.addAction("Disconnect Failed - an error occured");
  }

  private void connect(Throwable exception) {
    System.out.println("Client failed to connect");
    Connection c = Connections.getInstance(context).getConnection(clientHandle);
    if (c != null) {
      c.changeConnectionStatus(Connection.ConnectionStatus.ERROR);
      c.addAction("Client failed to connect");
      System.out.println("Client failed to connect");
    }
  }
}
