package com.ripbull.coresdk.fcm;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.RemoteMessage;

public interface FcmModule {

  void onMessageReceived(@NonNull RemoteMessage remoteMessage);

  void clearNotification();
}
