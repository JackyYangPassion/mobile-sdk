package com.ripbull.coresdk.typing;

import com.ripbull.coresdk.core.type.TypingState;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import io.reactivex.Completable;
import io.reactivex.Observable;

/** Created by DK on 22/04/19. */
public interface TypingModule {

  Observable<TypingIndicatorRecord> subscribe(String threadId);

  Completable publish(String threadId, TypingState typingState);
}
