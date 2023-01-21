package io.inappchat.inappchat.data;

import android.content.Context;

import io.inappchat.inappchat.cache.database.DataSource;
import io.inappchat.inappchat.cache.database.DataSourceImpl;
import io.inappchat.inappchat.cache.preference.PreferenceManager;
import io.inappchat.inappchat.cache.preference.PreferenceManagerImpl;
import io.inappchat.inappchat.mqtt.MqttManagerImpl;
import io.inappchat.inappchat.remote.NetworkConfig;
import io.inappchat.inappchat.remote.NetworkManager;
import io.inappchat.inappchat.remote.NetworkManagerImpl;
import io.inappchat.inappchat.remote.TokenChangeListener;
import io.inappchat.inappchat.BuildConfig;
import io.inappchat.inappchat.mqtt.MqttManager;

/** Created by DK on 24/11/18. */
public class DataManagerImpl implements DataManager {

  private final DataSource databaseManager;
  private final NetworkManager networkManager;
  private final PreferenceManager preferenceManager;
  private final MqttManager mqttManager;
  private NetworkConfig networkConfig;

  private static DataManager instance;

  public static DataManager shared(Context context) {
    if (instance == null) {
      synchronized (DataManagerImpl.class) {
        if (instance == null) {
          DataSource databaseManager = DataSourceImpl.instance(context);
          PreferenceManager preferenceManager =
              new PreferenceManagerImpl(context.getApplicationContext());
          NetworkManager networkManager =
              new NetworkManagerImpl();
          MqttManager mqttManager = new MqttManagerImpl(context.getApplicationContext(),preferenceManager.getMqttServer());
          instance =
              new DataManagerImpl(databaseManager, networkManager, preferenceManager, mqttManager);
        }
      }
    }
    return instance;
  }

  private DataManagerImpl(
      DataSource databaseManager,
      NetworkManager networkManager,
      PreferenceManager preferenceManager,
      MqttManager mqttManager) {
    this.databaseManager = databaseManager;
    this.networkManager = networkManager;
    this.preferenceManager = preferenceManager;
    this.mqttManager = mqttManager;
    this.networkConfig = getNetworkConfig();
  }

  @Override
  public DataSource db() {
    return databaseManager;
  }

  @Override
  public NetworkManager network() {
    return networkManager;
  }

  @Override
  public PreferenceManager preference() {
    return preferenceManager;
  }

  @Override
  public NetworkConfig networkConfig() {
    networkConfig = null;
    networkConfig = getNetworkConfig();
    return networkConfig;
  }

  @Override
  public MqttManager mqtt() {
    return mqttManager;
  }

  private NetworkConfig getNetworkConfig(){
    return new NetworkConfig.Builder()
            .packageName(preferenceManager.getPackageName())
            .authApiKey(preferenceManager.getApiKey())
            .authUrl(BuildConfig.BASE_URL)
            .chatApiKey(preferenceManager.getChatApiKey())
            .chatUrl(preferenceManager.getChatServer())
            .chatToken(preferenceManager.getChatToken())
            .chatRefreshToken(preferenceManager.getChatRefreshToken())
            .userApiKey(preferenceManager.getUserApiKey())
            .userRefreshToken(preferenceManager.getUserRefreshToken())
            .userToken(preferenceManager.getUserToken())
            .userUrl(preferenceManager.getUserServer())
            .chatUserId(preferenceManager.getChatUserId())
            .userId(preferenceManager.getUserId())
            .tenantId(preferenceManager.getTenantId())
            .deviceId(preferenceManager.getDeviceId())
            .tokenChangeListener(new TokenChangeListener() {
              @Override
              public void onChatTokenChanged(String token, String refreshToken) {
                preferenceManager.setChatToken(token);
                preferenceManager.setChatRefreshToken(refreshToken);
              }

              @Override
              public void onUserTokenChanged(String token, String refreshToken) {
                preferenceManager.setUserToken(token);
                preferenceManager.setUserRefreshToken(refreshToken);
              }
            })
            .build();
  }
}
