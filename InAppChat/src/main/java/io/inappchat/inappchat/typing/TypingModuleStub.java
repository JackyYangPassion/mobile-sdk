package io.inappchat.inappchat.typing;

import android.content.Context;
import io.inappchat.inappchat.R;
import io.inappchat.inappchat.core.ChatSDKException;
import io.inappchat.inappchat.core.type.TypingState;
import io.inappchat.inappchat.eRTCSDK;
import io.inappchat.inappchat.typing.mapper.TypingIndicatorRecord;
import io.inappchat.inappchat.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;

/**
 * Created by DK on 2019-09-24.
 */
public class TypingModuleStub implements TypingModule{

  private Context appContext;

  public static TypingModule newInstance() {
    return new TypingModuleStub(eRTCSDK.getAppContext());
  }

  private TypingModuleStub(Context context) {
    this.appContext = context;
  }

  @Override
  public Observable<TypingIndicatorRecord> subscribe(String threadId) {
    return Observable.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.TYPING)));
  }

  @Override
  public Completable publish(String threadId, TypingState typingState) {
    return Completable.error(new ChatSDKException(new ChatSDKException.Error.InvalidModule(),
        appContext.getString(R.string.alert_message, Constants.Features.TYPING)));
  }
}