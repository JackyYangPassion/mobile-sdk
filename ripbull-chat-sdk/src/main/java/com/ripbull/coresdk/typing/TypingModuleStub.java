package com.ripbull.coresdk.typing;

import android.content.Context;
import com.ripbull.coresdk.R;
import com.ripbull.coresdk.core.ChatSDKException;
import com.ripbull.coresdk.core.type.TypingState;
import com.ripbull.coresdk.eRTCSDK;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import com.ripbull.coresdk.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.Observable;

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