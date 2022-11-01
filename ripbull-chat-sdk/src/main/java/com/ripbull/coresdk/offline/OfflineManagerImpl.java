package com.ripbull.coresdk.offline;

import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.util.Log;
import com.ripbull.coresdk.nwchange.NwChangeListener;
import com.ripbull.coresdk.nwchange.NwChangeManager;
import com.ripbull.coresdk.nwchange.NwChangeManagerImpl;

public class OfflineManagerImpl implements OfflineManager, NwChangeListener {

  private Context context;

  private static OfflineManager instance;
  NwChangeManager nwChangeManager;

  public static OfflineManager shared(Context context) {
    if (instance == null) {
      synchronized (OfflineManagerImpl.class) {
        if (instance == null) {
          instance = new OfflineManagerImpl(context);
        }
      }
    }
    return instance;
  }

  private OfflineManagerImpl(Context context) {
    this.context = context;
    nwChangeManager = NwChangeManagerImpl.shared(context);
    nwChangeManager.setNwChangeListener(this);
  }

  public void doOfflineMessageTask() {
    Log.i(OfflineMessageService.TAG, "doOfflineMessageTask");
    Intent intent = new Intent(context, OfflineMessageService.class);
    OfflineMessageService.enqueueWork(context, intent);
  }

  @Override
  public void unregister() {
    nwChangeManager.setNwChangeListener(null);
  }

  @Override
  public void onAvailable(Network network) {
    Log.i(OfflineMessageService.TAG, "onAvailable");
    doOfflineMessageTask();
  }

  @Override
  public void onLost(Network network) {
    Log.i(OfflineMessageService.TAG, "onLost");
  }
}
