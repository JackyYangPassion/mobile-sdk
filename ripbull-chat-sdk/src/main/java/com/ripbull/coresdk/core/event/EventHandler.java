package com.ripbull.coresdk.core.event;

import com.ripbull.coresdk.chat.mapper.ChatEvent;
import com.ripbull.coresdk.core.type.NetworkEvent;
import com.ripbull.ertc.mqtt.listener.MqttEventHandler;
import com.ripbull.ertc.mqtt.listener.ReceivedMessage;
import com.ripbull.sdk.downloader.handler.DownloadEventHandler;

import io.reactivex.Completable;
import io.reactivex.Observable;
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
