package io.inappchat.inappchat.core.event;

import com.google.gson.Gson;
import io.inappchat.inappchat.user.model.UserDBUpdate;
import io.inappchat.inappchat.cache.preference.PreferenceManager;
import io.inappchat.inappchat.remote.model.response.UserSelfUpdateResponse;
import io.inappchat.inappchat.chat.mapper.ChatEvent;
import io.inappchat.inappchat.core.type.NetworkEvent;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.utils.CrashReportingObserver;
import io.inappchat.inappchat.utils.DisposableList;
import io.inappchat.inappchat.utils.Logger;
import io.inappchat.inappchat.wrappers.ChatWrapper;
import io.inappchat.inappchat.wrappers.MessageWrapper;
import io.inappchat.inappchat.wrappers.ThreadWrapper;
import io.inappchat.inappchat.wrappers.UserWrapper;
import io.inappchat.inappchat.mqtt.MqttManager;
import io.inappchat.inappchat.mqtt.listener.MqttEventHandler;
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage;
import io.inappchat.inappchat.mqtt.utils.Constants;
import io.inappchat.inappchat.downloader.handler.DownloadEventHandler;
import io.inappchat.inappchat.downloader.utils.Status;

import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/** Created by DK on 04/04/19. */
public class EventHandlerImpl implements EventHandler, MqttEventHandler, DownloadEventHandler {

  private static EventHandlerImpl instance;
  private final PublishSubject<NetworkEvent> eventSource = PublishSubject.create();
  private final PublishSubject<ChatEvent> chatEventSource = PublishSubject.create();
  private ChatWrapper wrapper;
  private UserWrapper userWrapper;
  private MessageWrapper messageWrapper;
  private ThreadWrapper threadWrapper;
  private MqttManager mqttManager;
  private DisposableList disposableList = new DisposableList();
  private PreferenceManager preferenceManager;

  public static EventHandler newInstance(DataManager dataManager) {
    if (instance == null) {
      instance = new EventHandlerImpl(dataManager);
    }
    return instance;
  }

  private EventHandlerImpl(DataManager dataManager) {
    this.wrapper = new ChatWrapper(dataManager,this);
    this.messageWrapper = new MessageWrapper(dataManager);
    this.userWrapper = new UserWrapper(dataManager);
    this.threadWrapper = new ThreadWrapper(dataManager);
    this.mqttManager = dataManager.mqtt();
    this.mqttManager.setEventHandler(this);
    this.preferenceManager = dataManager.preference();
  }

  @Override
  public void messageFromFcm(String topic, ReceivedMessage message) {
    Logger.d(Logger.FCM, topic + " <====  : " + message.getMessage());
    messageArrived(topic, message);
  }

  @Override
  public void messageFromMQTT(String topic, ReceivedMessage message) {
    Logger.d(Logger.MQTT, topic + " <====  : " + message.getMessage());
    messageArrived(topic, message);
  }

  private void messageArrived(String topic, ReceivedMessage message) {

    if (topic.contains(Constants.CHAT_TOPIC)) {
      // ThreadWrapper threadWrapper = new ThreadWrapper(dataManager,message);
      wrapper
          .messagesOn(message)
          .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.messageAdded(messageRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.CHAT_UPDATE_TOPIC)) {
      wrapper
           .onChatUpdate(message)
           .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.messageUpdated(messageRecord)))
           .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.MSG_READ_STATUS_TOPIC)) {
      wrapper
          .msgReadStatus(message)
          .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.messageUpdated(messageRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.TYPING_STATUS_TOPIC)) {
      eventSource.onNext(NetworkEvent.typingStateChanged(message));
    } else if (topic.contains(Constants.AVAILABILITY_STATUS_TOPIC)) {
      userWrapper
        .userMetaUpdated(message)
        .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.userMetaDataUpdated(message)))
        .subscribe(new CrashReportingObserver<>(disposableList));
