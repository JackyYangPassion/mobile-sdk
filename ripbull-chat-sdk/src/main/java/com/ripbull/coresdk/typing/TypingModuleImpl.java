package com.ripbull.coresdk.typing;

import com.ripbull.coresdk.core.type.TypingState;
import com.ripbull.coresdk.data.DataManager;
import com.ripbull.coresdk.typing.mapper.TypingIndicatorRecord;
import com.ripbull.coresdk.typing.repo.TypingRepository;
import com.ripbull.coresdk.typing.repo.TypingRepositoryImpl;
import io.reactivex.Completable;
import io.reactivex.Observable;

/** Created by DK on 2019-05-04. */
public class TypingModuleImpl implements TypingModule {

  private final TypingRepository repository;

  public static TypingModule newInstance(DataManager dataManager) {
    TypingRepository typingRepository = TypingRepositoryImpl.newInstance(dataManager);
    return new TypingModuleImpl(typingRepository);
  }

  private TypingModuleImpl(TypingRepository repository) {
    this.repository = repository;
  }

  @Override
  public Observable<TypingIndicatorRecord> subscribe(String threadId) {
    return repository.subscribe(threadId);
  }

  @Override
  public Completable publish(String threadId, TypingState typingState) {
    return repository.publish(threadId, typingState);
  }
}
