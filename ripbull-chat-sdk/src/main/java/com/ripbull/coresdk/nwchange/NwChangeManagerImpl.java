package com.ripbull.coresdk.nwchange;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;
import com.ripbull.coresdk.offline.OfflineMessageService;

public class NwChangeManagerImpl extends ConnectivityManager.NetworkCallback
    implements NwChangeManager {

  final private NetworkRequest networkRequest;
  private ConnectivityManager connectivityManager;
  private NwChangeListener nwChangeListener;

  private static NwChangeManager instance;

  public static NwChangeManager shared(Context context) {
    if (instance == null) {
      synchronized (NwChangeManager.class) {
        if (instance == null) {
          instance = new NwChangeManagerImpl(context);
        }
      }
    }
    return instance;
  }

  private NwChangeManagerImpl(Context context) {
    networkRequest =
        new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build();
    connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    connectivityManager.registerNetworkCallback(networkRequest, this);
  }

  @Override
  public void enable() {

  }

  @Override
  public void disable() {
    connectivityManager.unregisterNetworkCallback(this);
  }

  @Override
  public void setNwChangeListener(NwChangeListener listener) {
    nwChangeListener = listener;
  }

  @Override
  public void onAvailable(Network network) {
    Log.i(OfflineMessageService.TAG, "NwChangeManagerImpl: onAvailable");
    if (nwChangeListener != null) {
      nwChangeListener.onAvailable(network);
    }
  }

  @Override
  public void onLost(Network network) {
    Log.i(OfflineMessageService.TAG, "NwChangeManagerImpl: onLost");
    if (nwChangeListener != null) {
      nwChangeListener.onLost(network);
    }
  }
}
