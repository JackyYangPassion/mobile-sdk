package io.inappchat.inappchat.utils;

import io.reactivex.disposables.Disposable;
import java.util.ArrayList;

public class DisposableList {

  private ArrayList<Disposable> disposables = new ArrayList<>();

  public void add(Disposable d) {
    disposables.add(d);
  }

  public void remove(Disposable d) {
    disposables.remove(d);
  }

  public void dispose() {
    for (Disposable d : disposables) {
      d.dispose();
    }
    disposables.clear();
  }
}
