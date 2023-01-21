package io.inappchat.inappchat.typing.repo;

import androidx.annotation.NonNull;
import io.inappchat.inappchat.core.type.TypingState;
import io.inappchat.inappchat.typing.mapper.TypingIndicatorRecord;
import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;

/** Created by DK on 2019-05-04. */
public interface TypingRepository {

  Observable<TypingIndicatorRecord> subscribe(String threadId);

  Completable publish(@NonNull String threadId, TypingState typingState);
}
