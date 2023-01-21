package io.inappchat.inappchat.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

  /**
   * Receive swipe/dismiss or delete action from user.This will not be triggered if you manually
   * cancel the notification.
   */
  @Override
  public void onReceive(Context context, Intent intent) {
    /**
     * As soon as received,remove it from shared preferences,meaning the notification no longer
     * available on the tray for user so you do not need to worry.
     */
    NotificationPreferenceManager preferenceManager = new NotificationPreferenceManager(context);
    preferenceManager.removeNotification(intent.getExtras().getString("threadId"));
  }
}
