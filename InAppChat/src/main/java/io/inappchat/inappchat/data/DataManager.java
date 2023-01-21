package io.inappchat.inappchat.data;

;

import io.inappchat.inappchat.cache.database.DataSource;
import io.inappchat.inappchat.cache.preference.PreferenceManager;
import io.inappchat.inappchat.mqtt.MqttManager;
import io.inappchat.inappchat.remote.NetworkConfig;
import io.inappchat.inappchat.remote.NetworkManager;

/** Created by DK on 24/11/18. */

public interface DataManager {
  NetworkManager network();

  MqttManager mqtt();

  DataSource db();

  PreferenceManager preference();

  NetworkConfig networkConfig();
}
