package io.inappchat.inappchat.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import io.inappchat.inappchat.eRTCSDK;
import io.inappchat.inappchat.utils.Logger;

public class ERTCFirebaseMessagingService extends FirebaseMessagingService {

  private static final String TAG = ERTCFirebaseMessagingService.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    Logger.d(TAG, "onCreate: ");
  }

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
   eRTCSDK.fcm().onMessageReceived(remoteMessage);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Logger.d(TAG, "onDestroy: ");
  }

  @Override
  public void onNewToken(String token) {
    super.onNewToken(token);
    eRTCSDK.saveFCMToken(token);
  }
}