//      eventSource.onNext(NetworkEvent.userMetaUpdated(message));
    } else if (topic.contains(Constants.GROUP_UPDATED)) {
      wrapper
          .groupUpdated(message)
          .doOnNext(groupRecord -> eventSource.onNext(NetworkEvent.groupUpdated(groupRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
      wrapper
          .groupUpdatedMessage(message)
          .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.messageAdded(messageRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.USER_SELF_UPDATE)) {
      UserSelfUpdateResponse response = new Gson().fromJson(message.getMessage(), UserSelfUpdateResponse.class);
      if (response.getEventList().size() == 0) {
        return;
      }
      UserSelfUpdateResponse.EventItem eventItem = response.getEventList().get(0);

      if (eventItem.getEventType().equals(io.inappchat.inappchat.utils.Constants.THREAD_NOTIFICATION_SETTINGS_CHANGED)) {
        threadWrapper
            .threadMetaDataUpdated(eventItem)
            .doOnNext(chatMetaDataRecord -> eventSource.onNext(NetworkEvent.threadMetaUpdated(chatMetaDataRecord)))
            .subscribe(new CrashReportingObserver<>(disposableList));
      } else if (eventItem.getEventType().equals(io.inappchat.inappchat.utils.Constants.GLOBAL_NOTIFICATION_SETTINGS_CHANGED)) {
        userWrapper
            .userMetaDataUpdated(eventItem)
            .doOnNext(userMetaDataRecord -> eventSource.onNext(NetworkEvent.userMetaDataUpdated(userMetaDataRecord)))
            .subscribe(new CrashReportingObserver<>(disposableList));
      } else if (eventItem.getEventType().equals(io.inappchat.inappchat.utils.Constants.AVAILABILITY_STATUS_CHANGED)) {
        userWrapper
            .userSelfAvailabilityUpdated(eventItem)
            .doOnNext(userMetaDataRecord -> eventSource.onNext(NetworkEvent.userMetaDataUpdated(userMetaDataRecord)))
            .subscribe(new CrashReportingObserver<>(disposableList));
      } else if (eventItem.getEventType().equals(io.inappchat.inappchat.utils.Constants.USER_BLOCKED_STATUS_CHANGED)) {
        userWrapper
            .userBlockStatusUpdated(eventItem)
            .doOnNext(userRecord -> eventSource.onNext(NetworkEvent.userMetaDataUpdated(userRecord)))
            .subscribe(new CrashReportingObserver<>(disposableList));
      }
    } else if (topic.contains(Constants.CHAT_REACTION)) {
      messageWrapper
          .messageMetaData(message,Constants.CHAT_REACTION)
          .doOnNext(messageMetaDataRecord -> eventSource.onNext(NetworkEvent.messageMetaData(messageMetaDataRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.ANNOUNCEMENT)) {
      wrapper
          .announcementTrigger(message)
          .doOnNext(announcementRecord -> eventSource.onNext(NetworkEvent.announcement(announcementRecord)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.CHAT_HISTORY_CLEARED)) {
      threadWrapper
          .chatCleared(message)
          .doOnNext(receivedMessage -> eventSource.onNext(NetworkEvent.chatCleared(message)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.LOGOUT)) {
      wrapper
          .autoLogout()
          .doOnNext(result -> eventSource.onNext(NetworkEvent.logout(result)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.TENANT_CONFIG_MODIFIED)) {
      wrapper
          .updateFeatures()
          .doOnNext(result -> eventSource.onNext(NetworkEvent.tenantConfigUpdated(result)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    } else if (topic.contains(Constants.USER_DB_UPDATED)) {
      UserDBUpdate userDBUpdate = new Gson().fromJson(message.getMessage(), UserDBUpdate.class);
      if (userDBUpdate.getEvent().equals(Constants.USER_ADD_UPDATED)) {
        userWrapper
            .userAddUpdated(userDBUpdate)
            .doOnNext(userRecord -> eventSource.onNext(NetworkEvent.userDbUpdated(userRecord)))
            .subscribe(new CrashReportingObserver<>(disposableList));
      } else if (userDBUpdate.getEvent().equals(Constants.USER_DELETED)) {
        for (int i=0; i < userDBUpdate.getAppUserIds().size(); i++) {
          if (userDBUpdate.getAppUserIds().get(i).equals(preferenceManager.getAppUserId())) {
            userDBUpdate.getAppUserIds().remove(i);
            wrapper
                .autoLogout()
                .doOnNext(result -> eventSource.onNext(NetworkEvent.logout(result)))
                .subscribe(new CrashReportingObserver<>(disposableList));

            break;
          }
        }
        if (!userDBUpdate.getAppUserIds().isEmpty()) {
          userWrapper
              .userDeleted(userDBUpdate)
              .doOnNext(userRecord -> eventSource.onNext(NetworkEvent.userDbUpdated(userRecord)))
              .subscribe(new CrashReportingObserver<>(disposableList));
        }
      } else if (userDBUpdate.getEvent().equals(Constants.USER_INACTIVE)) {
        for (int i=0; i < userDBUpdate.getAppUserIds().size(); i++) {
          if (userDBUpdate.getAppUserIds().get(i).equals(preferenceManager.getAppUserId())) {
            userDBUpdate.getAppUserIds().remove(i);
            wrapper
                .autoLogout()
                .doOnNext(result -> eventSource.onNext(NetworkEvent.logout(result)))
                .subscribe(new CrashReportingObserver<>(disposableList));

            break;
          }
        }
        if (!userDBUpdate.getAppUserIds().isEmpty()) {
          userWrapper
              .userInactivated(userDBUpdate)
              .doOnNext(userRecord -> eventSource.onNext(NetworkEvent.userDbUpdated(userRecord)))
              .subscribe(new CrashReportingObserver<>(disposableList));
        }
      }
    } else if (topic.contains(Constants.CHAT_SETTING_UPDATED)) {
      wrapper
          .fetchChatSettings(message)
          .doOnNext(result -> eventSource.onNext(NetworkEvent.fetchChatSettings(result)))
          .subscribe(new CrashReportingObserver<>(disposableList));
    }
  }

  @Override
  public void download(String msgId, String localPath, Status status) {
    // send download event to UI
    wrapper.download(msgId, localPath, status)
        .doOnNext(messageRecord -> eventSource.onNext(NetworkEvent.messageMediaDownload(messageRecord)))
        .subscribe(new CrashReportingObserver<>(disposableList));
  }

  @Override
  public void connectionLost(Throwable cause) { }

  @Override
  public Completable markAsRead(String threadId,String appUserId,String parentMsgId) {

    return wrapper
        .markAsRead(threadId,appUserId, parentMsgId);
        // .doOnEvent(typingIndicatorRecord ->
        // eventSource.onNext(NetworkEvent.messageUpdated(typingIndicatorRecord)))
        //.subscribe(new CrashReportingCompletableObserver(disposableList));
  }

  @Override
  public void publish(String topic, String message) {
    Logger.d(Logger.MQTT, topic+ " publish ====> " + message);
    this.mqttManager.publish(topic, message);
  }

  @Override
  public PublishSubject<NetworkEvent> source() {
    return eventSource;
  }

  @Override
  public PublishSubject<ChatEvent> chatEventSource() {
    return  chatEventSource;
  }

  @Override
  public Observable<ChatEvent> sourceOnMain() {
    return chatEventSource.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }
}
