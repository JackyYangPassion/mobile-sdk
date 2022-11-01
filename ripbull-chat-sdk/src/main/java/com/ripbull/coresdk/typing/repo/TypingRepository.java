package com.ripbull.coresdk.typing.repo;

import androidx.annotation.NonNull;
import com.ripbull.coresdk.core.type.TypingState;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import io.reactivex.Completable;
import io.reactivex.Observable;

/** Created by DK on 2019-05-04. */
public interface TypingRepository {

  Observable<TypingIndicatorRecord> subscribe(String threadId);

  Completable publish(@NonNull String threadId, TypingState typingState);
}
