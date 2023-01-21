package io.inappchat.inappchat.mqtt;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import io.inappchat.inappchat.mqtt.listener.ActionListener;
import io.inappchat.inappchat.mqtt.listener.MqttCallbackHandler;
import io.inappchat.inappchat.mqtt.listener.MqttEventHandler;
import io.inappchat.inappchat.mqtt.listener.MqttTraceCallback;
import io.inappchat.inappchat.mqtt.model.Subscription;
import io.inappchat.inappchat.mqtt.utils.Constants;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Map;

;

/** Created by DK on 15/03/19. */

public class MqttManagerImpl implements MqttManager{

  private Context context;
  private String mqttServer;
  private MqttEventHandler eventHandler;

  public MqttManagerImpl(Context context, String mqttServer) {
    this.context = context;
    this.mqttServer = mqttServer;
  }

  public void setEventHandler(MqttEventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  public void setMqttServer(String mqttServer) {
    this.mqttServer = mqttServer;
  }

  public MqttEventHandler getEventHandler() {
    return eventHandler;
  }

  public void setConnection(MqttEventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  public void createConnection(String tenantId, String chatUserId, String mqttServer,
      String userName, String password, String deviceId) {

    Map<String, Connection> connections = Connections.getInstance(context).getConnections();
    if (connections.size() > 0) {
      Log.d(Constants.TAG, " **** Updating connection: " + connections.keySet().toString());
      for (Map.Entry<String, Connection> entry : connections.entrySet()) {
        updateAndConnect(new ConnectionModel(entry.getValue()), entry.getValue());
      }
    } else {
      Log.d(Constants.TAG, " **** Creating the connection: ...");
      ConnectionModel model = new ConnectionModel();
      String clientId = chatUserId + ":" + deviceId + ":android";
      model.setClientId(clientId);
      model.setServerHostName(mqttServer);
      model.setUsername(userName);
      model.setPassword(password);
      model.setLwtQos(0);
      model.setLwtTopic("disconnect/clients");
      model.setLwtMessage(clientId);
      Connection connection =
          Connection.createConnection(model.getClientHandle(), model.getClientId(),
              model.getServerHostName(), context, model.isTlsConnection());
      createAndConnect(model, connection);
      subscribe(model, connection, Constants.CHAT_TOPIC + clientId);
      subscribe(model, connection, Constants.CHAT_UPDATE_TOPIC + clientId);
      subscribe(model, connection, Constants.MSG_READ_STATUS_TOPIC + ":" + clientId);
      subscribe(model, connection, Constants.TYPING_STATUS_TOPIC + ":" + clientId);
      subscribe(model, connection, Constants.AVAILABILITY_STATUS_TOPIC + ":" + clientId);
      subscribe(model, connection, Constants.CHAT_REACTION + clientId);
      subscribe(model, connection, Constants.GROUP_UPDATED + ":" + clientId);
      subscribe(model, connection, Constants.USER_SELF_UPDATE + ":" + clientId);
      subscribe(model, connection, Constants.ANNOUNCEMENT + ":" + clientId);
      subscribe(model, connection, Constants.TENANT_CONFIG_MODIFIED + ":" + clientId);
      subscribe(model, connection, Constants.USER_DB_UPDATED + ":" + clientId);
      subscribe(model, connection, Constants.CHAT_HISTORY_CLEARED + ":" + clientId);
      subscribe(model, connection, Constants.CHAT_SETTING_UPDATED + ":" + clientId);
    }
  }

  private void createAndConnect(ConnectionModel model, Connection connection) {
    Log.d(Constants.TAG, "Persisting new connection:" + model.getClientHandle());

    connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);
    String[] actionArgs = new String[1];
    actionArgs[0] = model.getClientId();
    final ActionListener callback =
        new ActionListener(context, ActionListener.Action.CONNECT, connection, actionArgs);
    connection.getClient()
        .setCallback(new MqttCallbackHandler(context, model.getClientHandle(), eventHandler));

    connection.getClient().setTraceCallback(new MqttTraceCallback());

    MqttConnectOptions connOpts = optionsFromModel(model);

    connection.addConnectionOptions(connOpts);
    Connections.getInstance(context).addConnection(connection);
    try {
      connection.getClient().connect(connOpts, null, callback);
    } catch (MqttException e) {
      Log.e(Constants.TAG, "MqttException occurred", e);
    }
  }

  private void updateAndConnect(ConnectionModel model, Connection connection) {

    try {
      // First disconnect the current instance of this connection
      if (connection.isConnected()) {
        connection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTING);
        connection.getClient().disconnect();
      }
      // Update the connection.
      connection.updateConnection(model.getClientId(), model.getServerHostName(),
          //model.getServerPort(),
          model.isTlsConnection());
      connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);

      String[] actionArgs = new String[1];
      actionArgs[0] = model.getClientId();
      final ActionListener callback =
          new ActionListener(context, ActionListener.Action.CONNECT, connection, actionArgs);
      connection.getClient()
          .setCallback(new MqttCallbackHandler(context, model.getClientHandle(), eventHandler));

      connection.getClient().setTraceCallback(new MqttTraceCallback());
      MqttConnectOptions connOpts = optionsFromModel(model);
      connection.addConnectionOptions(connOpts);
      Connections.getInstance(context).updateConnection(connection);
      if (!connection.getClient().isConnected()) {
        connection.getClient().connect(connOpts, null, callback);
      }
    } catch (MqttException ex) {
      Log.e(Constants.TAG, "Exception occurred updating connection: " + ex.getMessage());
    }
  }

  private MqttConnectOptions optionsFromModel(ConnectionModel model) {

    Log.i(Constants.TAG, model.toString());
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(false);
    //connOpts.setConnectionTimeout(90);
    //connOpts.setKeepAliveInterval(200);
    connOpts.setAutomaticReconnect(true);

    //connOpts.setExecutorServiceTimeout(90);

    if (!TextUtils.isEmpty(model.getUsername())) {
      connOpts.setUserName(model.getUsername());
    }

    if (!TextUtils.isEmpty(model.getPassword())) {
      connOpts.setPassword(model.getPassword().toCharArray());
    }
    if (!TextUtils.isEmpty(model.getLwtTopic()) && !TextUtils.isEmpty(model.getLwtMessage())) {
      connOpts.setWill(model.getLwtTopic(), model.getLwtMessage().getBytes(), model.getLwtQos(),
          model.isLwtRetain());
    }
    //   if(tlsConnection){
    //       // TODO Add Keys to conOpts here
    //       //connOpts.setSocketFactory();
    //   }
    return connOpts;
  }

  public void subscribe(ConnectionModel connectionModel, Connection connection, String topic) {
    try {
      connection.addNewSubscription(context, new Subscription(topic, 2, connection.handle()));
    } catch (MqttException e) {
      e.printStackTrace();
    }
  }

  public void unsubscribe(Subscription subscription) {
    try {
      Connection connection =
          Connections.getInstance(context).getConnection(new ConnectionModel().getClientHandle());
      connection.unsubscribe(subscription);
      System.out.println("Unsubscribed from: " + subscription.toString());
    } catch (MqttException ex) {
      System.out.println(
          "Failed to unsubscribe from " + subscription.toString() + ". " + ex.getMessage());
    }
  }

  public void publish(String topic, String message) {

    Map<String, Connection> connections = Connections.getInstance(context).getConnections();
    for (Map.Entry<String, Connection> entry : connections.entrySet()) {
      Connection connection = entry.getValue();
      try {
        String[] actionArgs = new String[2];
        actionArgs[0] = message;
        actionArgs[1] = topic;
        final ActionListener callback =
            new ActionListener(context, ActionListener.Action.PUBLISH, connection, actionArgs);
        connection.getClient().publish(topic, message.getBytes(), 2, true, null, callback);
        break;
      } catch (MqttException ex) {
        Log.e(Constants.TAG, "Exception occurred during publish: " + ex.getMessage());
      }

      /*ArrayList<Subscription> subscriptions = connection.getSubscriptions();
      for(Subscription subscription : subscriptions){
        String topic = subscription.getTopic();
        int qos = subscription.getQos();
        if(topic.contains(topicId)){

        }
      }*/
    }
  }

  public void removeConnectionAndSubscription() {

    Map<String, Connection> connections = Connections.getInstance(context).getConnections();
    for (Map.Entry<String, Connection> entry : connections.entrySet()) {
      Connection connection = entry.getValue();
      Connections.getInstance(context).removeConnection(connection);
      connection.deleteSubscription();
      if (connection.isConnected()) {
        connection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTING);
        try {
          connection.getClient().disconnect();
        } catch (MqttException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void removeSubscription() {
  }
}
