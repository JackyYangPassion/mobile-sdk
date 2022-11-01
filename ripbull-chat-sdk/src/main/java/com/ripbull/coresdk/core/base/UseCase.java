package com.ripbull.coresdk.core.base;

import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/** Created by DK on 05/12/18. */
public abstract class UseCase<T> {

  private T t;

  public UseCase(T t) {
    this.t = t;
  }

  public T data() {
    return this.t;
  }

  private final SingleTransformer schedulersTransformer =
      single -> single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

  @SuppressWarnings("unchecked")
  public <T> SingleTransformer<T, T> applySchedulers() {
    return schedulersTransformer;
  }
}
