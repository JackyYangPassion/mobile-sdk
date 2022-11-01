package com.ripbull.coresdk.fcm;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPreferenceManager {
  private String TAG = NotificationPreferenceManager.class.getSimpleName();

  // Shared Preferences
  SharedPreferences pref;

  // Editor for Shared preferences
  SharedPreferences.Editor editor;

  // Context
  Context _context;

  // Shared pref mode
  int PRIVATE_MODE = 0;

  // Shared pref file name
  private static final String PREF_NAME = "ertc_notification";

  // Constructor
  protected NotificationPreferenceManager(Context context) {
    this._context = context;
    pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    editor = pref.edit();
  }

  protected void setNotificationId(String key, int notificationId) {
    editor.putInt(key, notificationId);
    editor.commit();
  }

  protected int getNotificationId(String key) {
    return pref.getInt(key, 0);
  }

  protected void addNotification(String key, String notification) {

    // get old notifications
    String oldNotifications = getNotifications(key);

    if (oldNotifications != null) {
      oldNotifications += "|&#@|" + notification;
    } else {
      oldNotifications = notification;
    }

    editor.putString(key, oldNotifications);
    editor.commit();
  }

  protected String getNotifications(String key) {
    return pref.getString(key, null);
  }

  protected void removeNotification(String key) {
    editor.remove("threadId:" + key);
    editor.remove(key);
    editor.commit();
  }

  protected void clearNotifications() {
    editor.clear();
    editor.commit();
  }
}
