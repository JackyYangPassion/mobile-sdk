package com.ripbull.coresdk.utils;

import io.reactivex.disposables.Disposable;
import java.lang.ref.WeakReference;

public class CrashReporter {

  private WeakReference<DisposableList> list;

  CrashReporter(DisposableList list) {
    this.list = new WeakReference<>(list);
  }

  public CrashReporter() {}

  public void onSubscribe(Disposable d) {
    if (list != null) {
      list.get().add(d);
    }
  }

  public void onNext(Object o) {}

  public void onError(Throwable e) {
    e.printStackTrace();
  }

  public void onComplete() {}
}
