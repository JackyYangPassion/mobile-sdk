package com.ripbull.coresdk.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CrashReportingObserver<T> extends CrashReporter implements Observer<T> {

  public CrashReportingObserver(DisposableList list) {
    super(list);
  }

  public CrashReportingObserver() {}

  @Override
  public void onSubscribe(Disposable d) {
    super.onSubscribe(d);
  }

  @Override
  public void onNext(Object o) {}

  @Override
  public void onError(Throwable e) {
    super.onError(e);
  }

  @Override
  public void onComplete() {}
}
