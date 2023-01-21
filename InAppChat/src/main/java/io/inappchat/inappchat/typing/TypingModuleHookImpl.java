package io.inappchat.inappchat.typing;

import androidx.annotation.NonNull;
import io.inappchat.inappchat.cache.database.dao.TenantDao;
import io.inappchat.inappchat.core.type.TypingState;
import io.inappchat.inappchat.data.DataManager;
import io.inappchat.inappchat.typing.mapper.TypingIndicatorRecord;
import io.inappchat.inappchat.utils.Constants;
import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.functions.Function;

/** Created by DK on 05/12/18. */
public class TypingModuleHookImpl implements TypingModuleHook, TypingModule {

  private final TypingModule typingModule;
  private final TypingModule stub;
  private DataManager dataManager;

  public static TypingModuleHook newInstance(DataManager dataManager) {
    TypingModule typingModule = TypingModuleImpl.newInstance(dataManager);
    TypingModule stub = TypingModuleStub.newInstance();
    return new TypingModuleHookImpl(typingModule, stub, dataManager);
  }

  private TypingModuleHookImpl(@NonNull TypingModule typingModule, @NonNull TypingModule stub,
      DataManager dataManager) {
    this.typingModule = typingModule;
    this.stub = stub;
    this.dataManager = dataManager;
  }

  @Override
  public TypingModule provideModule() {
    return this;
  }

  @Override
  public Observable<TypingIndicatorRecord> subscribe(String threadId) {
    return isSubscriptionAvailable() ? typingModule.subscribe(threadId) : stub.subscribe(threadId);
  }

  private boolean isSubscriptionAvailable() {
    return true; // data to be fetch from preference
  }

  private Single<Boolean> canPublish() {
    return isFeatureEnabled(
        Constants.TenantConfig.TYPING_STATUS); // data to be fetch from preference
  }

  private TenantDao getTenantDao() {
    return dataManager.db().tenantDao();
  }

  private String getTenantId() {
    return dataManager.preference().getTenantId();
  }

  private Single<Boolean> isFeatureEnabled(String feature) {
    return getTenantDao().getTenantConfigValue(getTenantId(), feature)
        .flatMap((Function<String, SingleSource<Boolean>>) s -> Single.just(s.equals("1")));
  }

  @Override
  public Completable publish(String threadId, TypingState typingState) {
    return canPublish().flatMapCompletable(
        aBoolean -> aBoolean ? typingModule.publish(threadId, typingState)
            : stub.publish(threadId, typingState));
  }
}
