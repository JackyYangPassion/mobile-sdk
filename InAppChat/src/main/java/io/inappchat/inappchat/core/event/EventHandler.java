package io.inappchat.inappchat.core.event;

import io.inappchat.inappchat.chat.mapper.ChatEvent;
import io.inappchat.inappchat.core.type.NetworkEvent;
import io.inappchat.inappchat.mqtt.listener.MqttEventHandler;
import io.inappchat.inappchat.mqtt.listener.ReceivedMessage;
import io.inappchat.inappchat.downloader.handler.DownloadEventHandler;

import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.subjects.PublishSubject;

/** Created by DK on 04/04/19. */
public interface EventHandler extends MqttEventHandler, DownloadEventHandler {
  void messageFromFcm(String topic, ReceivedMessage message) throws Exception;

  PublishSubject<NetworkEvent> source();

  Observable<ChatEvent> sourceOnMain();

  PublishSubject<ChatEvent> chatEventSource();

  void publish(String topic, String message);

  Completable markAsRead(String threadId,String appUserId, String parentMsgId);


}
