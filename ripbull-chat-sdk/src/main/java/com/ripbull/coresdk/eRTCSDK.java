package com.ripbull.coresdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ripbull.coresdk.announcement.repository.AnnouncementModuleHook;
import com.ripbull.coresdk.chat.repository.ChatModuleHook;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.data.DataManagerImpl;
import com.ripbull.coresdk.data.common.Result;
import com.ripbull.coresdk.fcm.FcmModule;
import com.ripbull.coresdk.group.GroupModule;
import com.ripbull.coresdk.module.ModuleAdapter;
import com.ripbull.coresdk.module.ModuleAdapterImpl;
import com.ripbull.coresdk.notification.repository.NotificationModuleHook;
import com.ripbull.coresdk.offline.OfflineManagerImpl;
import com.ripbull.coresdk.tenant.AuthenticationModule;
import com.ripbull.coresdk.typing.TypingModule;
import com.ripbull.coresdk.user.UserModule;
import com.ripbull.ertc.remote.util.HeaderUtils;
import com.ripbull.sdk.downloader.FileDownloader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

import io.reactivex.Single;
import timber.log.Timber;


public class eRTCSDK {

  private static final eRTCSDK INSTANCE = new eRTCSDK();

  private Configuration config;
  private ModuleAdapter moduleAdapter;
  private WeakReference<Context> context;
  private static Intent fcmIntent;

  /** Initialize all core elements */
  public static void initializeWithConfig(@NonNull Configuration config) {
    config(config);
  }

  public static Single<Result> initializeWithConfigUser(@NonNull Configuration config,
      @NonNull UserConfiguration userConfiguration) {
    config(config);
    registerFCMToServer();
    setDeviceId();
    //tenant().validate(config.getNamespace());
    return tenant().configureUser(userConfiguration);
  }

  private static void config(@NonNull Configuration config) {
    shared().setContext(config.getContext());
    shared().config = config;

    // It'll go to config access
    shared().moduleAdapter = ModuleAdapterImpl.newInstance(data());

    // save config data to preference
    data().preference().setApiKey(config.getApiKey());
    data().preference().setPackageName(config.getContext().getPackageName());
    // set network config
    data().network().api(data().networkConfig());
    OfflineManagerImpl.shared(shared().context());

    //NewRelic.withApplicationToken(getAppContext().getString(R.string.new_relic_sdk_token)).start(getAppContext());
    FileDownloader.initialize(config.getContext());

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Timber.plant(new CrashReportingTree());
    }
  }

  private static class CrashReportingTree extends Timber.Tree {

    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }
    }
  }

  public static void connect() {

    String tenantId = data().preference().getTenantId();
    String chatUserId = data().preference().getChatUserId();
    String mqttServer = data().preference().getMqttServer();
    String deviceId = data().preference().getDeviceId();

    // username : <tenantId>/<X-Request-Signature>/<X-Nonce>
    long time = System.currentTimeMillis();
    String requestSignature = HeaderUtils.getHeaderSignature(data().preference().getMqttApiKey(),
        data().preference().getPackageName(), time);
    String separator = ":";
    String password = requestSignature + separator + time + separator + data().preference().getChatToken();

    if (!TextUtils.isEmpty(tenantId) && !TextUtils.isEmpty(chatUserId)) {
      data().mqtt().createConnection(tenantId, chatUserId, mqttServer, tenantId, password, deviceId);
    }
  }

  public static void registerFCMToServer() {
    FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(task -> {
          if (!task.isSuccessful()) {
            return;
          }
          // Get new Instance ID token
          data().preference().setFcmToken(task.getResult().getToken());
        });
    FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    setDeviceId();
  }

  public static AuthenticationModule tenant() {
    return shared().moduleAdapter.tenant();
  }

  public static ChatModuleHook chat() {
    return shared().moduleAdapter.chat();
  }

  public static UserModule user() {
    return shared().moduleAdapter.user();
  }

  public static TypingModule typing() {
    return shared().moduleAdapter.typing();
  }

  public static GroupModule group() {
    return shared().moduleAdapter.group();
  }

  public static FcmModule fcm() {
    return shared().moduleAdapter.fcm();
  }

  public static EventHandler event() {
    return shared().moduleAdapter.event();
  }

  public static NotificationModuleHook notification() {
    return shared().moduleAdapter.notification();
  }

  public static AnnouncementModuleHook announcement() {
    return shared().moduleAdapter.announcement();
  }

  private static eRTCSDK shared() {
    return INSTANCE;
  }

  private static Configuration config() {
    return shared().config;
  }

  private static DataManager data() {
    final Context context = shared().context();
    return DataManagerImpl.shared(context);
  }

  private void setContext(Context context) {
    this.context = new WeakReference<>(context);
  }

  public Context context() {
    return context.get();
  }

  public static void getIntentForFCM(@NotNull Intent intent) {
    fcmIntent = intent;
  }

  public static Intent getFcmIntent() {
    return fcmIntent;
  }

  public static void saveFCMToken(String token) {
    data().preference().setFcmToken(token);
  }

  private static void setDeviceId() {
    final Context context = shared().context();
    @SuppressLint("HardwareIds")
    String androidId =
        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    data().preference().setDeviceId(androidId);
  }

  public static Context getAppContext() {
    return shared().context();
  }

}
