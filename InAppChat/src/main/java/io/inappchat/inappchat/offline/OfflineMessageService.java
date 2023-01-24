package io.inappchat.inappchat.offline;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import io.inappchat.inappchat.chat.mapper.ChatRequestMapper;
import io.inappchat.inappchat.core.type.MessageStatus;
import io.inappchat.inappchat.core.type.MessageType;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.data.DataManagerImpl;
import io.inappchat.inappchat.data.common.Result;
import io.inappchat.inappchat.utils.Constants;
import io.inappchat.inappchat.cache.database.entity.SingleChat;
import io.inappchat.inappchat.remote.model.request.MessageRequest;
import io.inappchat.inappchat.remote.model.response.MessageResponse;
import io.inappchat.inappchat.remote.model.response.OfflineMessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import timber.log.Timber;


public class OfflineMessageService extends JobIntentService {

  public static final String TAG = "OfflineMessage";
  public static final String CLINET_MULTIPLE_MSG_SENT = "sendMultipleMsgs";
  private CompositeDisposable disposable = new CompositeDisposable();
  /**
   * Unique job ID for this service.
   */
  static final int JOB_ID = 10001;

  /**
   * Convenience method for enqueuing work in to this service.
   */
  public static void enqueueWork(Context context, Intent work) {
    Timber.i("enqueueWork");
    enqueueWork(context, OfflineMessageService.class, JOB_ID, work);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    disposable.dispose();
  }

  @Override
  protected void onHandleWork(@NonNull Intent intent) {

    Timber.i("onHandleWork");
    /*disposable.add(data().db()
        .tenantDao()
        .getTenantConfigValue(data().preference().getTenantId(), Constants.TenantConfig.E2E_CHAT)
        .subscribeOn(Schedulers.io())
        .subscribe(flag -> {
          if (TextUtils.isEmpty(flag) || flag.equals("1")) {
            Timber.i("e2e not supporting offline messages:");
          }
        }, Throwable::printStackTrace));*/
    String e2eFlag = data().db().tenantDao().getFeatureEnabled(data().preference().getTenantId(), Constants.TenantConfig.E2E_CHAT);

    if (e2eFlag != null && e2eFlag.equals("1")) {
      E2EOfflineMessage();
    } else {
      // get offline message only text
      List<String> messageTypes = new ArrayList<>();
      messageTypes.add(MessageType.TEXT.getType());
      List<SingleChat> messages = data().db()
          .singleChatDao()
          .getSendingMessages(MessageStatus.SENDING.getStatus(), messageTypes);

      if (messages != null && !messages.isEmpty()) {
        String appUserId = data().preference().getChatUserId();

        Timber.i("offline messages:" + messages.size());
        List<MessageRequest> messageRequests = new ArrayList<>();
        for (SingleChat singleChat : messages) {
          if (singleChat != null) {
            MessageRequest messageRequest =
                ChatRequestMapper.INSTANCE.textRequest(Objects.requireNonNull(singleChat.getThreadId()),
                    Objects.requireNonNull(appUserId),
                    Objects.requireNonNull(singleChat.getMessage()), singleChat.getId(),
                    singleChat.getClientCreatedAt(), singleChat.getCustomData());
            messageRequests.add(messageRequest);
          }
        }

        String apiKey = data().preference().getApiKey();
        String tenantId = data().preference().getTenantId();

        disposable.add(data().network()
            .api()
            .sendMessages(Objects.requireNonNull(apiKey), Objects.requireNonNull(tenantId),
                Objects.requireNonNull(appUserId), messageRequests)
            .flatMap((Function<OfflineMessageResponse, SingleSource<List<Result>>>) responses -> {
              Timber.i("offline messages resp:" + responses);
              List<Result> results = new ArrayList<>();
              for (MessageResponse response : responses.getMessageResponse()) {
                String msgId1 = response.getData().getRequestId();
                SingleChat singleChat = this.data()
                    .db()
                    .singleChatDao()
                    .getChatByLocalMsgId(msgId1, response.getThreadId());
                singleChat.setMsgUniqueId(response.getMsgUniqueId());
                singleChat.setStatus(MessageStatus.SENT.getStatus());
                this.data().db().singleChatDao().insertWithReplace(singleChat);
                Result result = new Result(true, response.getMsgUniqueId(), "");
                results.add(result);
              }
              return Single.just(results);
            })
            .subscribe(results -> Timber.i("offline messages sent"), throwable -> {
              Timber.e("offline messages not sent");
              throwable.printStackTrace();
            }));
      } else {
        Timber.i("no offline messages");
      }
    }
  }

  public DataManager data() {
    return DataManagerImpl.shared(getApplicationContext());
  }

  private void E2EOfflineMessage() {
    // get offline message only text
    List<String> messageTypes = new ArrayList<>();
    messageTypes.add(MessageType.TEXT.getType());
    List<SingleChat> messages = data().db()
        .singleChatDao()
        .getSendingMessages(MessageStatus.SENDING.getStatus(), messageTypes);

    if (messages != null && !messages.isEmpty()) {
      String appUserId = data().preference().getChatUserId();
      String tenantId = data().preference().getTenantId();

      Timber.i("offline messages:" + messages.size());
      for (SingleChat singleChat : messages) {
        if (singleChat != null) {
          OfflineMessageSent.Companion.sendE2EMessage(disposable, tenantId, singleChat, appUserId, data(), null);
        }
      }
    } else {
      Timber.i("no offline messages");
    }
  }
}
