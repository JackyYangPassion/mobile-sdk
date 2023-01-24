package io.inappchat.inappchat.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.inappchat.inappchat.InAppChat;
import io.inappchat.inappchat.utils.Logger;

public class IACFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = IACFirebaseMessagingService.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    Logger.d(TAG, "onCreate: ");
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
   InAppChat.fcm().onMessageReceived(remoteMessage);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Logger.d(TAG, "onDestroy: ");
  }

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);
    InAppChat.saveFCMToken(token);
  }
}
