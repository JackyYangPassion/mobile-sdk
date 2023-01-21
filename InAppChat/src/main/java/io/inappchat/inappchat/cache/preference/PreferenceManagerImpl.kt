package io.inappchat.inappchat.cache.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author meeth
 */
class PreferenceManagerImpl(
  context: Context
) : PreferenceManager {

  private val sharedPreferences = context.getSharedPreferences("", MODE_PRIVATE)

  //Tenant Info
  override var tenantId: String? by StringPreference(sharedPreferences, PREF_TENANT_KEY, "")
  override var userId: String? by StringPreference(sharedPreferences, PREF_USER_ID, "")
  override var name: String? by StringPreference(sharedPreferences, PREF_NAME, "")
  override var loginType: String? by StringPreference(sharedPreferences, PREF_LOGIN_TYPE, "")
  override var userServer: String? by StringPreference(sharedPreferences, PREF_USER_SERVER, "http://google.com")
  override var chatServer: String? by StringPreference(sharedPreferences, PREF_CHAT_SERVER, "http://google.com")
  override var mqttServer: String? by StringPreference(sharedPreferences, PREF_MQTT_SERVER, "")
  override var chatUserId: String? by StringPreference(sharedPreferences, PREF_CHAT_USER_ID, "")
  override var apiKey: String? by StringPreference(sharedPreferences, PREF_API_KEY, "")
  override var chatApiKey: String? by StringPreference(sharedPreferences, PREF_CHAT_API_KEY, "")
  override var userApiKey: String? by StringPreference(sharedPreferences, PREF_USER_API_KEY, "")
  override var mqttApiKey: String? by StringPreference(sharedPreferences, PREF_MQTT_API_KEY, "")
  override var appUserId: String? by StringPreference(sharedPreferences, PREF_APP_USER_ID, "")
  override var fcmToken: String? by StringPreference(sharedPreferences, PREF_FCM_TOKEN, "")
  override var deviceId: String? by StringPreference(sharedPreferences, PREF_DEVICE_ID, "")
  override var packageName: String? by StringPreference(sharedPreferences, PREF_PACKAGE_NAME, "")
  override var chatToken: String? by StringPreference(sharedPreferences, PREF_CHAT_TOKEN, "")
  override var chatRefreshToken: String? by StringPreference(
    sharedPreferences,
    PREF_CHAT_REFRESH_TOKEN,
    ""
  )
  override var userToken: String? by StringPreference(sharedPreferences, PREF_USER_TOKEN, "")
  override var userRefreshToken: String? by StringPreference(
    sharedPreferences,
    PREF_USER_REFRESH_TOKEN,
    ""
  )
  override var lastCallTimeUser: String? by StringPreference(
    sharedPreferences,
    PREF_LAST_CALL_TIME_USER,
    ""
  )
  override var skipRestoreFlag: Boolean by BooleanPreference(sharedPreferences, PREF_SKIP_RESTORE_FLAG, true)
  override var projectType: String? by StringPreference(
    sharedPreferences,
    PREF_PROJECT_TYPE,
    "freesampleapp"
  )

  companion object {
    const val PREF_TENANT_KEY = "PREF_TENANT_KEY"
    const val PREF_USER_ID = "PREF_USER_ID"
    const val PREF_NAME = "PREF_NAME"
    const val PREF_LOGIN_TYPE = "PREF_LOGIN_TYPE"
    const val PREF_USER_SERVER = "PREF_USER_SERVER"
    const val PREF_CHAT_SERVER = "PREF_CHAT_SERVER"
    const val PREF_MQTT_SERVER = "PREF_MQTT_SERVER"
    const val PREF_CHAT_USER_ID = "PREF_CHAT_USER_ID"
    const val PREF_API_KEY = "PREF_API_KEY"
    const val PREF_CHAT_API_KEY = "PREF_CHAT_API_KEY"
    const val PREF_USER_API_KEY = "PREF_USER_API_KEY"
    const val PREF_MQTT_API_KEY = "PREF_MQTT_API_KEY"
    const val PREF_APP_USER_ID = "PREF_APP_USER_ID"
    const val PREF_FCM_TOKEN = "PREF_FCM_TOKEN"
    const val PREF_DEVICE_ID = "PREF_DEVICE_ID"
    const val PREF_PACKAGE_NAME = "PREF_PACKAGE_NAME"
    const val PREF_CHAT_TOKEN = "PREF_CHAT_TOKEN"
    const val PREF_CHAT_REFRESH_TOKEN = "PREF_CHAT_REFRESH_TOKEN"
    const val PREF_USER_TOKEN = "PREF_USER_TOKEN"
    const val PREF_USER_REFRESH_TOKEN = "PREF_USER_REFRESH_TOKEN"
    const val PREF_LAST_CALL_TIME_USER = "PREF_LAST_CALL_TIME_USER"
    const val PREF_SKIP_RESTORE_FLAG = "PREF_SKIP_RESTORE_FLAG"
    const val PREF_PROJECT_TYPE = "PREF_PROJECT_TYPE"
  }

  override fun clearData() {
    sharedPreferences.edit().clear()
    sharedPreferences.edit().apply()
  }
}

class BooleanPreference(
  private var preferences: SharedPreferences,
  private var name: String,
  private var defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {
  override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
    return preferences.getBoolean(name, defaultValue)
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
    preferences.edit().putBoolean(name, value).apply()
  }

}

class StringPreference(
  private var preferences: SharedPreferences,
  private var name: String,
  private var defaultValue: String?
) : ReadWriteProperty<Any, String?> {
  override fun getValue(thisRef: Any, property: KProperty<*>): String? {
    return preferences.getString(name, defaultValue)
  }

  override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
    preferences.edit().putString(name, value).apply()
  }

}
