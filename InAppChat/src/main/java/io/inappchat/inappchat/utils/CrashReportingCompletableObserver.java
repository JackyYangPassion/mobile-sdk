package io.inappchat.inappchat.utils;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class CrashReportingCompletableObserver extends CrashReporter
    implements CompletableObserver {

  public CrashReportingCompletableObserver(DisposableList list) {
    super(list);
  }

  public CrashReportingCompletableObserver() {}

  @Override
  public void onSubscribe(Disposable d) {
    super.onSubscribe(d);
  }

  @Override
  public void onComplete() {}

  @Override
  public void onError(Throwable e) {
    super.onError(e);
  }
}
