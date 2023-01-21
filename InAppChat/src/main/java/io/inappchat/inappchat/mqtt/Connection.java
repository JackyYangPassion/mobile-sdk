package io.inappchat.inappchat.mqtt;

import android.content.Context;
import android.text.TextUtils;

import io.inappchat.inappchat.R;
import io.inappchat.inappchat.mqtt.model.Subscription;
import io.inappchat.inappchat.mqtt.service.MqttAndroidClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Connection {

  private String clientHandle = null;

  private String clientId = null;

  private String host = null;

  // private int port = 0;

  private ConnectionStatus status = ConnectionStatus.NONE;

  private MqttAndroidClient client = null;

  private Context context = null;

  private MqttConnectOptions mqttConnectOptions;

  private boolean tlsConnection = true;

  private long persistenceId = -1;

  private final Map<String, Subscription> subscriptions = new HashMap<String, Subscription>();

  public enum ConnectionStatus {
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    DISCONNECTED,
    ERROR,
    NONE
  }

  public static Connection createConnection(String clientHandle, String clientId, String host,
      Context context, boolean tlsConnection) {
    String uri = host;
    if (!TextUtils.isEmpty(host) && !host.startsWith("wss://")) {
      host = host.replace("tcp://", "").replace("http://", "").replace("https://", "");
      uri = "wss://" + host;
    }
    /*String uri;
    if (tlsConnection) {
      uri = "ssl://" + host + ":" + port;
    } else {
      uri = "tcp://" + host + ":" + port;
    }*/

    MqttAndroidClient client = new MqttAndroidClient(context, uri, clientId);
    return new Connection(clientHandle, clientId, uri, context, client, tlsConnection);
  }

  void updateConnection(String clientId, String host, boolean tlsConnection) {
    String uri = host;
    if (!TextUtils.isEmpty(host) && !host.startsWith("wss://")) {
      host = host.replace("tcp://", "").replace("http://", "").replace("https://", "");
      uri = "wss://" + host;
    }
    /*String uri;
    if (tlsConnection) {
      uri = "ssl://" + host + ":" + port;
    } else {
      uri = "tcp://" + host + ":" + port;
    }*/

    this.clientId = clientId;
    this.host = uri;
    // this.port = port;
    this.tlsConnection = tlsConnection;
    this.client = new MqttAndroidClient(context, uri, clientId);
  }

  private Connection(String clientHandle, String clientId, String host,
      // int port,
      Context context, MqttAndroidClient client, boolean tlsConnection) {
    // generate the client handle from its hash code
    this.clientHandle = clientHandle;
    this.clientId = clientId;
    this.host = host;
    // this.port = port;
    this.context = context;
    this.client = client;
    this.tlsConnection = tlsConnection;
    String sb = "Client: " + clientId + " created";
    addAction(sb);
  }

  public void addAction(String action) {

    Object[] args = new String[1];
    DateFormat dateTimeFormatter =
        SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    args[0] = dateTimeFormatter.format(new Date());
  }

  public String handle() {
    return clientHandle;
  }

  boolean isConnected() {
    return status == ConnectionStatus.CONNECTED;
  }

  public void changeConnectionStatus(ConnectionStatus connectionStatus) {
    status = connectionStatus;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(clientId);
    sb.append("\n ");

    switch (status) {
      case CONNECTED:
        sb.append(context.getString(R.string.connection_connected_to));
        break;
      case DISCONNECTED:
        sb.append(context.getString(R.string.connection_disconnected_from));
        break;
      case NONE:
        sb.append(context.getString(R.string.connection_unknown_status));
        break;
      case CONNECTING:
        sb.append(context.getString(R.string.connection_connecting_to));
        break;
      case DISCONNECTING:
        sb.append(context.getString(R.string.connection_disconnecting_from));
        break;
      case ERROR:
        sb.append(context.getString(R.string.connection_error_connecting_to));
    }
    sb.append(" ");
    sb.append(host);

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Connection)) {
      return false;
    }

    Connection c = (Connection) o;

    return clientHandle.equals(c.clientHandle);
  }

  public String getId() {
    return clientId;
  }

  String getHostName() {

    return host;
  }

  public MqttAndroidClient getClient() {
    return client;
  }

  void addConnectionOptions(MqttConnectOptions connectOptions) {
    mqttConnectOptions = connectOptions;
  }

  MqttConnectOptions getConnectionOptions() {
    return mqttConnectOptions;
  }

  /*int getPort() {
    return port;
  }*/

  /**
   * Determines if the connection is secured using SSL, returning a C style integer value
   *
   * @return 1 if SSL secured 0 if plain text
   */
  int isSSL() {
    return tlsConnection ? 1 : 0;
  }

  void assignPersistenceId(long id) {
    persistenceId = id;
  }

  long persistenceId() {
    return persistenceId;
  }

  void addNewSubscription(Context context, Subscription subscription) throws MqttException {
    if (!subscriptions.containsKey(subscription.getTopic())) {
      try {
        String[] actionArgs = new String[1];
        actionArgs[0] = subscription.getTopic();
        // final ActionListener callback =
        //    new ActionListener(context,ActionListener.Action.SUBSCRIBE, this, actionArgs);
        // this.getClient().subscribe(subscription.getTopic(), subscription.getQos(), null,
        // callback);

        Persistence persistence = new Persistence(context);
        long rowId = persistence.persistSubscription(subscription);
        subscription.setPersistenceId(rowId);
        subscriptions.put(subscription.getTopic(), subscription);
      } catch (PersistenceException pe) {
        throw new MqttException(pe);
      }
    }
  }

  void unsubscribe(Subscription subscription) throws MqttException {
    if (subscriptions.containsKey(subscription.getTopic())) {
      this.getClient().unsubscribe(subscription.getTopic());
      subscriptions.remove(subscription.getTopic());
      Persistence persistence = new Persistence(context);
      persistence.deleteSubscription(subscription);
    }
  }

  void deleteSubscription() {
    for (Subscription subscription : getSubscriptions()) {
      try {
        unsubscribe(subscription);
      } catch (MqttException e) {
        e.printStackTrace();
      }
    }
  }

  void setSubscriptions(ArrayList<Subscription> newSubs) {
    for (Subscription sub : newSubs) {
      subscriptions.put(sub.getTopic(), sub);
    }
  }

  public ArrayList<Subscription> getSubscriptions() {
    ArrayList<Subscription> subs = new ArrayList<Subscription>();
    subs.addAll(subscriptions.values());
    return subs;
  }

  public void messageArrived(String topic, MqttMessage message) {
    if (subscriptions.containsKey(topic)) {
      Objects.requireNonNull(subscriptions.get(topic))
          .setLastMessage(new String(message.getPayload()));
    }
  }
}
