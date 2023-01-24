package io.inappchat.inappchat.typing;

import io.inappchat.inappchat.core.type.TypingState;
import io.inappchat.inappchat.typing.mapper.TypingIndicatorRecord;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

/** Created by DK on 22/04/19. */
public interface TypingModule {

  Observable<TypingIndicatorRecord> subscribe(String threadId);

  Completable publish(String threadId, TypingState typingState);
}
