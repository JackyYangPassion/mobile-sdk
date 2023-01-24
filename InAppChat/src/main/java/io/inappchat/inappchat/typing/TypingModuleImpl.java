package io.inappchat.inappchat.typing;

import io.inappchat.inappchat.core.type.TypingState;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.typing.mapper.TypingIndicatorRecord;
import io.inappchat.inappchat.typing.repo.TypingRepository;
import io.inappchat.inappchat.typing.repo.TypingRepositoryImpl;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

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
