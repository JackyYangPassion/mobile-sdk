package com.ripbull.coresdk.typing.repo;

import static com.ripbull.ertc.mqtt.utils.Constants.TYPING_STATUS_TOPIC;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.ripbull.ertc.cache.database.entity.Group;
import com.ripbull.ertc.cache.database.entity.Thread;
import com.ripbull.ertc.cache.database.entity.User;
import com.ripbull.coresdk.core.base.BaseRepository;
import com.ripbull.coresdk.core.event.EventHandler;
import com.ripbull.coresdk.core.event.EventHandlerImpl;
import com.ripbull.coresdk.core.type.EventType;
import com.ripbull.coresdk.core.type.NetworkEvent;
import com.ripbull.coresdk.core.type.ChatType;
import com.ripbull.coresdk.core.type.TypingState;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import com.ripbull.ertc.mqtt.model.TypingIndicator;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

/** Created by DK on 2019-05-04. */
public class TypingRepositoryImpl extends BaseRepository implements TypingRepository {

  public static TypingRepository newInstance(DataManager dataManager) {
    return new TypingRepositoryImpl(dataManager);
  }

  private final Gson gson;
  private EventHandler eventHandler;
  private DataManager dataManager;
  private String chatUserId,deviceId;

  public TypingRepositoryImpl(DataManager dataManager) {
    super(dataManager);
    this.dataManager = dataManager;
    this.eventHandler = EventHandlerImpl.newInstance(dataManager);
    this.gson = new Gson();
  }

  public String getChatUserId() {
    if(this.chatUserId == null){
      this.chatUserId = this.dataManager.preference().getChatUserId();
    }
    return this.chatUserId;
  }

  public String getDeviceId() {
    if(this.deviceId == null){
      this.deviceId = this.dataManager.preference().getDeviceId();
    }
    return this.deviceId;
  }


  @Override
  public Observable<TypingIndicatorRecord> subscribe(String threadId) {
    return typingIndicatorReceiver(
        this.eventHandler.source()
            .filter(NetworkEvent.filterType(EventType.TYPING_STATE_CHANGED)))
        .filter(typingIndicatorRecord -> typingIndicatorRecord.getThreadId().equals(threadId));
  }

  @Override
  public Completable publish(@NonNull String threadId, TypingState state) {
    return Completable.create(
            e -> {
              Thread thread = db().threadDao().getThreadByIdInSync(threadId);
              publishTyping(state, thread);
              e.onComplete();
            })
        .subscribeOn(Schedulers.single());
  }

  private void publishTyping(TypingState typingState, Thread thread) {
    if (thread.getType().equals(ChatType.SINGLE.getType())) {
      ArrayList<String> participants = new ArrayList<>();
      participants.add(thread.getRecipientChatId());
      TypingIndicator typingIndicator =
          new TypingIndicator(thread.getTenantId(), thread.getType(), participants, thread.getId(),
              thread.getSenderChatId(), typingState.getState(), getName());
      this.eventHandler.publish(TYPING_STATUS_TOPIC, gson.toJson(typingIndicator));
    } else if (thread.getType().equals(ChatType.GROUP.getType())) {
      Group group = db().groupDao().getGroupByIdInSync(getTenantId(), thread.getRecipientChatId());
      ArrayList<String> participants = new ArrayList<>();
      for (User user : group.getGroupUsers()) {
        if (!user.getErtcId().equals(getChatUserId())) {
          participants.add(user.getErtcId());
        }
      }
      TypingIndicator typingIndicator =
          new TypingIndicator(thread.getTenantId(), thread.getType(), participants, thread.getId(),
              getChatUserId(), typingState.getState(), getName());

      this.eventHandler.publish(TYPING_STATUS_TOPIC, gson.toJson(typingIndicator));
    }
  }

  private Observable<TypingIndicatorRecord> typingIndicatorReceiver(
      Observable<NetworkEvent> receivedMessage) {
    return receivedMessage.flatMap(
        (Function<NetworkEvent, ObservableSource<TypingIndicatorRecord>>)
            networkEvent ->
                Observable.create(
                        (ObservableOnSubscribe<TypingIndicatorRecord>)
                            e -> {
                              TypingIndicator status =
                                  gson.fromJson(
                                      networkEvent.message().getMessage(), TypingIndicator.class);
                              e.onNext(
                                  new TypingIndicatorRecord(
                                      status.getThreadId(),
                                      status.getThreadType(),
                                      status.getTypingStatusEvent(),
                                      status.getERTCUserId(),
                                      status.getName()));
                            })
                    .subscribeOn(Schedulers.single()));
  }
}
