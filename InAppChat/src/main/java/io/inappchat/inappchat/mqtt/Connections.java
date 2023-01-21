package io.inappchat.inappchat.mqtt;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connections {

  private static Connections instance = null;

  private HashMap<String, Connection> connections = null;

  private Persistence persistence = null;

  private Connections(Context context) {
    connections = new HashMap<String, Connection>();

    // If there is state, attempt to restore it
    persistence = new Persistence(context);
    try {
      List<Connection> connectionList = persistence.restoreConnections(context);
      for (Connection connection : connectionList) {
        System.out.println("Connection was persisted.." + connection.handle());
        connections.put(connection.handle(), connection);
      }
    } catch (PersistenceException e) {
      e.printStackTrace();
    }
  }

  public static synchronized Connections getInstance(Context context) {
    if (instance == null) {
      instance = new Connections(context);
    }
    return instance;
  }

  public Connection getConnection(String handle) {
    return connections.get(handle);
  }

  public void addConnection(Connection connection) {
    connections.put(connection.handle(), connection);
    try {
      persistence.persistConnection(connection);
    } catch (PersistenceException e) {
      // @todo Handle this error more appropriately
      // error persisting well lets just swallow this
      e.printStackTrace();
    }
  }

  public Map<String, Connection> getConnections() {
    return connections;
  }

  public void removeConnection(Connection connection) {
    connections.remove(connection.handle());
    persistence.deleteConnection(connection);
  }

  public void updateConnection(Connection connection) {
    connections.put(connection.handle(), connection);
    persistence.updateConnection(connection);
  }
}
