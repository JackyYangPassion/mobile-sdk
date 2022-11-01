package com.ripbull.coresdk.data;

;

import com.ripbull.ertc.cache.database.DataSource;
import com.ripbull.ertc.cache.preference.PreferenceManager;
import com.ripbull.ertc.mqtt.MqttManager;
import com.ripbull.ertc.remote.NetworkConfig;
import com.ripbull.ertc.remote.NetworkManager;

/** Created by DK on 24/11/18. */

public interface DataManager {
  NetworkManager network();

  MqttManager mqtt();

  DataSource db();

  PreferenceManager preference();

  NetworkConfig networkConfig();
}
